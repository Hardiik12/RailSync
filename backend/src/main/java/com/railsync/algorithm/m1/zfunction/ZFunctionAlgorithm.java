package com.railsync.algorithm.m1.zfunction;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import com.railsync.algorithm.core.Algorithm;
import com.railsync.algorithm.m1.zfunction.dto.ZFunctionInput;
import com.railsync.algorithm.m1.zfunction.dto.ZFunctionResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ZFunctionAlgorithm implements Algorithm<ZFunctionInput, ZFunctionResult> {

    @Override
    public String getName() {
        return "Z-Function Algorithm";
    }

    @Override
    public Complexity getComplexity() {
        return Complexity.builder()
                .time("O(n)")
                .space("O(n)")
                .build();
    }

    @Override
    public ZFunctionResult execute(ZFunctionInput input) {
        long startTime = System.nanoTime();

        String text = input.getText() != null ? input.getText() : "";
        String pattern = input.getPattern();
        boolean traceEnabled = Boolean.TRUE.equals(input.getTraceEnabled());
        int maxTraceSteps = input.getMaxTraceSteps() != null ? input.getMaxTraceSteps() : 500;

        List<TraceStep> trace = new ArrayList<>();
        List<Integer> matches = new ArrayList<>();

        if (text.isEmpty()) {
            long endTime = System.nanoTime();
            return ZFunctionResult.builder()
                    .processedString("")
                    .zArray(new int[0])
                    .matches(matches)
                    .matchCount(0)
                    .comparisons(0)
                    .executionTimeNanos(endTime - startTime)
                    .complexity(getComplexity())
                    .trace(trace)
                    .build();
        }

        String targetStr;
        boolean isPatternSearch = pattern != null && !pattern.isEmpty();
        int patternLen = isPatternSearch ? pattern.length() : 0;

        if (isPatternSearch) {
            targetStr = pattern + "$" + text;
        } else {
            targetStr = text;
        }

        int n = targetStr.length();
        int[] z = new int[n];
        long comparisons = 0;
        int stepCounter = 1;

        if (traceEnabled && trace.size() < maxTraceSteps) {
            trace.add(TraceStep.builder()
                    .step(stepCounter++)
                    .action("ZBOX_INIT")
                    .state(Map.of("processedString", targetStr, "length", n))
                    .description("Initializing Z-array and Z-box bounds [L=0, R=0]")
                    .build());
        }

        int l = 0;
        int r = 0;

        for (int i = 1; i < n; i++) {
            if (i <= r) {
                int k = i - l;
                if (z[k] < r - i + 1) {
                    z[i] = z[k];
                    if (traceEnabled && trace.size() < maxTraceSteps) {
                        trace.add(TraceStep.builder()
                                .step(stepCounter++)
                                .action("ZBOX_REUSE")
                                .state(Map.of("i", i, "L", l, "R", r, "k", k, "zVal", z[i]))
                                .description(String.format("Reused previous Z-value Z[%d] = %d inside Z-box [%d, %d]", k, z[i], l, r))
                                .build());
                    }
                } else {
                    l = i;
                    while (r < n) {
                        comparisons++;
                        if (targetStr.charAt(r - l) == targetStr.charAt(r)) {
                            r++;
                        } else {
                            break;
                        }
                    }
                    z[i] = r - l;
                    r--;
                    if (traceEnabled && trace.size() < maxTraceSteps) {
                        trace.add(TraceStep.builder()
                                .step(stepCounter++)
                                .action("ZBOX_EXTEND")
                                .state(Map.of("i", i, "L", l, "R", r, "zVal", z[i]))
                                .description(String.format("Extended Z-box from index %d: new box [%d, %d] with Z[%d] = %d", i, l, r, i, z[i]))
                                .build());
                    }
                }
            } else {
                l = i;
                r = i;
                while (r < n) {
                    comparisons++;
                    if (targetStr.charAt(r - l) == targetStr.charAt(r)) {
                        r++;
                    } else {
                        break;
                    }
                }
                z[i] = r - l;
                r--;
                if (traceEnabled && trace.size() < maxTraceSteps) {
                    trace.add(TraceStep.builder()
                            .step(stepCounter++)
                            .action("ZBOX_NEW")
                            .state(Map.of("i", i, "L", l, "R", r, "zVal", z[i]))
                            .description(String.format("Created new Z-box at index %d: [%d, %d] with Z[%d] = %d", i, l, r, i, z[i]))
                            .build());
                }
            }

            if (isPatternSearch && z[i] == patternLen) {
                int matchIndex = i - patternLen - 1;
                if (matchIndex >= 0) {
                    matches.add(matchIndex);
                    if (traceEnabled && trace.size() < maxTraceSteps) {
                        trace.add(TraceStep.builder()
                                .step(stepCounter++)
                                .action("PATTERN_MATCH")
                                .state(Map.of("i", i, "matchIndex", matchIndex, "patternLength", patternLen))
                                .description(String.format("Z[%d] == %d matching pattern length: found pattern at text index %d", i, patternLen, matchIndex))
                                .build());
                    }
                }
            }
        }

        long endTime = System.nanoTime();

        return ZFunctionResult.builder()
                .processedString(targetStr)
                .zArray(z)
                .matches(matches)
                .matchCount(matches.size())
                .comparisons(comparisons)
                .executionTimeNanos(endTime - startTime)
                .complexity(getComplexity())
                .trace(trace)
                .build();
    }
}
