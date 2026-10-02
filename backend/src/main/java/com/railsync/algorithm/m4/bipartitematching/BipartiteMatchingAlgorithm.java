package com.railsync.algorithm.m4.bipartitematching;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import com.railsync.algorithm.core.Algorithm;
import com.railsync.algorithm.m4.bipartitematching.dto.BipartiteEdgeDto;
import com.railsync.algorithm.m4.bipartitematching.dto.BipartiteMatchingInput;
import com.railsync.algorithm.m4.bipartitematching.dto.BipartiteMatchingResult;
import com.railsync.algorithm.m4.bipartitematching.dto.MatchedPairDto;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class BipartiteMatchingAlgorithm implements Algorithm<BipartiteMatchingInput, BipartiteMatchingResult> {

    @Override
    public String getName() {
        return "Bipartite Maximum Matching (Augmenting Path)";
    }

    @Override
    public Complexity getComplexity() {
        return Complexity.builder()
                .time("O(V * E)")
                .space("O(V + E)")
                .build();
    }

    @Override
    public BipartiteMatchingResult execute(BipartiteMatchingInput input) {
        long startTime = System.nanoTime();

        List<String> leftList = input.getLeftVertices() != null ? input.getLeftVertices() : Collections.emptyList();
        List<String> rightList = input.getRightVertices() != null ? input.getRightVertices() : Collections.emptyList();
        List<BipartiteEdgeDto> edges = input.getEdges() != null ? input.getEdges() : Collections.emptyList();
        boolean traceEnabled = Boolean.TRUE.equals(input.getTraceEnabled());
        int maxTraceSteps = input.getMaxTraceSteps() != null ? input.getMaxTraceSteps() : 500;

        List<TraceStep> trace = new ArrayList<>();

        if (leftList.isEmpty() || rightList.isEmpty()) {
            long endTime = System.nanoTime();
            return BipartiteMatchingResult.builder()
                    .matchingSize(0)
                    .matchedPairs(Collections.emptyList())
                    .unmatchedLeftVertices(leftList)
                    .unmatchedRightVertices(rightList)
                    .comparisons(0)
                    .executionTimeNanos(endTime - startTime)
                    .complexity(getComplexity())
                    .trace(trace)
                    .build();
        }

        Map<String, Integer> leftIndexMap = new HashMap<>();
        for (int i = 0; i < leftList.size(); i++) {
            leftIndexMap.put(leftList.get(i), i);
        }

        Map<String, Integer> rightIndexMap = new HashMap<>();
        for (int j = 0; j < rightList.size(); j++) {
            rightIndexMap.put(rightList.get(j), j);
        }

        List<List<Integer>> adj = new ArrayList<>(leftList.size());
        for (int i = 0; i < leftList.size(); i++) {
            adj.add(new ArrayList<>());
        }

        for (BipartiteEdgeDto edge : edges) {
            Integer u = leftIndexMap.get(edge.getLeft());
            Integer v = rightIndexMap.get(edge.getRight());
            if (u != null && v != null) {
                adj.get(u).add(v);
            }
        }

        int[] matchR = new int[rightList.size()];
        int[] matchL = new int[leftList.size()];
        Arrays.fill(matchR, -1);
        Arrays.fill(matchL, -1);

        int matchingSize = 0;
        long comparisons = 0;
        int stepCounter = 1;

        for (int u = 0; u < leftList.size(); u++) {
            boolean[] visitedR = new boolean[rightList.size()];
            if (tryAugment(u, adj, matchR, matchL, visitedR, comparisons)) {
                matchingSize++;
                if (traceEnabled && trace.size() < maxTraceSteps) {
                    trace.add(TraceStep.builder()
                            .step(stepCounter++)
                            .action("BIPARTITE_MATCH_AUGMENT")
                            .state(Map.of("leftVertex", leftList.get(u), "matchedSize", matchingSize))
                            .description(String.format("Found augmenting path for '%s', current matching size: %d", leftList.get(u), matchingSize))
                            .build());
                }
            }
        }

        List<MatchedPairDto> matchedPairs = new ArrayList<>();
        List<String> unmatchedLeft = new ArrayList<>();
        List<String> unmatchedRight = new ArrayList<>();

        for (int u = 0; u < leftList.size(); u++) {
            if (matchL[u] != -1) {
                matchedPairs.add(MatchedPairDto.builder()
                        .left(leftList.get(u))
                        .right(rightList.get(matchL[u]))
                        .build());
            } else {
                unmatchedLeft.add(leftList.get(u));
            }
        }

        for (int v = 0; v < rightList.size(); v++) {
            if (matchR[v] == -1) {
                unmatchedRight.add(rightList.get(v));
            }
        }

        long endTime = System.nanoTime();

        return BipartiteMatchingResult.builder()
                .matchingSize(matchingSize)
                .matchedPairs(matchedPairs)
                .unmatchedLeftVertices(unmatchedLeft)
                .unmatchedRightVertices(unmatchedRight)
                .comparisons(comparisons)
                .executionTimeNanos(endTime - startTime)
                .complexity(getComplexity())
                .trace(trace)
                .build();
    }

    private boolean tryAugment(int u, List<List<Integer>> adj, int[] matchR, int[] matchL, boolean[] visitedR, long comparisons) {
        for (int v : adj.get(u)) {
            comparisons++;
            if (!visitedR[v]) {
                visitedR[v] = true;
                if (matchR[v] < 0 || tryAugment(matchR[v], adj, matchR, matchL, visitedR, comparisons)) {
                    matchR[v] = u;
                    matchL[u] = v;
                    return true;
                }
            }
        }
        return false;
    }
}
