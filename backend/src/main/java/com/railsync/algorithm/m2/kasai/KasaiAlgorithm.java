package com.railsync.algorithm.m2.kasai;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import com.railsync.algorithm.core.Algorithm;
import com.railsync.algorithm.m2.kasai.dto.KasaiInput;
import com.railsync.algorithm.m2.kasai.dto.KasaiResult;
import com.railsync.algorithm.m2.suffixarray.SuffixArrayAlgorithm;
import com.railsync.algorithm.m2.suffixarray.dto.SuffixArrayInput;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class KasaiAlgorithm implements Algorithm<KasaiInput, KasaiResult> {

    private final SuffixArrayAlgorithm suffixArrayAlgorithm = new SuffixArrayAlgorithm();

    @Override
    public String getName() {
        return "Kasai's LCP Algorithm";
    }

    @Override
    public Complexity getComplexity() {
        return Complexity.builder()
                .time("O(n)")
                .space("O(n)")
                .build();
    }

    @Override
    public KasaiResult execute(KasaiInput input) {
        long startTime = System.nanoTime();

        String text = input.getText() != null ? input.getText() : "";
        boolean traceEnabled = Boolean.TRUE.equals(input.getTraceEnabled());
        int maxTraceSteps = input.getMaxTraceSteps() != null ? input.getMaxTraceSteps() : 500;

        List<TraceStep> trace = new ArrayList<>();
        int n = text.length();

        if (n == 0) {
            long endTime = System.nanoTime();
            return KasaiResult.builder()
                    .text("")
                    .suffixArray(new int[0])
                    .lcpArray(new int[0])
                    .maxLcpValue(0)
                    .longestRepeatedSubstring("")
                    .comparisons(0)
                    .executionTimeNanos(endTime - startTime)
                    .complexity(getComplexity())
                    .trace(trace)
                    .build();
        }

        int[] sa = input.getSuffixArray();
        if (sa == null || sa.length != n) {
            sa = suffixArrayAlgorithm.execute(SuffixArrayInput.builder().text(text).build()).getSuffixArray();
        }

        // Step 1: Compute Rank Array
        int[] rank = new int[n];
        for (int i = 0; i < n; i++) {
            rank[sa[i]] = i;
        }

        int[] lcp = new int[n];
        int h = 0;
        long comparisons = 0;
        int maxLcp = 0;
        int maxLcpIndex = -1;
        int stepCounter = 1;

        // Step 2: Kasai's O(n) LCP computation
        for (int i = 0; i < n; i++) {
            if (rank[i] > 0) {
                int j = sa[rank[i] - 1]; // previous suffix in SA

                while (i + h < n && j + h < n && text.charAt(i + h) == text.charAt(j + h)) {
                    h++;
                    comparisons++;
                }
                lcp[rank[i]] = h;

                if (h > maxLcp) {
                    maxLcp = h;
                    maxLcpIndex = i;
                }

                if (traceEnabled && trace.size() < maxTraceSteps) {
                    trace.add(TraceStep.builder()
                            .step(stepCounter++)
                            .action("KASAI_LCP_CALC")
                            .state(Map.of("i", i, "rank", rank[i], "j", j, "lcpVal", h))
                            .description(String.format("Calculated LCP between suffix at %d ('%s') and suffix at %d ('%s'): LCP = %d",
                                    i, text.substring(i, Math.min(n, i + 10)), j, text.substring(j, Math.min(n, j + 10)), h))
                            .build());
                }

                if (h > 0) {
                    h--;
                }
            } else {
                lcp[0] = 0; // By convention LCP[0] is 0
            }
        }

        String lrs = (maxLcp > 0 && maxLcpIndex >= 0) ? text.substring(maxLcpIndex, maxLcpIndex + maxLcp) : "";

        long endTime = System.nanoTime();

        return KasaiResult.builder()
                .text(text)
                .suffixArray(sa)
                .lcpArray(lcp)
                .maxLcpValue(maxLcp)
                .longestRepeatedSubstring(lrs)
                .comparisons(comparisons)
                .executionTimeNanos(endTime - startTime)
                .complexity(getComplexity())
                .trace(trace)
                .build();
    }
}
