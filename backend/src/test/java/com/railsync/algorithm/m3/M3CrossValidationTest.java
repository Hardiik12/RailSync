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

    // Naive recursive reference for Matrix Chain
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
