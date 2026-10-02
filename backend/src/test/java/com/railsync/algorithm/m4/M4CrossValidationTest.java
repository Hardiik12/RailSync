package com.railsync.algorithm.m4;

import com.railsync.algorithm.m4.bipartitematching.BipartiteMatchingAlgorithm;
import com.railsync.algorithm.m4.bipartitematching.dto.BipartiteEdgeDto;
import com.railsync.algorithm.m4.bipartitematching.dto.BipartiteMatchingInput;
import com.railsync.algorithm.m4.bipartitematching.dto.BipartiteMatchingResult;
import com.railsync.algorithm.m4.bipartitematching.dto.MatchedPairDto;
import com.railsync.algorithm.m4.common.dto.EdgeInputDto;
import com.railsync.algorithm.m4.dinic.DinicAlgorithm;
import com.railsync.algorithm.m4.dinic.dto.DinicInput;
import com.railsync.algorithm.m4.dinic.dto.DinicResult;
import com.railsync.algorithm.m4.edmondskarp.EdmondsKarpAlgorithm;
import com.railsync.algorithm.m4.edmondskarp.dto.EdmondsKarpInput;
import com.railsync.algorithm.m4.edmondskarp.dto.EdmondsKarpResult;
import com.railsync.algorithm.m4.fordfulkerson.FordFulkersonAlgorithm;
import com.railsync.algorithm.m4.fordfulkerson.dto.FordFulkersonInput;
import com.railsync.algorithm.m4.fordfulkerson.dto.FordFulkersonResult;
import com.railsync.algorithm.m4.konig.KonigAlgorithm;
import com.railsync.algorithm.m4.konig.dto.KonigInput;
import com.railsync.algorithm.m4.konig.dto.KonigResult;
import com.railsync.algorithm.m4.maxflowmincut.MaxFlowMinCutAlgorithm;
import com.railsync.algorithm.m4.maxflowmincut.dto.MaxFlowMinCutInput;
import com.railsync.algorithm.m4.maxflowmincut.dto.MaxFlowMinCutResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class M4CrossValidationTest {

    private FordFulkersonAlgorithm fordFulkerson;
    private EdmondsKarpAlgorithm edmondsKarp;
    private DinicAlgorithm dinic;
    private BipartiteMatchingAlgorithm bipartiteMatching;
    private KonigAlgorithm konig;
    private MaxFlowMinCutAlgorithm maxFlowMinCut;

    @BeforeEach
    void setUp() {
        fordFulkerson = new FordFulkersonAlgorithm();
        edmondsKarp = new EdmondsKarpAlgorithm();
        dinic = new DinicAlgorithm();
        bipartiteMatching = new BipartiteMatchingAlgorithm();
        konig = new KonigAlgorithm();
        maxFlowMinCut = new MaxFlowMinCutAlgorithm();
    }

    // --- 1. MAX FLOW AGREEMENT CROSS-VALIDATION ---

    @Test
    @DisplayName("Cross-Validation: Ford-Fulkerson == Edmonds-Karp == Dinic (Deterministic Graph)")
    void crossValidateMaxFlowDeterministic() {
        List<EdgeInputDto> edges = List.of(
                EdgeInputDto.builder().u(0).v(1).capacity(10.0).uName("S").vName("A").build(),
                EdgeInputDto.builder().u(0).v(2).capacity(10.0).uName("S").vName("B").build(),
                EdgeInputDto.builder().u(1).v(3).capacity(4.0).uName("A").vName("C").build(),
                EdgeInputDto.builder().u(1).v(4).capacity(8.0).uName("A").vName("D").build(),
                EdgeInputDto.builder().u(2).v(4).capacity(9.0).uName("B").vName("D").build(),
                EdgeInputDto.builder().u(3).v(5).capacity(10.0).uName("C").vName("T").build(),
                EdgeInputDto.builder().u(4).v(5).capacity(10.0).uName("D").vName("T").build()
        );

        FordFulkersonResult ffRes = fordFulkerson.execute(FordFulkersonInput.builder().vertexCount(6).source(0).sink(5).edges(edges).build());
        EdmondsKarpResult ekRes = edmondsKarp.execute(EdmondsKarpInput.builder().vertexCount(6).source(0).sink(5).edges(edges).build());
        DinicResult dinicRes = dinic.execute(DinicInput.builder().vertexCount(6).source(0).sink(5).edges(edges).build());

        assertThat(ffRes.getMaxFlow()).isEqualTo(ekRes.getMaxFlow());
        assertThat(ekRes.getMaxFlow()).isEqualTo(dinicRes.getMaxFlow());
        assertThat(ffRes.getMaxFlow()).isEqualTo(14.0);
    }

    @Test
    @DisplayName("Cross-Validation: Max Flow Agreement on Seeded Random Flow Graphs")
    void crossValidateMaxFlowRandomGraphs() {
        Random rand = new Random(42);

        for (int t = 0; t < 15; t++) {
            int n = 5 + rand.nextInt(5);
            int s = 0;
            int sink = n - 1;

            List<EdgeInputDto> edges = new ArrayList<>();
            for (int u = 0; u < n; u++) {
                for (int v = u + 1; v < n; v++) {
                    if (rand.nextDouble() < 0.6) {
                        double cap = 1 + rand.nextInt(20);
                        edges.add(EdgeInputDto.builder().u(u).v(v).capacity(cap).uName("N" + u).vName("N" + v).build());
                    }
                }
            }

            double ffFlow = fordFulkerson.execute(FordFulkersonInput.builder().vertexCount(n).source(s).sink(sink).edges(edges).build()).getMaxFlow();
            double ekFlow = edmondsKarp.execute(EdmondsKarpInput.builder().vertexCount(n).source(s).sink(sink).edges(edges).build()).getMaxFlow();
            double dinicFlow = dinic.execute(DinicInput.builder().vertexCount(n).source(s).sink(sink).edges(edges).build()).getMaxFlow();

            assertThat(ffFlow).as("Random trial %d: FF vs EK", t).isEqualTo(ekFlow);
            assertThat(ekFlow).as("Random trial %d: EK vs Dinic", t).isEqualTo(dinicFlow);
        }
    }

    // --- 2. MAX-FLOW / MIN-CUT EQUALITY CROSS-VALIDATION ---

    @Test
    @DisplayName("Cross-Validation: Max-Flow == Min-Cut Capacity")
    void crossValidateMaxFlowMinCut() {
        List<EdgeInputDto> edges = List.of(
                EdgeInputDto.builder().u(0).v(1).capacity(16.0).uName("S").vName("A").build(),
                EdgeInputDto.builder().u(0).v(2).capacity(13.0).uName("S").vName("B").build(),
                EdgeInputDto.builder().u(1).v(2).capacity(10.0).uName("A").vName("B").build(),
                EdgeInputDto.builder().u(1).v(3).capacity(12.0).uName("A").vName("C").build(),
                EdgeInputDto.builder().u(2).v(1).capacity(4.0).uName("B").vName("A").build(),
                EdgeInputDto.builder().u(2).v(4).capacity(14.0).uName("B").vName("D").build(),
                EdgeInputDto.builder().u(3).v(2).capacity(9.0).uName("C").vName("B").build(),
                EdgeInputDto.builder().u(3).v(5).capacity(20.0).uName("C").vName("T").build(),
                EdgeInputDto.builder().u(4).v(3).capacity(7.0).uName("D").vName("C").build(),
                EdgeInputDto.builder().u(4).v(5).capacity(4.0).uName("D").vName("T").build()
        );

        MaxFlowMinCutResult cutRes = maxFlowMinCut.execute(MaxFlowMinCutInput.builder().vertexCount(6).source(0).sink(5).edges(edges).build());
        double ekFlow = edmondsKarp.execute(EdmondsKarpInput.builder().vertexCount(6).source(0).sink(5).edges(edges).build()).getMaxFlow();

        assertThat(cutRes.getMaxFlow()).isEqualTo(ekFlow);
        assertThat(cutRes.getMinCutCapacity()).isEqualTo(ekFlow);
        assertThat(cutRes.isValuesEqual()).isTrue();
    }

    // --- 3. KÖNIG THEOREM & MATCHING VALIDITY ---

    @Test
    @DisplayName("Cross-Validation: König Theorem (Matching Size == Vertex Cover Size)")
    void crossValidateKonigTheorem() {
        List<String> trains = List.of("T1", "T2", "T3", "T4");
        List<String> platforms = List.of("P1", "P2", "P3", "P4");
        List<BipartiteEdgeDto> edges = List.of(
                BipartiteEdgeDto.builder().left("T1").right("P1").build(),
                BipartiteEdgeDto.builder().left("T1").right("P2").build(),
                BipartiteEdgeDto.builder().left("T2").right("P2").build(),
                BipartiteEdgeDto.builder().left("T3").right("P3").build()
        );

        BipartiteMatchingResult matchRes = bipartiteMatching.execute(BipartiteMatchingInput.builder().leftVertices(trains).rightVertices(platforms).edges(edges).build());
        KonigResult konigRes = konig.execute(KonigInput.builder().leftVertices(trains).rightVertices(platforms).edges(edges).build());

        assertThat(matchRes.getMatchingSize()).isEqualTo(konigRes.getMatchingSize());
        assertThat(konigRes.getMatchingSize()).isEqualTo(konigRes.getVertexCoverSize());
        assertThat(konigRes.isSizesEqual()).isTrue();

        // Matching validity checks
        Set<String> usedLeft = new HashSet<>();
        Set<String> usedRight = new HashSet<>();
        Set<String> edgeSet = new HashSet<>();
        for (BipartiteEdgeDto e : edges) {
            edgeSet.add(e.getLeft() + "->" + e.getRight());
        }

        for (MatchedPairDto pair : matchRes.getMatchedPairs()) {
            assertThat(usedLeft.add(pair.getLeft())).as("Left vertex %s matched at most once", pair.getLeft()).isTrue();
            assertThat(usedRight.add(pair.getRight())).as("Right vertex %s matched at most once", pair.getRight()).isTrue();
            assertThat(edgeSet.contains(pair.getLeft() + "->" + pair.getRight())).as("Pair (%s -> %s) exists in input graph", pair.getLeft(), pair.getRight()).isTrue();
        }
    }

    // --- 4. BRUTE-FORCE MATCHING REFERENCE CROSS-VALIDATION ---

    private int bruteForceMaxMatching(List<String> left, List<String> right, List<BipartiteEdgeDto> edges) {
        Map<String, Integer> lMap = new HashMap<>();
        for (int i = 0; i < left.size(); i++) lMap.put(left.get(i), i);
        Map<String, Integer> rMap = new HashMap<>();
        for (int j = 0; j < right.size(); j++) rMap.put(right.get(j), j);

        List<int[]> edgeList = new ArrayList<>();
        for (BipartiteEdgeDto e : edges) {
            edgeList.add(new int[]{lMap.get(e.getLeft()), rMap.get(e.getRight())});
        }

        int[] maxMatch = {0};
        boolean[] usedL = new boolean[left.size()];
        boolean[] usedR = new boolean[right.size()];
        backtrackMatching(edgeList, 0, 0, usedL, usedR, maxMatch);
        return maxMatch[0];
    }

    private void backtrackMatching(List<int[]> edgeList, int index, int currentCount, boolean[] usedL, boolean[] usedR, int[] maxMatch) {
        if (currentCount > maxMatch[0]) {
            maxMatch[0] = currentCount;
        }

        for (int i = index; i < edgeList.size(); i++) {
            int u = edgeList.get(i)[0];
            int v = edgeList.get(i)[1];
            if (!usedL[u] && !usedR[v]) {
                usedL[u] = true;
                usedR[v] = true;
                backtrackMatching(edgeList, i + 1, currentCount + 1, usedL, usedR, maxMatch);
                usedL[u] = false;
                usedR[v] = false;
            }
        }
    }

    @Test
    @DisplayName("Cross-Validation: Production Bipartite Matching == Brute-Force Reference")
    void crossValidateBipartiteMatchingBruteForce() {
        List<String> left = List.of("T1", "T2", "T3", "T4");
        List<String> right = List.of("P1", "P2", "P3", "P4");
        List<BipartiteEdgeDto> edges = List.of(
                BipartiteEdgeDto.builder().left("T1").right("P1").build(),
                BipartiteEdgeDto.builder().left("T1").right("P2").build(),
                BipartiteEdgeDto.builder().left("T2").right("P1").build(),
                BipartiteEdgeDto.builder().left("T2").right("P3").build(),
                BipartiteEdgeDto.builder().left("T3").right("P2").build(),
                BipartiteEdgeDto.builder().left("T4").right("P3").build(),
                BipartiteEdgeDto.builder().left("T4").right("P4").build()
        );

        int prodSize = bipartiteMatching.execute(BipartiteMatchingInput.builder().leftVertices(left).rightVertices(right).edges(edges).build()).getMatchingSize();
        int bruteSize = bruteForceMaxMatching(left, right, edges);

        assertThat(prodSize).isEqualTo(bruteSize);
    }
}
