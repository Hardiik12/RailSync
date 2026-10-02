package com.railsync.algorithm.m2.sais;

import com.railsync.algorithm.m2.sais.dto.SAISInput;
import com.railsync.algorithm.m2.sais.dto.SAISResult;
import com.railsync.algorithm.m2.suffixarray.SuffixArrayAlgorithm;
import com.railsync.algorithm.m2.suffixarray.SuffixArrayAlgorithmTest;
import com.railsync.algorithm.m2.suffixarray.dto.SuffixArrayInput;
import com.railsync.algorithm.m2.suffixarray.dto.SuffixArrayResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class SAISAlgorithmTest {

    private SAISAlgorithm saisAlgorithm;
    private SuffixArrayAlgorithm suffixArrayAlgorithm;

    @BeforeEach
    void setUp() {
        saisAlgorithm = new SAISAlgorithm();
        suffixArrayAlgorithm = new SuffixArrayAlgorithm();
    }

    @Test
    @DisplayName("Cross-validation: SA-IS == SuffixArrayAlgorithm == Reference for railway text")
    void testRailwayTextCrossValidation() {
        String text = "DELHI_EXPRESS_SCHEDULE";

        SAISResult saisResult = saisAlgorithm.execute(SAISInput.builder().text(text).build());
        SuffixArrayResult saResult = suffixArrayAlgorithm.execute(SuffixArrayInput.builder().text(text).build());
        int[] expectedRef = SuffixArrayAlgorithmTest.naiveSuffixArrayReference(text);

        assertThat(saisResult.getSuffixArray()).isEqualTo(expectedRef);
        assertThat(saisResult.getSuffixArray()).isEqualTo(saResult.getSuffixArray());
    }

    @Test
    @DisplayName("Empty string")
    void testEmptyString() {
        SAISResult result = saisAlgorithm.execute(SAISInput.builder().text("").build());
        assertThat(result.getSuffixArray()).isEmpty();
    }

    @Test
    @DisplayName("Single character")
    void testSingleCharacter() {
        SAISResult result = saisAlgorithm.execute(SAISInput.builder().text("X").build());
        assertThat(result.getSuffixArray()).isEqualTo(new int[]{0});
    }

    @Test
    @DisplayName("Repeated characters")
    void testRepeatedCharacters() {
        String text = "AAAAAA";
        SAISResult result = saisAlgorithm.execute(SAISInput.builder().text(text).build());
        int[] expectedRef = SuffixArrayAlgorithmTest.naiveSuffixArrayReference(text);
        assertThat(result.getSuffixArray()).isEqualTo(expectedRef);
    }

    @Test
    @DisplayName("Repeated substrings")
    void testRepeatedSubstrings() {
        String text = "cacao_cocoa";
        SAISResult result = saisAlgorithm.execute(SAISInput.builder().text(text).build());
        int[] expectedRef = SuffixArrayAlgorithmTest.naiveSuffixArrayReference(text);
        assertThat(result.getSuffixArray()).isEqualTo(expectedRef);
    }

    @Test
    @DisplayName("Deterministic random strings with fixed seed")
    void testDeterministicRandomStrings() {
        Random random = new Random(42);
        char[] alphabet = "ABCDEFGH".toCharArray();

        for (int len : new int[]{10, 50, 100, 200}) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < len; i++) {
                sb.append(alphabet[random.nextInt(alphabet.length)]);
            }
            String randomText = sb.toString();

            SAISResult saisResult = saisAlgorithm.execute(SAISInput.builder().text(randomText).build());
            int[] expectedRef = SuffixArrayAlgorithmTest.naiveSuffixArrayReference(randomText);

            assertThat(saisResult.getSuffixArray())
                    .as("SA-IS output for random text length %d", len)
                    .isEqualTo(expectedRef);
        }
    }
}
