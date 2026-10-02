package com.railsync.algorithm.m3.damerau;

import com.railsync.algorithm.m3.damerau.dto.DamerauLevenshteinInput;
import com.railsync.algorithm.m3.damerau.dto.DamerauLevenshteinResult;
import com.railsync.algorithm.m3.levenshtein.LevenshteinAlgorithm;
import com.railsync.algorithm.m3.levenshtein.dto.LevenshteinInput;
import com.railsync.algorithm.m3.levenshtein.dto.LevenshteinResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DamerauLevenshteinAlgorithmTest {

    private DamerauLevenshteinAlgorithm damerauAlgorithm;
    private LevenshteinAlgorithm levenshteinAlgorithm;

    @BeforeEach
    void setUp() {
        damerauAlgorithm = new DamerauLevenshteinAlgorithm();
        levenshteinAlgorithm = new LevenshteinAlgorithm();
    }

    @Test
    @DisplayName("Adjacent transposition: 'AB' -> 'BA' (Damerau=1 vs Levenshtein=2)")
    void testAdjacentTransposition() {
        DamerauLevenshteinResult damerauRes = damerauAlgorithm.execute(DamerauLevenshteinInput.builder()
                .source("AB")
                .target("BA")
                .build());

        LevenshteinResult levenshteinRes = levenshteinAlgorithm.execute(LevenshteinInput.builder()
                .source("AB")
                .target("BA")
                .build());

        assertThat(damerauRes.getDistance()).isEqualTo(1);
        assertThat(levenshteinRes.getDistance()).isEqualTo(2);
        assertThat(damerauRes.getTranspositionCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("Station name transposition: 'DLEHI' -> 'DELHI'")
    void testStationNameTransposition() {
        DamerauLevenshteinResult result = damerauAlgorithm.execute(DamerauLevenshteinInput.builder()
                .source("DLEHI")
                .target("DELHI")
                .build());

        assertThat(result.getDistance()).isEqualTo(1);
        assertThat(result.getTranspositionCount()).isEqualTo(1);
    }
}
