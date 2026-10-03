package com.railsync.algorithm.m5.reductions.clique_is;

import com.railsync.algorithm.m5.common.CliqueToISInput;
import com.railsync.algorithm.m5.common.CliqueToISResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CliqueToIndependentSetReductionTest {

    private CliqueToIndependentSetReduction reduction;
    private CliqueToISService service;

    @BeforeEach
    void setUp() {
        reduction = new CliqueToIndependentSetReduction();
        service = new CliqueToISService(reduction);
    }

    @Test
    void testCliqueToIndependentSetReduction() {
        // Triangle graph (A, B, C) with clique size 3
        List<String> vertices = List.of("A", "B", "C", "D");
        List<List<String>> edges = List.of(
                List.of("A", "B"),
                List.of("B", "C"),
                List.of("A", "C")
        );

        CliqueToISInput input = CliqueToISInput.builder()
                .vertices(vertices)
                .edges(edges)
                .cliqueSize(3)
                .build();

        CliqueToISResult result = service.execute(input);

        assertEquals("CLIQUE", result.getSourceProblem());
        assertEquals("INDEPENDENT_SET", result.getTargetProblem());
        assertEquals(3, result.getIndependentSetSize());
        assertTrue(result.isEquivalent());

        // Independent brute-force verification: max clique in original G == max independent set in complement G
        int maxCliqueInG = findMaxCliqueBruteForce(vertices, edges);
        int maxISInComplement = findMaxIndependentSetBruteForce(result.getTransformedVertices(), result.getTransformedEdges());

        assertEquals(maxCliqueInG, maxISInComplement);
    }

    private int findMaxCliqueBruteForce(List<String> vertices, List<List<String>> edges) {
        int n = vertices.size();
        Set<String> edgeSet = new HashSet<>();
        for (List<String> e : edges) {
            edgeSet.add(e.get(0) + "---" + e.get(1));
            edgeSet.add(e.get(1) + "---" + e.get(0));
        }

        int maxClique = 0;
        int maxMask = 1 << n;
        for (int mask = 1; mask < maxMask; mask++) {
            List<String> sub = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    sub.add(vertices.get(i));
                }
            }

            boolean isClique = true;
            for (int i = 0; i < sub.size(); i++) {
                for (int j = i + 1; j < sub.size(); j++) {
                    if (!edgeSet.contains(sub.get(i) + "---" + sub.get(j))) {
                        isClique = false;
                        break;
                    }
                }
                if (!isClique) break;
            }

            if (isClique) {
                maxClique = Math.max(maxClique, sub.size());
            }
        }
        return maxClique;
    }

    private int findMaxIndependentSetBruteForce(List<String> vertices, List<List<String>> edges) {
        int n = vertices.size();
        Set<String> edgeSet = new HashSet<>();
        for (List<String> e : edges) {
            edgeSet.add(e.get(0) + "---" + e.get(1));
            edgeSet.add(e.get(1) + "---" + e.get(0));
        }

        int maxIS = 0;
        int maxMask = 1 << n;
        for (int mask = 1; mask < maxMask; mask++) {
            List<String> sub = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    sub.add(vertices.get(i));
                }
            }

            boolean isIS = true;
            for (int i = 0; i < sub.size(); i++) {
                for (int j = i + 1; j < sub.size(); j++) {
                    if (edgeSet.contains(sub.get(i) + "---" + sub.get(j))) {
                        isIS = false;
                        break;
                    }
                }
                if (!isIS) break;
            }

            if (isIS) {
                maxIS = Math.max(maxIS, sub.size());
            }
        }
        return maxIS;
    }
}
