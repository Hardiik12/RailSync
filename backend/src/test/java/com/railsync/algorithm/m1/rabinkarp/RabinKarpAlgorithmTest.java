package com.railsync.algorithm.m1.rabinkarp;

import com.railsync.algorithm.m1.rabinkarp.dto.RabinKarpInput;
import com.railsync.algorithm.m1.rabinkarp.dto.RabinKarpResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RabinKarpAlgorithmTest {

    private RabinKarpAlgorithm rabinKarpAlgorithm;

    @BeforeEach
    void setUp() {
        rabinKarpAlgorithm = new RabinKarpAlgorithm();
    }

    @Test
    @DisplayName("Normal match")
    void testNormalMatch() {
        RabinKarpInput input = RabinKarpInput.builder()
                .pattern("RAIL")
                .text("INDIAN_RAILWAYS_EXPRESS")
                .traceEnabled(true)
                .build();

        RabinKarpResult result = rabinKarpAlgorithm.execute(input);

        assertThat(result.getMatches()).containsExactly(7);
        assertThat(result.getMatchCount()).isEqualTo(1);
        assertThat(result.getHashVerifications()).isGreaterThanOrEqualTo(1);
        assertThat(result.getTrace()).isNotEmpty();
    }

    @Test
    @DisplayName("No match")
    void testNoMatch() {
        RabinKarpInput input = RabinKarpInput.builder()
                .pattern("SHATABDI")
                .text("RAILDISP_EXPRESS")
                .build();

        RabinKarpResult result = rabinKarpAlgorithm.execute(input);

        assertThat(result.getMatches()).isEmpty();
        assertThat(result.getMatchCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("Multiple matches")
    void testMultipleMatches() {
        RabinKarpInput input = RabinKarpInput.builder()
                .pattern("AB")
                .text("ABACABA")
                .build();

        RabinKarpResult result = rabinKarpAlgorithm.execute(input);

        assertThat(result.getMatches()).containsExactly(0, 4);
        assertThat(result.getMatchCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("Overlapping matches")
    void testOverlappingMatches() {
        RabinKarpInput input = RabinKarpInput.builder()
                .pattern("ABA")
                .text("ABABA")
                .build();

        RabinKarpResult result = rabinKarpAlgorithm.execute(input);

        assertThat(result.getMatches()).containsExactly(0, 2);
    }

    @Test
    @DisplayName("Repeated patterns")
    void testRepeatedPatterns() {
        RabinKarpInput input = RabinKarpInput.builder()
                .pattern("AA")
                .text("AAAAA")
                .build();

        RabinKarpResult result = rabinKarpAlgorithm.execute(input);

        assertThat(result.getMatches()).containsExactly(0, 1, 2, 3);
    }

    @Test
    @DisplayName("Collision requiring verification with actual small modulus")
    void testCollisionRequiringVerification() {
        // Using primeModulus = 13 and base = 256
        // With small modulus 13, hash collisions naturally occur for non-matching strings
        RabinKarpInput input = RabinKarpInput.builder()
                .pattern("AB")
                .text("ACADAB")
                .primeModulus(13)
                .traceEnabled(true)
                .build();

        RabinKarpResult result = rabinKarpAlgorithm.execute(input);

        assertThat(result.getMatches()).containsExactly(4); // "AB" is at index 4
        assertThat(result.getHashVerifications()).isGreaterThan(1);
        assertThat(result.getHashCollisions()).isGreaterThan(0); // Proves real collision occurred and was handled!
    }

    @Test
    @DisplayName("Pattern longer than text")
    void testPatternLongerThanText() {
        RabinKarpInput input = RabinKarpInput.builder()
                .pattern("LONGPATTERN")
                .text("SHORT")
                .build();

        RabinKarpResult result = rabinKarpAlgorithm.execute(input);

        assertThat(result.getMatches()).isEmpty();
    }

    @Test
    @DisplayName("Empty pattern")
    void testEmptyPattern() {
        RabinKarpInput input = RabinKarpInput.builder()
                .pattern("")
                .text("SOME_TEXT")
                .build();

        RabinKarpResult result = rabinKarpAlgorithm.execute(input);

        assertThat(result.getMatches()).isEmpty();
    }

    @Test
    @DisplayName("Unicode input")
    void testUnicodeInput() {
        RabinKarpInput input = RabinKarpInput.builder()
                .pattern("🚉MUMBAI")
                .text("EXPRESS_🚉MUMBAI_CENTRAL")
                .build();

        RabinKarpResult result = rabinKarpAlgorithm.execute(input);

        assertThat(result.getMatches()).containsExactly(8);
    }
}
