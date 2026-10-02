package com.railsync.algorithm.m4.fordfulkerson;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import com.railsync.algorithm.core.Algorithm;
import com.railsync.algorithm.m4.common.FlowEdge;
import com.railsync.algorithm.m4.common.FlowNetwork;
import com.railsync.algorithm.m4.common.dto.AugmentingPathDto;
import com.railsync.algorithm.m4.common.dto.EdgeInputDto;
import com.railsync.algorithm.m4.common.dto.FlowEdgeDto;
import com.railsync.algorithm.m4.fordfulkerson.dto.FordFulkersonInput;
import com.railsync.algorithm.m4.fordfulkerson.dto.FordFulkersonResult;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FordFulkersonAlgorithm implements Algorithm<FordFulkersonInput, FordFulkersonResult> {

    @Override
    public String getName() {
        return "Ford-Fulkerson Algorithm (DFS Augmenting Paths)";
    }

    @Override
    public Complexity getComplexity() {
        return Complexity.builder()
                .time("O(E * F)")
                .space("O(V + E)")
                .build();
    }

    @Override
    public FordFulkersonResult execute(FordFulkersonInput input) {
        long startTime = System.nanoTime();

        int n = input.getVertexCount() != null ? input.getVertexCount() : 0;
        int s = input.getSource() != null ? input.getSource() : 0;
        int t = input.getSink() != null ? input.getSink() : 0;
        boolean traceEnabled = Boolean.TRUE.equals(input.getTraceEnabled());
        int maxTraceSteps = input.getMaxTraceSteps() != null ? input.getMaxTraceSteps() : 500;

        List<TraceStep> trace = new ArrayList<>();
        List<AugmentingPathDto> augmentingPaths = new ArrayList<>();

        if (n <= 0 || s < 0 || s >= n || t < 0 || t >= n || s == t || input.getEdges() == null) {
            long endTime = System.nanoTime();
            return FordFulkersonResult.builder()
                    .maxFlow(0.0)
                    .augmentingPaths(Collections.emptyList())
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

        while (true) {
            boolean[] visited = new boolean[n];
            List<FlowEdge> pathEdges = new ArrayList<>();

            double bottleneck = dfsAugment(network, s, t, Double.POSITIVE_INFINITY, visited, pathEdges);
            comparisons += visited.length;

            if (bottleneck <= 0) {
                break;
            }

            maxFlow += bottleneck;

            // Push bottleneck flow along edges
            List<Integer> nodePath = new ArrayList<>();
            List<String> nodeNamePath = new ArrayList<>();
            nodePath.add(s);
            nodeNamePath.add(network.getAdj(s).isEmpty() ? "Node " + s : (pathEdges.get(0).getU() == s ? pathEdges.get(0).getUName() : pathEdges.get(0).getVName()));

            int curr = s;
            for (FlowEdge edge : pathEdges) {
                edge.addResidualFlowTo(edge.getU() == curr ? edge.getV() : edge.getU(), bottleneck);
                curr = (edge.getU() == curr) ? edge.getV() : edge.getU();
                nodePath.add(curr);
                nodeNamePath.add(edge.getV() == curr ? edge.getVName() : edge.getUName());
            }

            AugmentingPathDto pathDto = AugmentingPathDto.builder()
                    .nodePath(nodePath)
                    .nodeNamePath(nodeNamePath)
                    .bottleneckCapacity(bottleneck)
                    .updatedMaxFlow(maxFlow)
                    .build();
            augmentingPaths.add(pathDto);

            if (traceEnabled && trace.size() < maxTraceSteps) {
                trace.add(TraceStep.builder()
                        .step(stepCounter++)
                        .action("FORD_FULKERSON_AUGMENT")
                        .state(Map.of("bottleneck", bottleneck, "maxFlow", maxFlow, "path", nodeNamePath))
                        .description(String.format("Pushed bottleneck flow %.2f along DFS path %s (Total Flow: %.2f)", bottleneck, nodeNamePath, maxFlow))
                        .build());
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

        return FordFulkersonResult.builder()
                .maxFlow(maxFlow)
                .augmentingPaths(augmentingPaths)
                .finalEdges(finalEdges)
                .comparisons(comparisons)
                .executionTimeNanos(endTime - startTime)
                .complexity(getComplexity())
                .trace(trace)
                .build();
    }

    private double dfsAugment(FlowNetwork network, int u, int t, double currentFlow, boolean[] visited, List<FlowEdge> pathEdges) {
        if (u == t) return currentFlow;
        visited[u] = true;

        for (FlowEdge edge : network.getAdj(u)) {
            int v = (edge.getU() == u) ? edge.getV() : edge.getU();
            double residual = edge.residualCapacityTo(v);

            if (!visited[v] && residual > 0) {
                pathEdges.add(edge);
                double bottleneck = dfsAugment(network, v, t, Math.min(currentFlow, residual), visited, pathEdges);
                if (bottleneck > 0) {
                    return bottleneck;
                }
                pathEdges.remove(pathEdges.size() - 1);
            }
        }

        return 0.0;
    }
}
