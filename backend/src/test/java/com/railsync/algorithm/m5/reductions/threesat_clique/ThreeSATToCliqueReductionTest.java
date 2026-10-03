package com.railsync.algorithm.m5.reductions.threesat_clique;

import com.railsync.algorithm.m5.common.CliqueReductionResult;
import com.railsync.algorithm.m5.common.SATInput;
import com.railsync.algorithm.m5.sat.SATAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ThreeSATToCliqueReductionTest {

    private ThreeSATToCliqueReduction reduction;
    private ThreeSATToCliqueService service;

    @BeforeEach
    void setUp() {
        SATAlgorithm satAlgorithm = new SATAlgorithm();
        reduction = new ThreeSATToCliqueReduction(satAlgorithm);
        service = new ThreeSATToCliqueService(reduction);
    }

    @Test
    void testReductionGraphConstruction() {
        SATInput input = SATInput.builder()
                .variables(List.of("A", "B", "C"))
                .stringClauses(List.of(
                        List.of("A", "!B", "C"),
                        List.of("!A", "B", "C")
                ))
                .build();

        CliqueReductionResult result = service.execute(input);

        assertEquals("3-SAT", result.getSourceProblem());
        assertEquals("CLIQUE", result.getTargetProblem());
        assertEquals(2, result.getTargetCliqueSize());
        assertEquals(6, result.getVertices().size()); // 2 clauses * 3 literals = 6 vertices
        assertTrue(result.isSatisfiable());
        assertFalse(result.getCliqueFound().isEmpty());

        // Independent brute-force clique verification
        int maxCliqueSize = findMaxCliqueBruteForce(result.getVertices(), result.getEdges());
        assertEquals(result.getTargetCliqueSize(), maxCliqueSize);
    }

    // Independent brute-force clique solver reference for tests
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
}
