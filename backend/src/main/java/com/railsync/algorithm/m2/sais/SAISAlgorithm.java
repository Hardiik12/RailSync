package com.railsync.algorithm.m2.sais;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import com.railsync.algorithm.core.Algorithm;
import com.railsync.algorithm.m2.sais.dto.SAISInput;
import com.railsync.algorithm.m2.sais.dto.SAISResult;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SAISAlgorithm implements Algorithm<SAISInput, SAISResult> {

    private static class Counter {
        long comparisons = 0;
        int maxRecursionDepth = 0;
    }

    @Override
    public String getName() {
        return "SA-IS (Suffix Array Induced Sorting)";
    }

    @Override
    public Complexity getComplexity() {
        return Complexity.builder()
                .time("O(n)")
                .space("O(n)")
                .build();
    }

    @Override
    public SAISResult execute(SAISInput input) {
        long startTime = System.nanoTime();

        String rawText = input.getText() != null ? input.getText() : "";
        boolean traceEnabled = Boolean.TRUE.equals(input.getTraceEnabled());
        int maxTraceSteps = input.getMaxTraceSteps() != null ? input.getMaxTraceSteps() : 500;

        List<TraceStep> trace = new ArrayList<>();

        if (rawText.isEmpty()) {
            long endTime = System.nanoTime();
            return SAISResult.builder()
                    .text("")
                    .suffixArray(new int[0])
                    .lmsCount(0)
                    .recursionDepth(0)
                    .comparisons(0)
                    .executionTimeNanos(endTime - startTime)
                    .complexity(getComplexity())
                    .trace(trace)
                    .build();
        }

        // Convert string to integer array with sentinel 0 appended at the end
        int n = rawText.length() + 1;
        int[] s = new int[n];
        int maxChar = 0;
        for (int i = 0; i < rawText.length(); i++) {
            s[i] = rawText.charAt(i) + 1; // shift by +1 so 0 is reserved for sentinel '$'
            maxChar = Math.max(maxChar, s[i]);
        }
        s[n - 1] = 0; // sentinel '$'

        Counter counter = new Counter();
        int stepCounter = 1;

        int[] saWithSentinel = sais(s, maxChar + 1, 0, counter, trace, traceEnabled, maxTraceSteps, stepCounter);

        // Strip the sentinel index (which is always saWithSentinel[0] = n-1)
        int[] sa = new int[rawText.length()];
        for (int i = 1; i < n; i++) {
            sa[i - 1] = saWithSentinel[i];
        }

        long endTime = System.nanoTime();

        return SAISResult.builder()
                .text(rawText)
                .suffixArray(sa)
                .lmsCount(countLMS(s))
                .recursionDepth(counter.maxRecursionDepth)
                .comparisons(counter.comparisons)
                .executionTimeNanos(endTime - startTime)
                .complexity(getComplexity())
                .trace(trace)
                .build();
    }

    private int countLMS(int[] s) {
        int n = s.length;
        boolean[] isS = classifySL(s);
        int count = 0;
        for (int i = 1; i < n; i++) {
            if (isS[i] && !isS[i - 1]) count++;
        }
        return count;
    }

    private boolean[] classifySL(int[] s) {
        int n = s.length;
        boolean[] isS = new boolean[n];
        isS[n - 1] = true; // Sentinel is S-type
        for (int i = n - 2; i >= 0; i--) {
            if (s[i] < s[i + 1]) {
                isS[i] = true;
            } else if (s[i] > s[i + 1]) {
                isS[i] = false;
            } else {
                isS[i] = isS[i + 1];
            }
        }
        return isS;
    }

    private int[] sais(int[] s, int alphabetSize, int depth, Counter counter, List<TraceStep> trace, boolean traceEnabled, int maxTraceSteps, int stepCounter) {
        counter.maxRecursionDepth = Math.max(counter.maxRecursionDepth, depth);
        int n = s.length;
        if (n == 1) {
            return new int[]{0};
        }

        boolean[] isS = classifySL(s);

        // Find LMS indices
        List<Integer> lmsIndicesList = new ArrayList<>();
        for (int i = 1; i < n; i++) {
            if (isS[i] && !isS[i - 1]) {
                lmsIndicesList.add(i);
            }
        }
        int[] lmsIndices = lmsIndicesList.stream().mapToInt(Integer::intValue).toArray();

        if (traceEnabled && trace.size() < maxTraceSteps) {
            trace.add(TraceStep.builder()
                    .step(stepCounter++)
                    .action("SAIS_PHASE_SL_CLASSIFICATION")
                    .state(Map.of("depth", depth, "lmsCount", lmsIndices.length, "lmsIndices", lmsIndices))
                    .description(String.format("Depth %d: Classified S/L types and identified %d LMS suffixes", depth, lmsIndices.length))
                    .build());
        }

        int[] buckets = getBucketCounts(s, alphabetSize);
        int[] sa = new int[n];
        Arrays.fill(sa, -1);

        // Step 1: Induced sort using initial LMS placement
        inducedSort(s, sa, isS, lmsIndices, buckets, alphabetSize, counter);

        if (traceEnabled && trace.size() < maxTraceSteps) {
            trace.add(TraceStep.builder()
                    .step(stepCounter++)
                    .action("SAIS_PHASE_INDUCED_SORT_1")
                    .state(Map.of("depth", depth))
                    .description(String.format("Depth %d: Executed initial induced sorting pass for LMS suffixes", depth))
                    .build());
        }

        // Step 2: Name LMS substrings and check uniqueness
        int[] sortedLms = new int[lmsIndices.length];
        int idx = 0;
        for (int i = 0; i < n; i++) {
            if (sa[i] > 0 && isS[sa[i]] && !isS[sa[i] - 1]) {
                sortedLms[idx++] = sa[i];
            }
        }

        int[] lmsNames = new int[n];
        Arrays.fill(lmsNames, -1);
        int currentName = 0;
        lmsNames[sortedLms[0]] = currentName;

        for (int i = 1; i < sortedLms.length; i++) {
            int prev = sortedLms[i - 1];
            int curr = sortedLms[i];
            counter.comparisons++;
            if (!isEqualLmsSubstring(s, isS, prev, curr)) {
                currentName++;
            }
            lmsNames[curr] = currentName;
        }

        // Step 3: Check if recursive reduced problem is needed
        int[] orderedLms;
        if (currentName + 1 < sortedLms.length) {
            // Build reduced string s1
            int[] s1 = new int[lmsIndices.length];
            for (int i = 0; i < lmsIndices.length; i++) {
                s1[i] = lmsNames[lmsIndices[i]];
            }

            // Recurse SA-IS on s1
            int[] sa1 = sais(s1, currentName + 1, depth + 1, counter, trace, traceEnabled, maxTraceSteps, stepCounter);

            orderedLms = new int[lmsIndices.length];
            for (int i = 0; i < sa1.length; i++) {
                orderedLms[i] = lmsIndices[sa1[i]];
            }
        } else {
            // All LMS substrings are unique
            orderedLms = sortedLms;
        }

        // Step 4: Final induced sort using ordered LMS
        Arrays.fill(sa, -1);
        inducedSort(s, sa, isS, orderedLms, buckets, alphabetSize, counter);

        if (traceEnabled && trace.size() < maxTraceSteps) {
            trace.add(TraceStep.builder()
                    .step(stepCounter++)
                    .action("SAIS_PHASE_FINAL_INDUCED_SORT")
                    .state(Map.of("depth", depth))
                    .description(String.format("Depth %d: Completed final induced sorting pass producing exact SA", depth))
                    .build());
        }

        return sa;
    }

    private boolean isEqualLmsSubstring(int[] s, boolean[] isS, int i, int j) {
        int n = s.length;
        int k = 0;
        while (true) {
            if (s[i + k] != s[j + k]) return false;
            boolean isLmsI = k > 0 && isS[i + k] && !isS[i + k - 1];
            boolean isLmsJ = k > 0 && isS[j + k] && !isS[j + k - 1];
            if (isLmsI && isLmsJ) return true;
            if (isLmsI != isLmsJ) return false;
            k++;
            if (i + k >= n || j + k >= n) return false;
        }
    }

    private int[] getBucketCounts(int[] s, int alphabetSize) {
        int[] buckets = new int[alphabetSize];
        for (int v : s) buckets[v]++;
        return buckets;
    }

    private void inducedSort(int[] s, int[] sa, boolean[] isS, int[] lmsIndices, int[] bucketCounts, int alphabetSize, Counter counter) {
        int n = s.length;

        // Place LMS elements at bucket tails
        int[] bucketTails = new int[alphabetSize];
        int sum = 0;
        for (int i = 0; i < alphabetSize; i++) {
            sum += bucketCounts[i];
            bucketTails[i] = sum;
        }

        for (int i = lmsIndices.length - 1; i >= 0; i--) {
            int pos = lmsIndices[i];
            int charVal = s[pos];
            sa[--bucketTails[charVal]] = pos;
        }

        // Induce L-type elements from left to right
        int[] bucketHeads = new int[alphabetSize];
        sum = 0;
        for (int i = 0; i < alphabetSize; i++) {
            bucketHeads[i] = sum;
            sum += bucketCounts[i];
        }

        for (int i = 0; i < n; i++) {
            if (sa[i] > 0) {
                int pos = sa[i] - 1;
                counter.comparisons++;
                if (!isS[pos]) { // L-type
                    int charVal = s[pos];
                    sa[bucketHeads[charVal]++] = pos;
                }
            }
        }

        // Induce S-type elements from right to left
        sum = 0;
        for (int i = 0; i < alphabetSize; i++) {
            sum += bucketCounts[i];
            bucketTails[i] = sum;
        }

        for (int i = n - 1; i >= 0; i--) {
            if (sa[i] > 0) {
                int pos = sa[i] - 1;
                counter.comparisons++;
                if (isS[pos]) { // S-type
                    int charVal = s[pos];
                    sa[--bucketTails[charVal]] = pos;
                }
            }
        }
    }
}
