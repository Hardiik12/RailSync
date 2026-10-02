package com.railsync.algorithm.m1.kmp;

import com.railsync.algorithm.m1.kmp.dto.KMPInput;
import com.railsync.algorithm.m1.kmp.dto.KMPResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class KMPAlgorithmTest {

    private KMPAlgorithm kmpAlgorithm;

    @BeforeEach
    void setUp() {
        kmpAlgorithm = new KMPAlgorithm();
    }

    @Test
    @DisplayName("Should find exact match when pattern exists in railway text")
    void shouldFindExactMatchInRailwayText() {
        KMPInput input = KMPInput.builder()
                .text("Kondapuram Express departs from Vijayawada Junction platform 4")
                .pattern("Vijayawada")
                .traceEnabled(true)
                .build();

        KMPResult result = kmpAlgorithm.execute(input);

        assertThat(result.getMatchCount()).isEqualTo(1);
        assertThat(result.getMatches()).containsExactly(32);
        assertThat(result.getComparisons()).isGreaterThan(0);
        assertThat(result.getTrace()).isNotEmpty();
    }

    @Test
    @DisplayName("Should return empty matches when pattern does not exist")
    void shouldReturnEmptyMatchesWhenPatternNotFound() {
        KMPInput input = KMPInput.builder()
                .text("Secunderabad Junction station manager office")
                .pattern("Chennai")
                .build();

        KMPResult result = kmpAlgorithm.execute(input);

        assertThat(result.getMatchCount()).isEqualTo(0);
        assertThat(result.getMatches()).isEmpty();
    }

    @Test
    @DisplayName("Should find pattern at beginning of text")
    void shouldFindPatternAtBeginning() {
        KMPInput input = KMPInput.builder()
                .text("Vijayawada Junction is a major railway hub")
                .pattern("Vijayawada")
                .build();

        KMPResult result = kmpAlgorithm.execute(input);

        assertThat(result.getMatchCount()).isEqualTo(1);
        assertThat(result.getMatches()).containsExactly(0);
    }

    @Test
    @DisplayName("Should find pattern at end of text")
    void shouldFindPatternAtEnd() {
        KMPInput input = KMPInput.builder()
                .text("Train 12727 arrives at Howrah")
                .pattern("Howrah")
                .build();

        KMPResult result = kmpAlgorithm.execute(input);

        assertThat(result.getMatchCount()).isEqualTo(1);
        assertThat(result.getMatches()).containsExactly(23);
    }

    @Test
    @DisplayName("Should find overlapping matches when pattern repeats")
    void shouldFindOverlappingMatches() {
        KMPInput input = KMPInput.builder()
                .text("AAAAA")
                .pattern("AAA")
                .traceEnabled(true)
                .build();

        KMPResult result = kmpAlgorithm.execute(input);

        assertThat(result.getMatchCount()).isEqualTo(3);
        assertThat(result.getMatches()).containsExactly(0, 1, 2);
    }

    @Test
    @DisplayName("Should handle pattern longer than text")
    void shouldHandlePatternLongerThanText() {
        KMPInput input = KMPInput.builder()
                .text("BZA")
                .pattern("Vijayawada")
                .build();

        KMPResult result = kmpAlgorithm.execute(input);

        assertThat(result.getMatchCount()).isEqualTo(0);
        assertThat(result.getMatches()).isEmpty();
    }

    @Test
    @DisplayName("Should handle empty pattern gracefully")
    void shouldHandleEmptyPattern() {
        KMPInput input = KMPInput.builder()
                .text("Secunderabad")
                .pattern("")
                .build();

        KMPResult result = kmpAlgorithm.execute(input);

        assertThat(result.getMatchCount()).isEqualTo(0);
        assertThat(result.getMatches()).isEmpty();
    }

    @Test
    @DisplayName("Should compute LPS array correctly for classic pattern")
    void shouldComputeLPSArrayCorrectly() {
        int[] lps = kmpAlgorithm.computeLPSArray("ABABCABAB", new ArrayList<>(), false, 500);

        // A B A B C A B A B
        // 0 0 1 2 0 1 2 3 4
        assertThat(lps).containsExactly(0, 0, 1, 2, 0, 1, 2, 3, 4);
    }
}
