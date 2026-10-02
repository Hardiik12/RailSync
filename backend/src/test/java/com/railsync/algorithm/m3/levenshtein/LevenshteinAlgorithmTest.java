package com.railsync.algorithm.m3.levenshtein;

import com.railsync.algorithm.m3.levenshtein.dto.LevenshteinInput;
import com.railsync.algorithm.m3.levenshtein.dto.LevenshteinResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LevenshteinAlgorithmTest {

    private LevenshteinAlgorithm levenshteinAlgorithm;

    @BeforeEach
    void setUp() {
        levenshteinAlgorithm = new LevenshteinAlgorithm();
    }

    @Test
    @DisplayName("Identical strings - distance 0")
    void testIdenticalStrings() {
        LevenshteinResult result = levenshteinAlgorithm.execute(LevenshteinInput.builder()
                .source("NEW_DELHI")
                .target("NEW_DELHI")
                .build());

        assertThat(result.getDistance()).isEqualTo(0);
        assertThat(result.getEditOperations()).allMatch(op -> op.getType().equals("KEEP"));
    }

    @Test
    @DisplayName("Empty string cases")
    void testEmptyString() {
        LevenshteinResult r1 = levenshteinAlgorithm.execute(LevenshteinInput.builder().source("").target("DELHI").build());
        LevenshteinResult r2 = levenshteinAlgorithm.execute(LevenshteinInput.builder().source("DELHI").target("").build());

        assertThat(r1.getDistance()).isEqualTo(5);
        assertThat(r2.getDistance()).isEqualTo(5);
    }

    @Test
    @DisplayName("Single insertion, deletion, substitution")
    void testSingleEdits() {
        LevenshteinResult rIns = levenshteinAlgorithm.execute(LevenshteinInput.builder().source("cat").target("cats").build());
        LevenshteinResult rDel = levenshteinAlgorithm.execute(LevenshteinInput.builder().source("cats").target("cat").build());
        LevenshteinResult rSub = levenshteinAlgorithm.execute(LevenshteinInput.builder().source("cat").target("cut").build());

        assertThat(rIns.getDistance()).isEqualTo(1);
        assertThat(rDel.getDistance()).isEqualTo(1);
        assertThat(rSub.getDistance()).isEqualTo(1);
    }

    @Test
    @DisplayName("Multiple edits and railway station name correction")
    void testStationNameCorrection() {
        // Misspelled station name "NEW_DELI_JN" -> "NEW_DELHI_JN"
        LevenshteinResult result = levenshteinAlgorithm.execute(LevenshteinInput.builder()
                .source("NEW_DELI_JN")
                .target("NEW_DELHI_JN")
                .traceEnabled(true)
                .build());

        assertThat(result.getDistance()).isEqualTo(1); // Insert 'H'
        assertThat(result.getDpMatrix()).isNotNull();
        assertThat(result.getTrace()).isNotEmpty();
    }

    @Test
    @DisplayName("Unicode station strings")
    void testUnicodeStrings() {
        LevenshteinResult result = levenshteinAlgorithm.execute(LevenshteinInput.builder()
                .source("🚉NEW_DELHI")
                .target("🚉DELHI")
                .build());

        assertThat(result.getDistance()).isEqualTo(4); // "NEW_" removed
    }
}
