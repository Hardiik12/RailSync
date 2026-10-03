package com.railsync.algorithm.m5.approx;

import com.railsync.algorithm.m5.common.VertexCoverApproxInput;
import com.railsync.algorithm.m5.common.VertexCoverApproxResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class VertexCoverTwoApproximationTest {

    private VertexCoverTwoApproximation approxAlgorithm;
    private VertexCoverApproxService service;

    @BeforeEach
    void setUp() {
        approxAlgorithm = new VertexCoverTwoApproximation();
        service = new VertexCoverApproxService(approxAlgorithm);
    }

    @Test
    void testDeterministicLineGraph() {
        List<String> vertices = List.of("A", "B", "C", "D");
        List<List<String>> edges = List.of(
                List.of("A", "B"),
                List.of("B", "C"),
                List.of("C", "D")
        );

        VertexCoverApproxInput input = VertexCoverApproxInput.builder()
                .vertices(vertices)
                .edges(edges)
                .traceEnabled(true)
                .build();

        VertexCoverApproxResult result = service.execute(input);

        assertTrue(result.isVerifiedCover());
        assertFalse(result.getCover().isEmpty());

        int exactOpt = findMinVertexCoverExact(vertices, edges);
        assertTrue(result.getCoverSize() <= 2 * exactOpt,
                "Approx cover size (" + result.getCoverSize() + ") must be <= 2 * OPT (" + exactOpt + ")");
    }

    @Test
    void testSeededRandomSmallGraphs() {
        Random random = new Random(42);
        for (int t = 0; t < 10; t++) {
            int numV = 6 + random.nextInt(5);
            List<String> vertices = new ArrayList<>();
            for (int i = 0; i < numV; i++) {
                vertices.add("V" + i);
            }

            List<List<String>> edges = new ArrayList<>();
            for (int i = 0; i < numV; i++) {
                for (int j = i + 1; j < numV; j++) {
                    if (random.nextDouble() < 0.3) {
                        edges.add(List.of(vertices.get(i), vertices.get(j)));
                    }
                }
            }

            if (edges.isEmpty()) continue;

            VertexCoverApproxInput input = VertexCoverApproxInput.builder()
                    .vertices(vertices)
                    .edges(edges)
                    .build();

            VertexCoverApproxResult result = service.execute(input);

            assertTrue(result.isVerifiedCover());

            int exactOpt = findMinVertexCoverExact(vertices, edges);
            assertTrue(result.getCoverSize() <= 2 * exactOpt,
                    "Random Graph " + t + ": Cover size (" + result.getCoverSize() + ") > 2 * OPT (" + exactOpt + ")");
        }
    }

    // Independent brute-force exact min vertex cover solver reference for tests
    private int findMinVertexCoverExact(List<String> vertices, List<List<String>> edges) {
        int n = vertices.size();
        int minCover = n;
        int maxMask = 1 << n;

        for (int mask = 0; mask < maxMask; mask++) {
            Set<String> cover = new HashSet<>();
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    cover.add(vertices.get(i));
                }
            }

            boolean isCover = true;
            for (List<String> e : edges) {
                if (!cover.contains(e.get(0)) && !cover.contains(e.get(1))) {
                    isCover = false;
                    break;
                }
            }

            if (isCover) {
                minCover = Math.min(minCover, cover.size());
            }
        }
        return minCover;
    }
}
