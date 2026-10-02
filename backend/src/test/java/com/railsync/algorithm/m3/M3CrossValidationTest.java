package com.railsync.algorithm.m3;

import com.railsync.algorithm.m3.bitmask.BitmaskAlgorithm;
import com.railsync.algorithm.m3.bitmask.dto.BitmaskInput;
import com.railsync.algorithm.m3.bitmask.dto.BitmaskResult;
import com.railsync.algorithm.m3.damerau.DamerauLevenshteinAlgorithm;
import com.railsync.algorithm.m3.damerau.dto.DamerauLevenshteinInput;
import com.railsync.algorithm.m3.damerau.dto.DamerauLevenshteinResult;
import com.railsync.algorithm.m3.levenshtein.LevenshteinAlgorithm;
import com.railsync.algorithm.m3.levenshtein.dto.LevenshteinInput;
import com.railsync.algorithm.m3.levenshtein.dto.LevenshteinResult;
import com.railsync.algorithm.m3.matrixchain.MatrixChainAlgorithm;
import com.railsync.algorithm.m3.matrixchain.dto.MatrixChainInput;
import com.railsync.algorithm.m3.matrixchain.dto.MatrixChainResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class M3CrossValidationTest {

    private LevenshteinAlgorithm levenshteinAlgorithm;
    private DamerauLevenshteinAlgorithm damerauAlgorithm;
    private BitmaskAlgorithm bitmaskAlgorithm;
    private MatrixChainAlgorithm matrixChainAlgorithm;

    @BeforeEach
    void setUp() {
        levenshteinAlgorithm = new LevenshteinAlgorithm();
        damerauAlgorithm = new DamerauLevenshteinAlgorithm();
        bitmaskAlgorithm = new BitmaskAlgorithm();
        matrixChainAlgorithm = new MatrixChainAlgorithm();
    }

    // --- 1. DAMERAU CROSS-VALIDATION ---

    private int referenceRestrictedDamerau(String s, String t) {
        int n = s.length();
        int m = t.length();
        int[][] dp = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) dp[i][0] = i;
        for (int j = 0; j <= m; j++) dp[0][j] = j;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int cost = (s.charAt(i - 1) == t.charAt(j - 1)) ? 0 : 1;
                int val = Math.min(dp[i - 1][j] + 1, Math.min(dp[i][j - 1] + 1, dp[i - 1][j - 1] + cost));
                if (i > 1 && j > 1 && s.charAt(i - 1) == t.charAt(j - 2) && s.charAt(i - 2) == t.charAt(j - 1)) {
                    val = Math.min(val, dp[i - 2][j - 2] + 1);
                }
                dp[i][j] = val;
            }
        }
        return dp[n][m];
    }

    @ParameterizedTest
    @CsvSource({
            "AB, BA",
            "DELHI, DLEHI",
            "MUMBAI, MUMBAI",
            "EXPRESS, EXPRSS",
            "STATION, STTAION",
            "AAAA, AA",
            "ABCD, BADC",
            "🚉STATION, STATION🚉"
    })
    @DisplayName("Cross-Validation: Production Damerau == Independent Reference Damerau")
    void crossValidateDamerauWithReference(String source, String target) {
        DamerauLevenshteinResult prodResult = damerauAlgorithm.execute(
                DamerauLevenshteinInput.builder().source(source).target(target).build());
        int refDistance = referenceRestrictedDamerau(source, target);

        assertThat(prodResult.getDistance())
                .as("Production Damerau distance for '%s' -> '%s' should match reference", source, target)
                .isEqualTo(refDistance);
    }

    @Test
    @DisplayName("Cross-Validation: Seeded Random Strings Production Damerau == Reference")
    void crossValidateDamerauRandomStrings() {
        Random rand = new Random(42);
        String chars = "ABCDEXYZ🚉";

        for (int k = 0; k < 20; k++) {
            StringBuilder sBuilder = new StringBuilder();
            StringBuilder tBuilder = new StringBuilder();
            int lenS = 3 + rand.nextInt(5);
            int lenT = 3 + rand.nextInt(5);
            for (int i = 0; i < lenS; i++) sBuilder.append(chars.charAt(rand.nextInt(chars.length())));
            for (int i = 0; i < lenT; i++) tBuilder.append(chars.charAt(rand.nextInt(chars.length())));

            String s = sBuilder.toString();
            String t = tBuilder.toString();

            int prodDist = damerauAlgorithm.execute(DamerauLevenshteinInput.builder().source(s).target(t).build()).getDistance();
            int refDist = referenceRestrictedDamerau(s, t);

            assertThat(prodDist).as("Random test '%s' -> '%s'", s, t).isEqualTo(refDist);
        }
    }

    @ParameterizedTest
    @CsvSource({
            "NEW_DELHI, NEW_DELHI_JN",
            "AB, BA",
            "DELHI, DLEHI",
            "EXPRESS, EXPRSS",
            "STATION, STTAION"
    })
    @DisplayName("Cross-Validation: Damerau-Levenshtein distance <= Levenshtein distance")
    void crossValidateDamerauVsLevenshtein(String source, String target) {
        LevenshteinResult levRes = levenshteinAlgorithm.execute(LevenshteinInput.builder().source(source).target(target).build());
        DamerauLevenshteinResult damRes = damerauAlgorithm.execute(DamerauLevenshteinInput.builder().source(source).target(target).build());

        assertThat(damRes.getDistance())
                .as("Damerau distance for '%s' -> '%s' should be <= Levenshtein distance", source, target)
                .isLessThanOrEqualTo(levRes.getDistance());
    }

    // --- 2. BITMASK DP CROSS-VALIDATION ---

    private double bruteForceHamiltonianPathCost(int n, double[][] costMatrix, int startNode) {
        List<Integer> nodes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (i != startNode) nodes.add(i);
        }

        double[] minCost = {Double.POSITIVE_INFINITY};
        permute(nodes, 0, startNode, costMatrix, minCost);
        return minCost[0] < 1e8 ? minCost[0] : -1.0;
    }

    private void permute(List<Integer> nodes, int index, int startNode, double[][] costMatrix, double[] minCost) {
        if (index == nodes.size()) {
            double currentCost = 0.0;
            int prev = startNode;
            for (int curr : nodes) {
                double edge = costMatrix[prev][curr];
                if (edge < 0 || edge >= 1e8) return; // no valid edge
                currentCost += edge;
                prev = curr;
            }
            if (currentCost < minCost[0]) {
                minCost[0] = currentCost;
            }
            return;
        }

        for (int i = index; i < nodes.size(); i++) {
            Collections.swap(nodes, index, i);
            permute(nodes, index + 1, startNode, costMatrix, minCost);
            Collections.swap(nodes, index, i);
        }
    }

    @Test
    @DisplayName("Cross-Validation: Bitmask DP vs Brute-force Minimum Hamiltonian Path (N=4)")
    void crossValidateBitmaskSmallGraph() {
        int n = 4;
        double[][] cost = {
                {0, 10, 15, 20},
                {10, 0, 35, 25},
                {15, 35, 0, 30},
                {20, 25, 30, 0}
        };

        BitmaskResult res = bitmaskAlgorithm.execute(BitmaskInput.builder().nodeCount(n).costMatrix(cost).startNode(0).build());
        double refCost = bruteForceHamiltonianPathCost(n, cost, 0);

        assertThat(res.getOptimalCost()).isEqualTo(refCost);
        assertThat(res.getPathSequence()).hasSize(n);
        assertThat(res.getPathSequence().get(0)).isEqualTo(0);
        assertThat(res.getPathSequence()).containsExactlyInAnyOrder(0, 1, 2, 3);

        // Verify total sum of edge costs along reconstructed path matches optimal cost
        double pathCostSum = 0;
        for (int i = 0; i < n - 1; i++) {
            int u = res.getPathSequence().get(i);
            int v = res.getPathSequence().get(i + 1);
            pathCostSum += cost[u][v];
        }
        assertThat(pathCostSum).isEqualTo(res.getOptimalCost());
    }

    @Test
    @DisplayName("Cross-Validation: Bitmask DP vs Brute-force (N=5 Seeded Random Graph)")
    void crossValidateBitmaskRandomGraph() {
        int n = 5;
        Random rand = new Random(12345);
        double[][] cost = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                cost[i][j] = (i == j) ? 0 : 5 + rand.nextInt(45);
            }
        }

        BitmaskResult res = bitmaskAlgorithm.execute(BitmaskInput.builder().nodeCount(n).costMatrix(cost).startNode(0).build());
        double refCost = bruteForceHamiltonianPathCost(n, cost, 0);

        assertThat(res.getOptimalCost()).isEqualTo(refCost);
    }

    @Test
    @DisplayName("Cross-Validation: Bitmask DP Disconnected Graph")
    void crossValidateBitmaskDisconnectedGraph() {
        int n = 3;
        // Node 0 can reach 1, but 1 cannot reach 2 (no Hamiltonian path)
        double[][] cost = {
                {0, 10, -1},
                {-1, 0, -1},
                {-1, -1, 0}
        };

        BitmaskResult res = bitmaskAlgorithm.execute(BitmaskInput.builder().nodeCount(n).costMatrix(cost).startNode(0).build());
        double refCost = bruteForceHamiltonianPathCost(n, cost, 0);

        assertThat(res.getOptimalCost()).isEqualTo(-1.0);
        assertThat(refCost).isEqualTo(-1.0);
    }

    // --- 3. MATRIX CHAIN CROSS-VALIDATION ---

    private long naiveMatrixChain(int[] p, int i, int j) {
        if (i == j) return 0;
        long min = Long.MAX_VALUE;
        for (int k = i; k < j; k++) {
            long count = naiveMatrixChain(p, i, k) + naiveMatrixChain(p, k + 1, j) + (long) p[i - 1] * p[k] * p[j];
            if (count < min) min = count;
        }
        return min;
    }

    @Test
    @DisplayName("Cross-Validation: Matrix Chain DP == Naive Recursive Reference")
    void crossValidateMatrixChain() {
        int[] dims = {10, 20, 30, 40, 30};
        MatrixChainResult dpRes = matrixChainAlgorithm.execute(MatrixChainInput.builder().dimensions(dims).build());
        long naiveRes = naiveMatrixChain(dims, 1, dims.length - 1);

        assertThat(dpRes.getMinScalarMultiplications()).isEqualTo(naiveRes);
    }
}
