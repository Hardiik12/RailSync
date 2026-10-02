package com.railsync.algorithm.m4.dinic;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import com.railsync.algorithm.core.Algorithm;
import com.railsync.algorithm.m4.common.FlowEdge;
import com.railsync.algorithm.m4.common.FlowNetwork;
import com.railsync.algorithm.m4.common.dto.AugmentingPathDto;
import com.railsync.algorithm.m4.common.dto.EdgeInputDto;
import com.railsync.algorithm.m4.common.dto.FlowEdgeDto;
import com.railsync.algorithm.m4.dinic.dto.DinicInput;
import com.railsync.algorithm.m4.dinic.dto.DinicResult;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DinicAlgorithm implements Algorithm<DinicInput, DinicResult> {

    @Override
    public String getName() {
        return "Dinic's Algorithm (Level Graph + Blocking Flow)";
    }

    @Override
    public Complexity getComplexity() {
        return Complexity.builder()
                .time("O(V^2 * E)")
                .space("O(V + E)")
                .build();
    }

    @Override
    public DinicResult execute(DinicInput input) {
        long startTime = System.nanoTime();

        int n = input.getVertexCount() != null ? input.getVertexCount() : 0;
        int s = input.getSource() != null ? input.getSource() : 0;
        int t = input.getSink() != null ? input.getSink() : 0;
        boolean traceEnabled = Boolean.TRUE.equals(input.getTraceEnabled());
        int maxTraceSteps = input.getMaxTraceSteps() != null ? input.getMaxTraceSteps() : 500;

        List<TraceStep> trace = new ArrayList<>();
        List<AugmentingPathDto> blockingFlows = new ArrayList<>();

        if (n <= 0 || s < 0 || s >= n || t < 0 || t >= n || s == t || input.getEdges() == null) {
            long endTime = System.nanoTime();
            return DinicResult.builder()
                    .maxFlow(0.0)
                    .phaseCount(0)
                    .blockingFlows(Collections.emptyList())
                    .finalEdges(Collections.emptyList())
                    .comparisons(0)
                    .executionTimeNanos(endTime - startTime)
                    .complexity(getComplexity())
                    .trace(trace)
                    .build();
        }

        FlowNetwork network = new FlowNetwork(n);
        for (EdgeInputDto edge : input.getEdges()) {
            if (edge.getU() != null && edge.getV() != null && edge.getCapacity() != null
                    && edge.getU() >= 0 && edge.getU() < n && edge.getV() >= 0 && edge.getV() < n
                    && edge.getCapacity() >= 0) {
                network.addEdge(edge.getU(), edge.getV(), edge.getCapacity(), edge.getUName(), edge.getVName());
            }
        }

        double maxFlow = 0.0;
        long comparisons = 0;
        int stepCounter = 1;
        int phaseCounter = 0;

        while (true) {
            int[] level = new int[n];
            Arrays.fill(level, -1);
            level[s] = 0;

            Queue<Integer> queue = new LinkedList<>();
            queue.add(s);

            while (!queue.isEmpty()) {
                int u = queue.poll();
                comparisons++;

                for (FlowEdge edge : network.getAdj(u)) {
                    int v = (edge.getU() == u) ? edge.getV() : edge.getU();
                    if (level[v] == -1 && edge.residualCapacityTo(v) > 0) {
                        level[v] = level[u] + 1;
                        queue.add(v);
                    }
                }
            }

            if (level[t] == -1) {
                break; // Sink unreachable in level graph
            }

            phaseCounter++;
            if (traceEnabled && trace.size() < maxTraceSteps) {
                trace.add(TraceStep.builder()
                        .step(stepCounter++)
                        .action("DINIC_LEVEL_GRAPH")
                        .state(Map.of("phase", phaseCounter, "sinkLevel", level[t]))
                        .description(String.format("Constructed Phase %d BFS level graph (Sink level: %d)", phaseCounter, level[t]))
                        .build());
            }

            int[] ptr = new int[n];
            while (true) {
                List<FlowEdge> pathEdges = new ArrayList<>();
                double pushed = dfsBlockingFlow(network, s, t, Double.POSITIVE_INFINITY, level, ptr, pathEdges);
                if (pushed <= 0) break;

                maxFlow += pushed;

                List<Integer> nodePath = new ArrayList<>();
                List<String> nodeNamePath = new ArrayList<>();
                nodePath.add(s);
                int currNode = s;
                for (FlowEdge edge : pathEdges) {
                    currNode = (edge.getU() == currNode) ? edge.getV() : edge.getU();
                    nodePath.add(currNode);
                }

                for (int node : nodePath) {
                    nodeNamePath.add(network.getAdj(node).isEmpty() ? "Node " + node :
                            (nodePath.indexOf(node) < nodePath.size() - 1 ?
                                    (pathEdges.get(nodePath.indexOf(node)).getU() == node ? pathEdges.get(nodePath.indexOf(node)).getUName() : pathEdges.get(nodePath.indexOf(node)).getVName()) :
                                    (pathEdges.get(pathEdges.size() - 1).getV() == node ? pathEdges.get(pathEdges.size() - 1).getVName() : pathEdges.get(pathEdges.size() - 1).getUName())));
                }

                AugmentingPathDto flowDto = AugmentingPathDto.builder()
                        .nodePath(nodePath)
                        .nodeNamePath(nodeNamePath)
                        .bottleneckCapacity(pushed)
                        .updatedMaxFlow(maxFlow)
                        .build();
                blockingFlows.add(flowDto);

                if (traceEnabled && trace.size() < maxTraceSteps) {
                    trace.add(TraceStep.builder()
                            .step(stepCounter++)
                            .action("DINIC_BLOCKING_FLOW")
                            .state(Map.of("phase", phaseCounter, "pushedFlow", pushed, "maxFlow", maxFlow))
                            .description(String.format("Pushed blocking flow %.2f in Phase %d (Total Flow: %.2f)", pushed, phaseCounter, maxFlow))
                            .build());
                }
            }
        }

        List<FlowEdgeDto> finalEdges = new ArrayList<>();
        for (FlowEdge e : network.getEdges()) {
            finalEdges.add(FlowEdgeDto.builder()
                    .u(e.getU())
                    .v(e.getV())
                    .uName(e.getUName())
                    .vName(e.getVName())
                    .capacity(e.getCapacity())
                    .flow(e.getFlow())
                    .residualCapacity(e.residualCapacityTo(e.getV()))
                    .build());
        }

        long endTime = System.nanoTime();

        return DinicResult.builder()
                .maxFlow(maxFlow)
                .phaseCount(phaseCounter)
                .blockingFlows(blockingFlows)
                .finalEdges(finalEdges)
                .comparisons(comparisons)
                .executionTimeNanos(endTime - startTime)
                .complexity(getComplexity())
                .trace(trace)
                .build();
    }

    private double dfsBlockingFlow(FlowNetwork network, int u, int t, double pushed, int[] level, int[] ptr, List<FlowEdge> pathEdges) {
        if (pushed <= 0 || u == t) return pushed;

        List<FlowEdge> adj = network.getAdj(u);
        for (; ptr[u] < adj.size(); ptr[u]++) {
            FlowEdge edge = adj.get(ptr[u]);
            int v = (edge.getU() == u) ? edge.getV() : edge.getU();

            if (level[v] == level[u] + 1 && edge.residualCapacityTo(v) > 0) {
                pathEdges.add(edge);
                double tr = dfsBlockingFlow(network, v, t, Math.min(pushed, edge.residualCapacityTo(v)), level, ptr, pathEdges);
                if (tr > 0) {
                    edge.addResidualFlowTo(v, tr);
                    return tr;
                }
                pathEdges.remove(pathEdges.size() - 1);
            }
        }

        return 0.0;
    }
}
