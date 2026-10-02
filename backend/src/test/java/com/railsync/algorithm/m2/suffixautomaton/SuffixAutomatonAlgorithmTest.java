package com.railsync.algorithm.m2.suffixautomaton;

import com.railsync.algorithm.m2.suffixautomaton.dto.SuffixAutomatonInput;
import com.railsync.algorithm.m2.suffixautomaton.dto.SuffixAutomatonResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SuffixAutomatonAlgorithmTest {

    private SuffixAutomatonAlgorithm samAlgorithm;

    @BeforeEach
    void setUp() {
        samAlgorithm = new SuffixAutomatonAlgorithm();
    }

    @Test
    @DisplayName("Substring exists in text")
    void testSubstringExists() {
        String text = "NOTICE: Train 12951 Express departure from New Delhi";
        String query = "12951 Express";

        SuffixAutomatonResult result = samAlgorithm.execute(SuffixAutomatonInput.builder()
                .text(text)
                .query(query)
                .traceEnabled(true)
                .build());

        assertThat(result.isSubstringFound()).isTrue();
        assertThat(result.getFirstOccurrenceIndex()).isEqualTo(text.indexOf(query));
        assertThat(result.getAutomatonStates()).isNotEmpty();
    }

    @Test
    @DisplayName("Substring absent in text")
    void testSubstringAbsent() {
        String text = "NOTICE: Train 12951 Express departure";
        String query = "CANCELLED";

        SuffixAutomatonResult result = samAlgorithm.execute(SuffixAutomatonInput.builder()
                .text(text)
                .query(query)
                .build());

        assertThat(result.isSubstringFound()).isFalse();
        assertThat(result.getFirstOccurrenceIndex()).isEqualTo(-1);
    }

    @Test
    @DisplayName("Full string query match")
    void testFullStringMatch() {
        String text = "RAILWAY_INDEX";
        SuffixAutomatonResult result = samAlgorithm.execute(SuffixAutomatonInput.builder()
                .text(text)
                .query(text)
                .build());

        assertThat(result.isSubstringFound()).isTrue();
        assertThat(result.getFirstOccurrenceIndex()).isEqualTo(0);
    }

    @Test
    @DisplayName("Single character input and query")
    void testSingleCharacter() {
        SuffixAutomatonResult result = samAlgorithm.execute(SuffixAutomatonInput.builder()
                .text("A")
                .query("A")
                .build());

        assertThat(result.isSubstringFound()).isTrue();
        assertThat(result.getFirstOccurrenceIndex()).isEqualTo(0);
    }

    @Test
    @DisplayName("Empty input")
    void testEmptyInput() {
        SuffixAutomatonResult result = samAlgorithm.execute(SuffixAutomatonInput.builder()
                .text("")
                .query("A")
                .build());

        assertThat(result.isSubstringFound()).isFalse();
    }

    @Test
    @DisplayName("Clone creation case (e.g. 'abcbc')")
    void testCloneCreationCase() {
        String text = "abcbc";
        SuffixAutomatonResult result = samAlgorithm.execute(SuffixAutomatonInput.builder()
                .text(text)
                .query("bc")
                .traceEnabled(true)
                .build());

        assertThat(result.isSubstringFound()).isTrue();
        assertThat(result.getAutomatonStates().stream().anyMatch(s -> s.isClone())).isTrue();
    }

    @Test
    @DisplayName("Unicode string query search")
    void testUnicodeSearch() {
        String text = "🚉NEW_DELHI_JN_12951";
        String query = "12951";

        SuffixAutomatonResult result = samAlgorithm.execute(SuffixAutomatonInput.builder()
                .text(text)
                .query(query)
                .build());

        assertThat(result.isSubstringFound()).isTrue();
        assertThat(result.getFirstOccurrenceIndex()).isEqualTo(text.indexOf(query));
    }
}
