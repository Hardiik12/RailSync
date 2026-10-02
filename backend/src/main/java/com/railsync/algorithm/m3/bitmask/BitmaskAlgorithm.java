package com.railsync.algorithm.m3.bitmask;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import com.railsync.algorithm.core.Algorithm;
import com.railsync.algorithm.m3.bitmask.dto.BitmaskInput;
import com.railsync.algorithm.m3.bitmask.dto.BitmaskResult;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class BitmaskAlgorithm implements Algorithm<BitmaskInput, BitmaskResult> {

    private static final double INF = 1e9;

    @Override
    public String getName() {
        return "Bitmask DP (Minimum Hamiltonian Route)";
    }

    @Override
    public Complexity getComplexity() {
        return Complexity.builder()
                .time("O(2^n * n^2)")
                .space("O(2^n * n)")
                .build();
    }

    @Override
    public BitmaskResult execute(BitmaskInput input) {
        long startTime = System.nanoTime();

        int n = input.getNodeCount() != null ? input.getNodeCount() : 0;
        double[][] cost = input.getCostMatrix();
        int startNode = input.getStartNode() != null ? input.getStartNode() : 0;
        boolean traceEnabled = Boolean.TRUE.equals(input.getTraceEnabled());
        int maxTraceSteps = input.getMaxTraceSteps() != null ? input.getMaxTraceSteps() : 500;

        List<TraceStep> trace = new ArrayList<>();

        if (n <= 0 || startNode < 0 || startNode >= n || cost == null || cost.length < n) {
            long endTime = System.nanoTime();
            return BitmaskResult.builder()
                    .nodeCount(0)
                    .optimalCost(-1.0)
                    .pathSequence(Collections.emptyList())
                    .dpStates(Collections.emptyMap())
                    .stateCount(0)
                    .comparisons(0)
                    .executionTimeNanos(endTime - startTime)
                    .complexity(getComplexity())
                    .trace(trace)
                    .build();
        }

        for (int i = 0; i < n; i++) {
            if (cost[i] == null || cost[i].length < n) {
                long endTime = System.nanoTime();
                return BitmaskResult.builder()
                        .nodeCount(0)
                        .optimalCost(-1.0)
                        .pathSequence(Collections.emptyList())
                        .dpStates(Collections.emptyMap())
                        .stateCount(0)
                        .comparisons(0)
                        .executionTimeNanos(endTime - startTime)
                        .complexity(getComplexity())
                        .trace(trace)
                        .build();
            }
        }

        int fullMask = (1 << n) - 1;
        double[][] dp = new double[1 << n][n];
        int[][] parent = new int[1 << n][n];

        for (int i = 0; i < (1 << n); i++) {
            Arrays.fill(dp[i], INF);
            Arrays.fill(parent[i], -1);
        }

        int startMask = (1 << startNode);
        dp[startMask][startNode] = 0.0;

        long comparisons = 0;
        int stepCounter = 1;
        Map<String, Double> stateMap = new HashMap<>();

        for (int mask = 1; mask <= fullMask; mask++) {
            for (int u = 0; u < n; u++) {
                if ((mask & (1 << u)) != 0 && dp[mask][u] < INF) {
                    stateMap.put(String.format("mask:%s,last:%d", Integer.toBinaryString(mask), u), dp[mask][u]);

                    for (int v = 0; v < n; v++) {
                        if ((mask & (1 << v)) == 0 && cost[u][v] >= 0) {
                            comparisons++;
                            int nextMask = mask | (1 << v);
                            double newCost = dp[mask][u] + cost[u][v];

                            if (newCost < dp[nextMask][v]) {
                                dp[nextMask][v] = newCost;
                                parent[nextMask][v] = u;

                                if (traceEnabled && trace.size() < maxTraceSteps) {
                                    trace.add(TraceStep.builder()
                                            .step(stepCounter++)
                                            .action("BITMASK_TRANSITION")
                                            .state(Map.of("mask", Integer.toBinaryString(nextMask), "u", u, "v", v, "cost", newCost))
                                            .description(String.format("Transitioned node %d -> %d: new mask %s with cost %.2f", u, v, Integer.toBinaryString(nextMask), newCost))
                                            .build());
                                }
                            }
                        }
                    }
                }
            }
        }

        // Find optimal end node covering all nodes
        double bestCost = INF;
        int bestLast = -1;

        for (int u = 0; u < n; u++) {
            if (dp[fullMask][u] < bestCost) {
                bestCost = dp[fullMask][u];
                bestLast = u;
            }
        }

        // Reconstruct path sequence
        List<Integer> path = new ArrayList<>();
        if (bestLast != -1) {
            int currMask = fullMask;
            int currNode = bestLast;

            while (currNode != -1) {
                path.add(currNode);
                int prevNode = parent[currMask][currNode];
                currMask = currMask ^ (1 << currNode);
                currNode = prevNode;
            }
            Collections.reverse(path);
        }

        long endTime = System.nanoTime();

        return BitmaskResult.builder()
                .nodeCount(n)
                .optimalCost(bestCost < INF ? bestCost : -1.0)
                .pathSequence(path)
                .dpStates(stateMap)
                .stateCount(stateMap.size())
                .comparisons(comparisons)
                .executionTimeNanos(endTime - startTime)
                .complexity(getComplexity())
                .trace(trace)
                .build();
    }
}
