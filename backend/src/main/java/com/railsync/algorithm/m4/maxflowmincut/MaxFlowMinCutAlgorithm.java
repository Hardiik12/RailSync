package com.railsync.algorithm.m4.maxflowmincut;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import com.railsync.algorithm.core.Algorithm;
import com.railsync.algorithm.m4.common.FlowEdge;
import com.railsync.algorithm.m4.common.FlowNetwork;
import com.railsync.algorithm.m4.common.dto.EdgeInputDto;
import com.railsync.algorithm.m4.edmondskarp.EdmondsKarpAlgorithm;
import com.railsync.algorithm.m4.edmondskarp.dto.EdmondsKarpInput;
import com.railsync.algorithm.m4.edmondskarp.dto.EdmondsKarpResult;
import com.railsync.algorithm.m4.maxflowmincut.dto.CutEdgeDto;
import com.railsync.algorithm.m4.maxflowmincut.dto.MaxFlowMinCutInput;
import com.railsync.algorithm.m4.maxflowmincut.dto.MaxFlowMinCutResult;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MaxFlowMinCutAlgorithm implements Algorithm<MaxFlowMinCutInput, MaxFlowMinCutResult> {

    @Override
    public String getName() {
        return "Max-Flow Min-Cut Theorem Analysis";
    }

    @Override
    public Complexity getComplexity() {
        return Complexity.builder()
                .time("O(V * E^2)")
                .space("O(V + E)")
                .build();
    }

    @Override
    public MaxFlowMinCutResult execute(MaxFlowMinCutInput input) {
        long startTime = System.nanoTime();

        int n = input.getVertexCount() != null ? input.getVertexCount() : 0;
        int s = input.getSource() != null ? input.getSource() : 0;
        int t = input.getSink() != null ? input.getSink() : 0;
        boolean traceEnabled = Boolean.TRUE.equals(input.getTraceEnabled());
        int maxTraceSteps = input.getMaxTraceSteps() != null ? input.getMaxTraceSteps() : 500;

        List<TraceStep> trace = new ArrayList<>();

        if (n <= 0 || s < 0 || s >= n || t < 0 || t >= n || s == t || input.getEdges() == null) {
            long endTime = System.nanoTime();
            return MaxFlowMinCutResult.builder()
                    .maxFlow(0.0)
                    .minCutCapacity(0.0)
                    .valuesEqual(true)
                    .sourceCutVertices(Collections.emptyList())
                    .sinkCutVertices(Collections.emptyList())
                    .cutEdges(Collections.emptyList())
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

        // Run Edmonds-Karp to compute max flow & final residual graph
        double maxFlow = 0.0;
        long comparisons = 0;

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

            if (parentEdge[t] == null) break;

            double bottleneck = Double.POSITIVE_INFINITY;
            int curr = t;
            while (curr != s) {
                FlowEdge edge = parentEdge[curr];
                bottleneck = Math.min(bottleneck, edge.residualCapacityTo(curr));
                curr = (edge.getV() == curr) ? edge.getU() : edge.getV();
            }

            maxFlow += bottleneck;

            curr = t;
            while (curr != s) {
                FlowEdge edge = parentEdge[curr];
                edge.addResidualFlowTo(curr, bottleneck);
                curr = (edge.getV() == curr) ? edge.getU() : edge.getV();
            }
        }

        // BFS in residual graph to find source-reachable cut set
        boolean[] reachable = new boolean[n];
        Queue<Integer> resQueue = new LinkedList<>();
        reachable[s] = true;
        resQueue.add(s);

        int stepCounter = 1;
        while (!resQueue.isEmpty()) {
            int u = resQueue.poll();
            comparisons++;

            for (FlowEdge edge : network.getAdj(u)) {
                int v = (edge.getU() == u) ? edge.getV() : edge.getU();
                if (!reachable[v] && edge.residualCapacityTo(v) > 0) {
                    reachable[v] = true;
                    resQueue.add(v);

                    if (traceEnabled && trace.size() < maxTraceSteps) {
                        trace.add(TraceStep.builder()
                                .step(stepCounter++)
                                .action("MIN_CUT_RESIDUAL_REACH")
                                .state(Map.of("node", v, "name", edge.getVName()))
                                .description(String.format("Node '%s' (%d) reachable from source in residual network", edge.getVName(), v))
                                .build());
                    }
                }
            }
        }

        List<String> sourceCutVertices = new ArrayList<>();
        List<String> sinkCutVertices = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String label = network.getAdj(i).isEmpty() ? "Node " + i : network.getAdj(i).get(0).getUName();
            if (reachable[i]) {
                sourceCutVertices.add(label);
            } else {
                sinkCutVertices.add(label);
            }
        }

        List<CutEdgeDto> cutEdges = new ArrayList<>();
        double minCutCapacity = 0.0;

        for (FlowEdge edge : network.getEdges()) {
            if (reachable[edge.getU()] && !reachable[edge.getV()]) {
                minCutCapacity += edge.getCapacity();
                cutEdges.add(CutEdgeDto.builder()
                        .u(edge.getU())
                        .v(edge.getV())
                        .uName(edge.getUName())
                        .vName(edge.getVName())
                        .capacity(edge.getCapacity())
                        .flow(edge.getFlow())
                        .build());

                if (traceEnabled && trace.size() < maxTraceSteps) {
                    trace.add(TraceStep.builder()
                            .step(stepCounter++)
                            .action("MIN_CUT_EDGE")
                            .state(Map.of("u", edge.getUName(), "v", edge.getVName(), "capacity", edge.getCapacity()))
                            .description(String.format("Identified bottleneck cut edge '%s' -> '%s' (Capacity: %.2f)", edge.getUName(), edge.getVName(), edge.getCapacity()))
                            .build());
                }
            }
        }

        boolean valuesEqual = (Math.abs(maxFlow - minCutCapacity) < 1e-6);
        long endTime = System.nanoTime();

        return MaxFlowMinCutResult.builder()
                .maxFlow(maxFlow)
                .minCutCapacity(minCutCapacity)
                .valuesEqual(valuesEqual)
                .sourceCutVertices(sourceCutVertices)
                .sinkCutVertices(sinkCutVertices)
                .cutEdges(cutEdges)
                .comparisons(comparisons)
                .executionTimeNanos(endTime - startTime)
                .complexity(getComplexity())
                .trace(trace)
                .build();
    }
}
