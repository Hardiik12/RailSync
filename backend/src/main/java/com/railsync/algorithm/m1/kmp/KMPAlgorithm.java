package com.railsync.algorithm.m1.kmp;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import com.railsync.algorithm.core.Algorithm;
import com.railsync.algorithm.m1.kmp.dto.KMPInput;
import com.railsync.algorithm.m1.kmp.dto.KMPResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class KMPAlgorithm implements Algorithm<KMPInput, KMPResult> {

    @Override
    public String getName() {
        return "Knuth-Morris-Pratt (KMP)";
    }

    @Override
    public Complexity getComplexity() {
        return Complexity.builder()
                .time("O(n + m)")
                .space("O(m)")
                .build();
    }

    @Override
    public KMPResult execute(KMPInput input) {
        long startTime = System.nanoTime();

        String text = input.getText();
        String pattern = input.getPattern();
        boolean traceEnabled = Boolean.TRUE.equals(input.getTraceEnabled());
        int maxTraceSteps = input.getMaxTraceSteps() != null ? input.getMaxTraceSteps() : 500;

        List<TraceStep> trace = new ArrayList<>();
        List<Integer> matches = new ArrayList<>();

        if (text == null || pattern == null || pattern.isEmpty() || text.length() < pattern.length()) {
            long endTime = System.nanoTime();
            return KMPResult.builder()
                    .matches(matches)
                    .matchCount(0)
                    .lps(new int[0])
                    .comparisons(0)
                    .executionTimeNanos(endTime - startTime)
                    .complexity(getComplexity())
                    .trace(trace)
                    .build();
        }

        int[] lps = computeLPSArray(pattern, trace, traceEnabled, maxTraceSteps);

        int n = text.length();
        int m = pattern.length();
        int i = 0; // index for text
        int j = 0; // index for pattern
        long comparisons = 0;
        int stepCounter = trace.size() + 1;

        while (i < n) {
            comparisons++;
            boolean isCharMatch = text.charAt(i) == pattern.charAt(j);

            if (traceEnabled && trace.size() < maxTraceSteps) {
                trace.add(TraceStep.builder()
                        .step(stepCounter++)
                        .action(isCharMatch ? "CHARACTER_MATCH" : "CHARACTER_MISMATCH")
                        .state(Map.of(
                                "textIndex", i,
                                "patternIndex", j,
                                "textChar", String.valueOf(text.charAt(i)),
                                "patternChar", String.valueOf(pattern.charAt(j))
                        ))
                        .description(String.format("Comparing text[%d] '%c' with pattern[%d] '%c': %s",
                                i, text.charAt(i), j, pattern.charAt(j), isCharMatch ? "Match" : "Mismatch"))
                        .build());
            }

            if (isCharMatch) {
                i++;
                j++;
            }

            if (j == m) {
                int matchIndex = i - j;
                matches.add(matchIndex);

                if (traceEnabled && trace.size() < maxTraceSteps) {
                    trace.add(TraceStep.builder()
                            .step(stepCounter++)
                            .action("PATTERN_FOUND")
                            .state(Map.of(
                                    "matchIndex", matchIndex,
                                    "textIndex", i,
                                    "patternIndex", j
                            ))
                            .description(String.format("Pattern match found at text index %d", matchIndex))
                            .build());
                }
                j = lps[j - 1];
            } else if (i < n && text.charAt(i) != pattern.charAt(j)) {
                if (j != 0) {
                    int prevJ = j;
                    j = lps[j - 1];
                    if (traceEnabled && trace.size() < maxTraceSteps) {
                        trace.add(TraceStep.builder()
                                .step(stepCounter++)
                                .action("LPS_FALLBACK")
                                .state(Map.of(
                                        "textIndex", i,
                                        "oldPatternIndex", prevJ,
                                        "newPatternIndex", j
                                ))
                                .description(String.format("LPS fallback: shifted pattern index from %d to %d using LPS[%d]=%d",
                                        prevJ, j, prevJ - 1, j))
                                .build());
                    }
                } else {
                    i++;
                }
            }
        }

        long endTime = System.nanoTime();

        return KMPResult.builder()
                .matches(matches)
                .matchCount(matches.size())
                .lps(lps)
                .comparisons(comparisons)
                .executionTimeNanos(endTime - startTime)
                .complexity(getComplexity())
                .trace(trace)
                .build();
    }

    public int[] computeLPSArray(String pattern, List<TraceStep> trace, boolean traceEnabled, int maxTraceSteps) {
        int m = pattern.length();
        int[] lps = new int[m];
        int len = 0;
        int i = 1;
        lps[0] = 0;
        int stepCounter = 1;

        if (traceEnabled && trace.size() < maxTraceSteps) {
            trace.add(TraceStep.builder()
                    .step(stepCounter++)
                    .action("LPS_INIT")
                    .state(Map.of("patternLength", m))
                    .description("Initializing LPS (Longest Prefix Suffix) table construction")
                    .build());
        }

        while (i < m) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;

                if (traceEnabled && trace.size() < maxTraceSteps) {
                    trace.add(TraceStep.builder()
                            .step(stepCounter++)
                            .action("LPS_BUILD_MATCH")
                            .state(Map.of("i", i, "len", len, "lpsVal", len))
                            .description(String.format("LPS build match pattern[%d] == pattern[%d]: LPS[%d] = %d", i, len - 1, i, len))
                            .build());
                }
                i++;
            } else {
                if (len != 0) {
                    int prevLen = len;
                    len = lps[len - 1];
                    if (traceEnabled && trace.size() < maxTraceSteps) {
                        trace.add(TraceStep.builder()
                                .step(stepCounter++)
                                .action("LPS_BUILD_FALLBACK")
                                .state(Map.of("i", i, "oldLen", prevLen, "newLen", len))
                                .description(String.format("LPS build fallback at index %d: len updated to %d", i, len))
                                .build());
                    }
                } else {
                    lps[i] = 0;
                    if (traceEnabled && trace.size() < maxTraceSteps) {
                        trace.add(TraceStep.builder()
                                .step(stepCounter++)
                                .action("LPS_BUILD_ZERO")
                                .state(Map.of("i", i, "lpsVal", 0))
                                .description(String.format("No prefix match at index %d: LPS[%d] = 0", i, i))
                                .build());
                    }
                    i++;
                }
            }
        }

        return lps;
    }
}
