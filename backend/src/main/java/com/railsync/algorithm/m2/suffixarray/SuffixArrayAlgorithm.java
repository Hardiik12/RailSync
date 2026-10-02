package com.railsync.algorithm.m2.suffixarray;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import com.railsync.algorithm.core.Algorithm;
import com.railsync.algorithm.m2.suffixarray.dto.SuffixArrayInput;
import com.railsync.algorithm.m2.suffixarray.dto.SuffixArrayResult;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SuffixArrayAlgorithm implements Algorithm<SuffixArrayInput, SuffixArrayResult> {

    public static class SuffixRank implements Comparable<SuffixRank> {
        public int index;
        public int rankPrimary;
        public int rankSecondary;

        public SuffixRank(int index, int rankPrimary, int rankSecondary) {
            this.index = index;
            this.rankPrimary = rankPrimary;
            this.rankSecondary = rankSecondary;
        }

        @Override
        public int compareTo(SuffixRank o) {
            if (this.rankPrimary != o.rankPrimary) {
                return Integer.compare(this.rankPrimary, o.rankPrimary);
            }
            return Integer.compare(this.rankSecondary, o.rankSecondary);
        }
    }

    @Override
    public String getName() {
        return "Suffix Array (Prefix Doubling)";
    }

    @Override
    public Complexity getComplexity() {
        return Complexity.builder()
                .time("O(n log^2 n)")
                .space("O(n)")
                .build();
    }

    @Override
    public SuffixArrayResult execute(SuffixArrayInput input) {
        long startTime = System.nanoTime();

        String text = input.getText() != null ? input.getText() : "";
        boolean traceEnabled = Boolean.TRUE.equals(input.getTraceEnabled());
        int maxTraceSteps = input.getMaxTraceSteps() != null ? input.getMaxTraceSteps() : 500;

        List<TraceStep> trace = new ArrayList<>();
        int n = text.length();

        if (n == 0) {
            long endTime = System.nanoTime();
            return SuffixArrayResult.builder()
                    .text("")
                    .suffixArray(new int[0])
                    .rankArray(new int[0])
                    .comparisons(0)
                    .executionTimeNanos(endTime - startTime)
                    .complexity(getComplexity())
                    .trace(trace)
                    .build();
        }

        SuffixRank[] suffixes = new SuffixRank[n];
        for (int i = 0; i < n; i++) {
            suffixes[i] = new SuffixRank(i, text.charAt(i), i + 1 < n ? text.charAt(i + 1) : -1);
        }

        long comparisons = 0;
        int stepCounter = 1;

        Arrays.sort(suffixes);
        comparisons += (long) (n * Math.log(n) / Math.log(2));

        int[] ind = new int[n];
        for (int k = 4; k < 2 * n; k *= 2) {
            int rank = 0;
            int prevRank = suffixes[0].rankPrimary;
            suffixes[0].rankPrimary = rank;
            ind[suffixes[0].index] = 0;

            for (int i = 1; i < n; i++) {
                comparisons++;
                if (suffixes[i].rankPrimary == prevRank && suffixes[i].rankSecondary == suffixes[i - 1].rankSecondary) {
                    suffixes[i].rankPrimary = rank;
                } else {
                    prevRank = suffixes[i].rankPrimary;
                    suffixes[i].rankPrimary = ++rank;
                }
                ind[suffixes[i].index] = i;
            }

            for (int i = 0; i < n; i++) {
                int nextIndex = suffixes[i].index + k / 2;
                suffixes[i].rankSecondary = (nextIndex < n) ? suffixes[ind[nextIndex]].rankPrimary : -1;
            }

            Arrays.sort(suffixes);
            comparisons += (long) (n * Math.log(n) / Math.log(2));

            if (traceEnabled && trace.size() < maxTraceSteps) {
                int[] currentSA = new int[n];
                for (int i = 0; i < n; i++) currentSA[i] = suffixes[i].index;
                trace.add(TraceStep.builder()
                        .step(stepCounter++)
                        .action("PREFIX_DOUBLING_ROUND")
                        .state(Map.of("k", k, "suffixArray", currentSA))
                        .description(String.format("Sorted suffixes by prefix length %d", k))
                        .build());
            }
        }

        int[] sa = new int[n];
        int[] ranks = new int[n];
        for (int i = 0; i < n; i++) {
            sa[i] = suffixes[i].index;
            ranks[sa[i]] = i;
        }

        long endTime = System.nanoTime();

        return SuffixArrayResult.builder()
                .text(text)
                .suffixArray(sa)
                .rankArray(ranks)
                .comparisons(comparisons)
                .executionTimeNanos(endTime - startTime)
                .complexity(getComplexity())
                .trace(trace)
                .build();
    }
}
