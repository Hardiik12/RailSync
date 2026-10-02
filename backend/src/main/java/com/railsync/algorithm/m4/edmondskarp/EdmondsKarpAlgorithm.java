package com.railsync.algorithm.m4.edmondskarp;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import com.railsync.algorithm.core.Algorithm;
import com.railsync.algorithm.m4.common.FlowEdge;
import com.railsync.algorithm.m4.common.FlowNetwork;
import com.railsync.algorithm.m4.common.dto.AugmentingPathDto;
import com.railsync.algorithm.m4.common.dto.EdgeInputDto;
import com.railsync.algorithm.m4.common.dto.FlowEdgeDto;
import com.railsync.algorithm.m4.edmondskarp.dto.EdmondsKarpInput;
import com.railsync.algorithm.m4.edmondskarp.dto.EdmondsKarpResult;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class EdmondsKarpAlgorithm implements Algorithm<EdmondsKarpInput, EdmondsKarpResult> {

    @Override
    public String getName() {
        return "Edmonds-Karp Algorithm (BFS Shortest Augmenting Paths)";
    }

    @Override
    public Complexity getComplexity() {
        return Complexity.builder()
                .time("O(V * E^2)")
                .space("O(V + E)")
                .build();
    }

    @Override
    public EdmondsKarpResult execute(EdmondsKarpInput input) {
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
            return EdmondsKarpResult.builder()
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
            FlowEdge[] parentEdge = new FlowEdge[n];
            Queue<Integer> queue = new LinkedList<>();
            queue.add(s);

            while (!queue.isEmpty() && parentEdge[t] == null) {
                int curr = queue.poll();
                comparisons++;

                for (FlowEdge edge : network.getAdj(curr)) {
                    int v = (edge.getU() == curr) ? edge.getV() : edge.getU();
                    if (v != s && parentEdge[v] == null && edge.residualCapacityTo(v) > 0) {
                        parentEdge[v] = edge;
                        queue.add(v);
                    }
                }
            }

            if (parentEdge[t] == null) {
                break; // Sink unreachable
            }

            // Calculate bottleneck capacity along BFS path
            double bottleneck = Double.POSITIVE_INFINITY;
            int curr = t;
            while (curr != s) {
                FlowEdge edge = parentEdge[curr];
                bottleneck = Math.min(bottleneck, edge.residualCapacityTo(curr));
                curr = (edge.getV() == curr) ? edge.getU() : edge.getV();
            }

            maxFlow += bottleneck;

            // Push bottleneck flow & reconstruct node path
            List<Integer> nodePath = new ArrayList<>();
            List<String> nodeNamePath = new ArrayList<>();

            curr = t;
            while (curr != s) {
                FlowEdge edge = parentEdge[curr];
                edge.addResidualFlowTo(curr, bottleneck);
                nodePath.add(curr);
                nodeNamePath.add(edge.getV() == curr ? edge.getVName() : edge.getUName());
                curr = (edge.getV() == curr) ? edge.getU() : edge.getV();
            }
            nodePath.add(s);
            nodeNamePath.add(network.getAdj(s).isEmpty() ? "Node " + s : (parentEdge[nodePath.get(nodePath.size() - 2)].getU() == s ? parentEdge[nodePath.get(nodePath.size() - 2)].getUName() : parentEdge[nodePath.get(nodePath.size() - 2)].getVName()));

            Collections.reverse(nodePath);
            Collections.reverse(nodeNamePath);

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
                        .action("EDMONDS_KARP_AUGMENT")
                        .state(Map.of("bottleneck", bottleneck, "maxFlow", maxFlow, "path", nodeNamePath))
                        .description(String.format("Pushed bottleneck flow %.2f along BFS path %s (Total Flow: %.2f)", bottleneck, nodeNamePath, maxFlow))
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

        return EdmondsKarpResult.builder()
                .maxFlow(maxFlow)
                .augmentingPaths(augmentingPaths)
                .finalEdges(finalEdges)
                .comparisons(comparisons)
                .executionTimeNanos(endTime - startTime)
                .complexity(getComplexity())
                .trace(trace)
                .build();
    }
}
