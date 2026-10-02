package com.railsync.algorithm.m4.konig;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import com.railsync.algorithm.core.Algorithm;
import com.railsync.algorithm.m4.bipartitematching.dto.BipartiteEdgeDto;
import com.railsync.algorithm.m4.bipartitematching.dto.MatchedPairDto;
import com.railsync.algorithm.m4.konig.dto.KonigInput;
import com.railsync.algorithm.m4.konig.dto.KonigResult;
import com.railsync.algorithm.m4.konig.dto.VertexCoverDto;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class KonigAlgorithm implements Algorithm<KonigInput, KonigResult> {

    @Override
    public String getName() {
        return "König's Theorem (Bipartite Matching = Vertex Cover)";
    }

    @Override
    public Complexity getComplexity() {
        return Complexity.builder()
                .time("O(V * E)")
                .space("O(V + E)")
                .build();
    }

    @Override
    public KonigResult execute(KonigInput input) {
        long startTime = System.nanoTime();

        List<String> leftList = input.getLeftVertices() != null ? input.getLeftVertices() : Collections.emptyList();
        List<String> rightList = input.getRightVertices() != null ? input.getRightVertices() : Collections.emptyList();
        List<BipartiteEdgeDto> edges = input.getEdges() != null ? input.getEdges() : Collections.emptyList();
        boolean traceEnabled = Boolean.TRUE.equals(input.getTraceEnabled());
        int maxTraceSteps = input.getMaxTraceSteps() != null ? input.getMaxTraceSteps() : 500;

        List<TraceStep> trace = new ArrayList<>();

        if (leftList.isEmpty() || rightList.isEmpty()) {
            long endTime = System.nanoTime();
            return KonigResult.builder()
                    .matchingSize(0)
                    .vertexCoverSize(0)
                    .sizesEqual(true)
                    .maximumMatching(Collections.emptyList())
                    .minimumVertexCover(Collections.emptyList())
                    .alternatingReachableVertices(Collections.emptyList())
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

        for (int u = 0; u < leftList.size(); u++) {
            boolean[] visitedR = new boolean[rightList.size()];
            if (tryAugment(u, adj, matchR, matchL, visitedR, comparisons)) {
                matchingSize++;
            }
        }

        List<MatchedPairDto> maximumMatching = new ArrayList<>();
        for (int u = 0; u < leftList.size(); u++) {
            if (matchL[u] != -1) {
                maximumMatching.add(MatchedPairDto.builder()
                        .left(leftList.get(u))
                        .right(rightList.get(matchL[u]))
                        .build());
            }
        }

        // Alternating reachability BFS from unmatched left vertices
        boolean[] reachableL = new boolean[leftList.size()];
        boolean[] reachableR = new boolean[rightList.size()];
        Queue<Integer> queueL = new LinkedList<>();

        for (int u = 0; u < leftList.size(); u++) {
            if (matchL[u] == -1) {
                reachableL[u] = true;
                queueL.add(u);
            }
        }

        int stepCounter = 1;
        while (!queueL.isEmpty()) {
            int u = queueL.poll();
            comparisons++;

            for (int v : adj.get(u)) {
                if (v != matchL[u] && !reachableR[v]) { // unmatched edge
                    reachableR[v] = true;
                    if (traceEnabled && trace.size() < maxTraceSteps) {
                        trace.add(TraceStep.builder()
                                .step(stepCounter++)
                                .action("KONIG_ALTERNATING_REACH")
                                .state(Map.of("fromLeft", leftList.get(u), "toRight", rightList.get(v)))
                                .description(String.format("Alternating path reached right vertex '%s' from '%s'", rightList.get(v), leftList.get(u)))
                                .build());
                    }

                    int matchedLeft = matchR[v];
                    if (matchedLeft != -1 && !reachableL[matchedLeft]) { // matched edge back to left
                        reachableL[matchedLeft] = true;
                        queueL.add(matchedLeft);
                    }
                }
            }
        }

        List<VertexCoverDto> vertexCover = new ArrayList<>();
        List<String> reachableNames = new ArrayList<>();

        for (int u = 0; u < leftList.size(); u++) {
            if (reachableL[u]) {
                reachableNames.add("L:" + leftList.get(u));
            } else {
                vertexCover.add(VertexCoverDto.builder().name(leftList.get(u)).partition("LEFT").build());
            }
        }

        for (int v = 0; v < rightList.size(); v++) {
            if (reachableR[v]) {
                reachableNames.add("R:" + rightList.get(v));
                vertexCover.add(VertexCoverDto.builder().name(rightList.get(v)).partition("RIGHT").build());
            }
        }

        boolean isEqual = (matchingSize == vertexCover.size());

        long endTime = System.nanoTime();

        return KonigResult.builder()
                .matchingSize(matchingSize)
                .vertexCoverSize(vertexCover.size())
                .sizesEqual(isEqual)
                .maximumMatching(maximumMatching)
                .minimumVertexCover(vertexCover)
                .alternatingReachableVertices(reachableNames)
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
