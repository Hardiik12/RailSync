package com.railsync.algorithm.m3.levenshtein;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import com.railsync.algorithm.core.Algorithm;
import com.railsync.algorithm.m3.levenshtein.dto.EditOperationDto;
import com.railsync.algorithm.m3.levenshtein.dto.LevenshteinInput;
import com.railsync.algorithm.m3.levenshtein.dto.LevenshteinResult;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class LevenshteinAlgorithm implements Algorithm<LevenshteinInput, LevenshteinResult> {

    @Override
    public String getName() {
        return "Levenshtein Distance Algorithm";
    }

    @Override
    public Complexity getComplexity() {
        return Complexity.builder()
                .time("O(n * m)")
                .space("O(n * m)")
                .build();
    }

    @Override
    public LevenshteinResult execute(LevenshteinInput input) {
        long startTime = System.nanoTime();

        String source = input.getSource() != null ? input.getSource() : "";
        String target = input.getTarget() != null ? input.getTarget() : "";
        boolean traceEnabled = Boolean.TRUE.equals(input.getTraceEnabled());
        int maxTraceSteps = input.getMaxTraceSteps() != null ? input.getMaxTraceSteps() : 500;

        List<TraceStep> trace = new ArrayList<>();
        int n = source.length();
        int m = target.length();

        int[][] dp = new int[n + 1][m + 1];
        long comparisons = 0;
        int stepCounter = 1;

        // Initialize base cases
        for (int i = 0; i <= n; i++) dp[i][0] = i;
        for (int j = 0; j <= m; j++) dp[0][j] = j;

        if (traceEnabled && trace.size() < maxTraceSteps) {
            trace.add(TraceStep.builder()
                    .step(stepCounter++)
                    .action("DP_INIT")
                    .state(Map.of("sourceLen", n, "targetLen", m))
                    .description(String.format("Initialized Levenshtein DP matrix of size %dx%d", n + 1, m + 1))
                    .build());
        }

        // Fill DP Matrix
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                comparisons++;
                if (source.charAt(i - 1) == target.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    int delCost = dp[i - 1][j] + 1;
                    int insCost = dp[i][j - 1] + 1;
                    int subCost = dp[i - 1][j - 1] + 1;
                    dp[i][j] = Math.min(delCost, Math.min(insCost, subCost));
                }

                if (traceEnabled && trace.size() < maxTraceSteps) {
                    trace.add(TraceStep.builder()
                            .step(stepCounter++)
                            .action("DP_CELL_UPDATE")
                            .state(Map.of("i", i, "j", j, "val", dp[i][j], "srcChar", String.valueOf(source.charAt(i - 1)), "tgtChar", String.valueOf(target.charAt(j - 1))))
                            .description(String.format("Updated dp[%d][%d] = %d (comparing '%c' vs '%c')", i, j, dp[i][j], source.charAt(i - 1), target.charAt(j - 1)))
                            .build());
                }
            }
        }

        // Backtrack edit sequence
        List<EditOperationDto> operations = reconstructEditPath(source, target, dp);

        long endTime = System.nanoTime();

        return LevenshteinResult.builder()
                .source(source)
                .target(target)
                .distance(dp[n][m])
                .dpMatrix(dp)
                .editOperations(operations)
                .comparisons(comparisons)
                .executionTimeNanos(endTime - startTime)
                .complexity(getComplexity())
                .trace(trace)
                .build();
    }

    private List<EditOperationDto> reconstructEditPath(String source, String target, int[][] dp) {
        List<EditOperationDto> ops = new ArrayList<>();
        int i = source.length();
        int j = target.length();

        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && source.charAt(i - 1) == target.charAt(j - 1) && dp[i][j] == dp[i - 1][j - 1]) {
                ops.add(EditOperationDto.builder()
                        .type("KEEP")
                        .sourceChar(source.charAt(i - 1))
                        .targetChar(target.charAt(j - 1))
                        .sourceIndex(i - 1)
                        .targetIndex(j - 1)
                        .description(String.format("Keep '%c'", source.charAt(i - 1)))
                        .build());
                i--;
                j--;
            } else if (i > 0 && j > 0 && dp[i][j] == dp[i - 1][j - 1] + 1) {
                ops.add(EditOperationDto.builder()
                        .type("SUBSTITUTE")
                        .sourceChar(source.charAt(i - 1))
                        .targetChar(target.charAt(j - 1))
                        .sourceIndex(i - 1)
                        .targetIndex(j - 1)
                        .description(String.format("Substitute '%c' -> '%c'", source.charAt(i - 1), target.charAt(j - 1)))
                        .build());
                i--;
                j--;
            } else if (i > 0 && dp[i][j] == dp[i - 1][j] + 1) {
                ops.add(EditOperationDto.builder()
                        .type("DELETE")
                        .sourceChar(source.charAt(i - 1))
                        .targetChar(null)
                        .sourceIndex(i - 1)
                        .targetIndex(-1)
                        .description(String.format("Delete '%c'", source.charAt(i - 1)))
                        .build());
                i--;
            } else if (j > 0 && dp[i][j] == dp[i][j - 1] + 1) {
                ops.add(EditOperationDto.builder()
                        .type("INSERT")
                        .sourceChar(null)
                        .targetChar(target.charAt(j - 1))
                        .sourceIndex(-1)
                        .targetIndex(j - 1)
                        .description(String.format("Insert '%c'", target.charAt(j - 1)))
                        .build());
                j--;
            }
        }

        Collections.reverse(ops);
        return ops;
    }
}
