package com.railsync.algorithm.m3.matrixchain;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import com.railsync.algorithm.core.Algorithm;
import com.railsync.algorithm.m3.matrixchain.dto.MatrixChainInput;
import com.railsync.algorithm.m3.matrixchain.dto.MatrixChainResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class MatrixChainAlgorithm implements Algorithm<MatrixChainInput, MatrixChainResult> {

    @Override
    public String getName() {
        return "Matrix-Chain Multiplication DP";
    }

    @Override
    public Complexity getComplexity() {
        return Complexity.builder()
                .time("O(n^3)")
                .space("O(n^2)")
                .build();
    }

    @Override
    public MatrixChainResult execute(MatrixChainInput input) {
        long startTime = System.nanoTime();

        int[] p = input.getDimensions();
        boolean traceEnabled = Boolean.TRUE.equals(input.getTraceEnabled());
        int maxTraceSteps = input.getMaxTraceSteps() != null ? input.getMaxTraceSteps() : 500;

        List<TraceStep> trace = new ArrayList<>();

        if (p == null || p.length < 2) {
            long endTime = System.nanoTime();
            return MatrixChainResult.builder()
                    .matrixCount(0)
                    .dimensions(new int[0])
                    .minScalarMultiplications(0)
                    .dpTable(new long[0][0])
                    .splitTable(new int[0][0])
                    .optimalParenthesization("")
                    .operationCount(0)
                    .executionTimeNanos(endTime - startTime)
                    .complexity(getComplexity())
                    .trace(trace)
                    .build();
        }

        int n = p.length - 1; // Number of matrices
        long[][] dp = new long[n + 1][n + 1];
        int[][] split = new int[n + 1][n + 1];

        long operations = 0;
        int stepCounter = 1;

        // Chain length len from 2 to n
        for (int len = 2; len <= n; len++) {
            for (int i = 1; i <= n - len + 1; i++) {
                int j = i + len - 1;
                dp[i][j] = Long.MAX_VALUE;

                for (int k = i; k <= j - 1; k++) {
                    operations++;
                    long cost = dp[i][k] + dp[k + 1][j] + (long) p[i - 1] * p[k] * p[j];

                    if (cost < dp[i][j]) {
                        dp[i][j] = cost;
                        split[i][j] = k;

                        if (traceEnabled && trace.size() < maxTraceSteps) {
                            trace.add(TraceStep.builder()
                                    .step(stepCounter++)
                                    .action("MATRIX_CHAIN_SPLIT_UPDATE")
                                    .state(Map.of("i", i, "j", j, "k", k, "cost", cost))
                                    .description(String.format("Optimal split for chain [%d..%d] updated at k=%d: cost=%d", i, j, k, cost))
                                    .build());
                        }
                    }
                }
            }
        }

        String parenStr = buildParenthesization(split, 1, n);

        long endTime = System.nanoTime();

        return MatrixChainResult.builder()
                .matrixCount(n)
                .dimensions(p)
                .minScalarMultiplications(dp[1][n])
                .dpTable(dp)
                .splitTable(split)
                .optimalParenthesization(parenStr)
                .operationCount(operations)
                .executionTimeNanos(endTime - startTime)
                .complexity(getComplexity())
                .trace(trace)
                .build();
    }

    private String buildParenthesization(int[][] split, int i, int j) {
        if (i == j) {
            return "A" + i;
        }
        int k = split[i][j];
        return "(" + buildParenthesization(split, i, k) + " x " + buildParenthesization(split, k + 1, j) + ")";
    }
}
