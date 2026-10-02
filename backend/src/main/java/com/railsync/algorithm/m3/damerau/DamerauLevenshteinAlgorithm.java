package com.railsync.algorithm.m3.damerau;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import com.railsync.algorithm.core.Algorithm;
import com.railsync.algorithm.m3.damerau.dto.DamerauLevenshteinInput;
import com.railsync.algorithm.m3.damerau.dto.DamerauLevenshteinResult;
import com.railsync.algorithm.m3.levenshtein.dto.EditOperationDto;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DamerauLevenshteinAlgorithm implements Algorithm<DamerauLevenshteinInput, DamerauLevenshteinResult> {

    @Override
    public String getName() {
        return "Damerau-Levenshtein (Restricted Adjacent Transposition)";
    }

    @Override
    public Complexity getComplexity() {
        return Complexity.builder()
                .time("O(n * m)")
                .space("O(n * m)")
                .build();
    }

    @Override
    public DamerauLevenshteinResult execute(DamerauLevenshteinInput input) {
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

        for (int i = 0; i <= n; i++) dp[i][0] = i;
        for (int j = 0; j <= m; j++) dp[0][j] = j;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                comparisons++;
                boolean isMatch = source.charAt(i - 1) == target.charAt(j - 1);
                int cost = isMatch ? 0 : 1;

                int delCost = dp[i - 1][j] + 1;
                int insCost = dp[i][j - 1] + 1;
                int subCost = dp[i - 1][j - 1] + cost;

                dp[i][j] = Math.min(delCost, Math.min(insCost, subCost));

                // Check adjacent transposition
                if (i > 1 && j > 1 && source.charAt(i - 1) == target.charAt(j - 2) && source.charAt(i - 2) == target.charAt(j - 1)) {
                    int transCost = dp[i - 2][j - 2] + 1;
                    if (transCost < dp[i][j]) {
                        dp[i][j] = transCost;
                        if (traceEnabled && trace.size() < maxTraceSteps) {
                            trace.add(TraceStep.builder()
                                    .step(stepCounter++)
                                    .action("DAMERAU_TRANSPOSITION")
                                    .state(Map.of("i", i, "j", j, "val", dp[i][j]))
                                    .description(String.format("Applied adjacent transposition for '%c%c' <-> '%c%c' at [%d,%d]",
                                            source.charAt(i - 2), source.charAt(i - 1), target.charAt(j - 2), target.charAt(j - 1), i, j))
                                    .build());
                        }
                    }
                }

                if (traceEnabled && trace.size() < maxTraceSteps) {
                    trace.add(TraceStep.builder()
                            .step(stepCounter++)
                            .action("DP_CELL_UPDATE")
                            .state(Map.of("i", i, "j", j, "val", dp[i][j]))
                            .description(String.format("Updated dp[%d][%d] = %d", i, j, dp[i][j]))
                            .build());
                }
            }
        }

        // Backtrack path & count transpositions
        List<EditOperationDto> operations = reconstructPath(source, target, dp);
        int transCount = (int) operations.stream().filter(op -> "TRANSPOSITION".equals(op.getType())).count();

        long endTime = System.nanoTime();

        return DamerauLevenshteinResult.builder()
                .source(source)
                .target(target)
                .distance(dp[n][m])
                .dpMatrix(dp)
                .editOperations(operations)
                .transpositionCount(transCount)
                .comparisons(comparisons)
                .executionTimeNanos(endTime - startTime)
                .complexity(getComplexity())
                .trace(trace)
                .build();
    }

    private List<EditOperationDto> reconstructPath(String source, String target, int[][] dp) {
        List<EditOperationDto> ops = new ArrayList<>();
        int i = source.length();
        int j = target.length();

        while (i > 0 || j > 0) {
            // Check transposition condition first
            if (i > 1 && j > 1 && source.charAt(i - 1) == target.charAt(j - 2) && source.charAt(i - 2) == target.charAt(j - 1)
                    && dp[i][j] == dp[i - 2][j - 2] + 1) {
                ops.add(EditOperationDto.builder()
                        .type("TRANSPOSITION")
                        .sourceChar(source.charAt(i - 1))
                        .targetChar(target.charAt(j - 1))
                        .sourceIndex(i - 1)
                        .targetIndex(j - 1)
                        .description(String.format("Transpose adjacent chars '%c%c' <-> '%c%c'",
                                source.charAt(i - 2), source.charAt(i - 1), target.charAt(j - 2), target.charAt(j - 1)))
                        .build());
                i -= 2;
                j -= 2;
            } else if (i > 0 && j > 0 && source.charAt(i - 1) == target.charAt(j - 1) && dp[i][j] == dp[i - 1][j - 1]) {
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
