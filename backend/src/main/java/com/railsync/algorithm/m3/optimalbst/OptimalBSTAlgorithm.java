package com.railsync.algorithm.m3.optimalbst;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import com.railsync.algorithm.core.Algorithm;
import com.railsync.algorithm.m3.optimalbst.dto.BSTNodeDto;
import com.railsync.algorithm.m3.optimalbst.dto.OptimalBSTInput;
import com.railsync.algorithm.m3.optimalbst.dto.OptimalBSTResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class OptimalBSTAlgorithm implements Algorithm<OptimalBSTInput, OptimalBSTResult> {

    @Override
    public String getName() {
        return "Optimal Binary Search Tree DP";
    }

    @Override
    public Complexity getComplexity() {
        return Complexity.builder()
                .time("O(n^3)")
                .space("O(n^2)")
                .build();
    }

    @Override
    public OptimalBSTResult execute(OptimalBSTInput input) {
        long startTime = System.nanoTime();

        String[] keys = input.getKeys();
        double[] freqs = input.getFrequencies();
        boolean traceEnabled = Boolean.TRUE.equals(input.getTraceEnabled());
        int maxTraceSteps = input.getMaxTraceSteps() != null ? input.getMaxTraceSteps() : 500;

        List<TraceStep> trace = new ArrayList<>();

        if (keys == null || freqs == null || keys.length == 0 || keys.length != freqs.length) {
            long endTime = System.nanoTime();
            return OptimalBSTResult.builder()
                    .keyCount(0)
                    .keys(new String[0])
                    .frequencies(new double[0])
                    .minCost(0.0)
                    .dpTable(new double[0][0])
                    .rootTable(new int[0][0])
                    .rootNode(null)
                    .operationCount(0)
                    .executionTimeNanos(endTime - startTime)
                    .complexity(getComplexity())
                    .trace(trace)
                    .build();
        }

        int n = keys.length;
        double[][] cost = new double[n + 2][n + 2];
        int[][] root = new int[n + 2][n + 2];

        // Base case: length 1
        for (int i = 1; i <= n; i++) {
            cost[i][i] = freqs[i - 1];
            root[i][i] = i;
        }

        long operations = 0;
        int stepCounter = 1;

        // Length 2 to n
        for (int len = 2; len <= n; len++) {
            for (int i = 1; i <= n - len + 1; i++) {
                int j = i + len - 1;
                cost[i][j] = Double.MAX_VALUE;

                double weightSum = 0;
                for (int m = i; m <= j; m++) weightSum += freqs[m - 1];

                for (int r = i; r <= j; r++) {
                    operations++;
                    double c = (r > i ? cost[i][r - 1] : 0) + (r < j ? cost[r + 1][j] : 0) + weightSum;

                    if (c < cost[i][j]) {
                        cost[i][j] = c;
                        root[i][j] = r;

                        if (traceEnabled && trace.size() < maxTraceSteps) {
                            trace.add(TraceStep.builder()
                                    .step(stepCounter++)
                                    .action("OBST_ROOT_UPDATE")
                                    .state(Map.of("i", i, "j", j, "root", r, "cost", c))
                                    .description(String.format("Updated optimal root for keys [%d..%d] at root r=%d ('%s'): cost=%.2f",
                                            i, j, r, keys[r - 1], c))
                                    .build());
                        }
                    }
                }
            }
        }

        BSTNodeDto rootTree = constructTree(root, keys, freqs, 1, n);

        long endTime = System.nanoTime();

        return OptimalBSTResult.builder()
                .keyCount(n)
                .keys(keys)
                .frequencies(freqs)
                .minCost(cost[1][n])
                .dpTable(cost)
                .rootTable(root)
                .rootNode(rootTree)
                .operationCount(operations)
                .executionTimeNanos(endTime - startTime)
                .complexity(getComplexity())
                .trace(trace)
                .build();
    }

    private BSTNodeDto constructTree(int[][] root, String[] keys, double[] freqs, int i, int j) {
        if (i > j) return null;
        int r = root[i][j];
        if (r == 0) return null;

        return BSTNodeDto.builder()
                .key(keys[r - 1])
                .frequency(freqs[r - 1])
                .left(constructTree(root, keys, freqs, i, r - 1))
                .right(constructTree(root, keys, freqs, r + 1, j))
                .build();
    }
}
