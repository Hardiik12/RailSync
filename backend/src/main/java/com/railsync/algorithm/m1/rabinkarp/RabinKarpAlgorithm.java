package com.railsync.algorithm.m1.rabinkarp;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import com.railsync.algorithm.core.Algorithm;
import com.railsync.algorithm.m1.rabinkarp.dto.RabinKarpInput;
import com.railsync.algorithm.m1.rabinkarp.dto.RabinKarpResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class RabinKarpAlgorithm implements Algorithm<RabinKarpInput, RabinKarpResult> {

    private static final int BASE = 256;
    private static final long DEFAULT_PRIME = 1000000007L;

    @Override
    public String getName() {
        return "Rabin-Karp Algorithm";
    }

    @Override
    public Complexity getComplexity() {
        return Complexity.builder()
                .time("O(n + m) avg, O(n * m) worst")
                .space("O(1)")
                .build();
    }

    @Override
    public RabinKarpResult execute(RabinKarpInput input) {
        long startTime = System.nanoTime();

        String text = input.getText() != null ? input.getText() : "";
        String pattern = input.getPattern() != null ? input.getPattern() : "";
        boolean traceEnabled = Boolean.TRUE.equals(input.getTraceEnabled());
        int maxTraceSteps = input.getMaxTraceSteps() != null ? input.getMaxTraceSteps() : 500;
        long prime = input.getPrimeModulus() != null && input.getPrimeModulus() > 0 
                ? input.getPrimeModulus() : DEFAULT_PRIME;

        List<TraceStep> trace = new ArrayList<>();
        List<Integer> matches = new ArrayList<>();

        int n = text.length();
        int m = pattern.length();

        if (m == 0 || n == 0 || m > n) {
            long endTime = System.nanoTime();
            return RabinKarpResult.builder()
                    .matches(matches)
                    .matchCount(0)
                    .patternHash(0)
                    .primeModulus(prime)
                    .hashCollisions(0)
                    .hashVerifications(0)
                    .comparisons(0)
                    .hashOperations(0)
                    .executionTimeNanos(endTime - startTime)
                    .complexity(getComplexity())
                    .trace(trace)
                    .build();
        }

        long comparisons = 0;
        long hashOperations = 0;
        int hashCollisions = 0;
        int hashVerifications = 0;
        int stepCounter = 1;

        // Calculate h = pow(BASE, m-1) % prime
        long h = 1;
        for (int i = 0; i < m - 1; i++) {
            h = (h * BASE) % prime;
            hashOperations++;
        }

        // Calculate initial hash value for pattern and first window of text
        long pHash = 0;
        long tHash = 0;

        for (int i = 0; i < m; i++) {
            pHash = (BASE * pHash + pattern.charAt(i)) % prime;
            tHash = (BASE * tHash + text.charAt(i)) % prime;
            hashOperations += 2;
        }

        if (traceEnabled && trace.size() < maxTraceSteps) {
            trace.add(TraceStep.builder()
                    .step(stepCounter++)
                    .action("HASH_INIT")
                    .state(Map.of("pattern", pattern, "patternHash", pHash, "initialWindowHash", tHash, "primeModulus", prime))
                    .description(String.format("Calculated initial hashes with mod %d: patternHash=%d, textWindow[0..%d]Hash=%d", prime, pHash, m - 1, tHash))
                    .build());
        }

        // Slide the pattern over text one by one
        for (int i = 0; i <= n - m; i++) {

            if (pHash == tHash) {
                hashVerifications++;
                boolean match = true;

                if (traceEnabled && trace.size() < maxTraceSteps) {
                    trace.add(TraceStep.builder()
                            .step(stepCounter++)
                            .action("HASH_MATCH")
                            .state(Map.of("windowIndex", i, "windowText", text.substring(i, i + m), "hash", tHash))
                            .description(String.format("Hash match found at window index %d (%d == %d). Starting character verification.", i, pHash, tHash))
                            .build());
                }

                // Verify characters one by one
                for (int j = 0; j < m; j++) {
                    comparisons++;
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        match = false;
                        hashCollisions++;
                        if (traceEnabled && trace.size() < maxTraceSteps) {
                            trace.add(TraceStep.builder()
                                    .step(stepCounter++)
                                    .action("HASH_COLLISION")
                                    .state(Map.of("windowIndex", i, "mismatchIndex", i + j, "expectedChar", pattern.charAt(j), "foundChar", text.charAt(i + j)))
                                    .description(String.format("Hash collision at index %d! Hashes matched (%d), but char mismatch at offset %d.", i, pHash, j))
                                    .build());
                        }
                        break;
                    }
                }

                if (match) {
                    matches.add(i);
                    if (traceEnabled && trace.size() < maxTraceSteps) {
                        trace.add(TraceStep.builder()
                                .step(stepCounter++)
                                .action("PATTERN_MATCH")
                                .state(Map.of("windowIndex", i, "matchIndex", i))
                                .description(String.format("Pattern match verified successfully at text index %d.", i))
                                .build());
                    }
                }
            } else {
                if (traceEnabled && trace.size() < maxTraceSteps) {
                    trace.add(TraceStep.builder()
                            .step(stepCounter++)
                            .action("HASH_MISMATCH")
                            .state(Map.of("windowIndex", i, "patternHash", pHash, "windowHash", tHash))
                            .description(String.format("Hash mismatch at index %d (patternHash=%d != windowHash=%d). Skipping character checks.", i, pHash, tHash))
                            .build());
                }
            }

            // Calculate hash value for next window of text
            if (i < n - m) {
                tHash = (BASE * (tHash - text.charAt(i) * h) + text.charAt(i + m)) % prime;
                hashOperations++;
                if (tHash < 0) {
                    tHash = (tHash + prime);
                }
            }
        }

        long endTime = System.nanoTime();

        return RabinKarpResult.builder()
                .matches(matches)
                .matchCount(matches.size())
                .patternHash(pHash)
                .primeModulus(prime)
                .hashCollisions(hashCollisions)
                .hashVerifications(hashVerifications)
                .comparisons(comparisons)
                .hashOperations(hashOperations)
                .executionTimeNanos(endTime - startTime)
                .complexity(getComplexity())
                .trace(trace)
                .build();
    }
}
