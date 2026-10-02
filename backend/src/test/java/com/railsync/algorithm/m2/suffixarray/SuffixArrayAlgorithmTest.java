package com.railsync.algorithm.m2.suffixarray;

import com.railsync.algorithm.m2.suffixarray.dto.SuffixArrayInput;
import com.railsync.algorithm.m2.suffixarray.dto.SuffixArrayResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;

public class SuffixArrayAlgorithmTest {

    private SuffixArrayAlgorithm suffixArrayAlgorithm;

    @BeforeEach
    void setUp() {
        suffixArrayAlgorithm = new SuffixArrayAlgorithm();
    }

    // Test-only naive reference suffix array implementation (Sorting suffixes naively)
    public static int[] naiveSuffixArrayReference(String text) {
        if (text == null || text.isEmpty()) return new int[0];
        int n = text.length();
        Integer[] indices = new Integer[n];
        for (int i = 0; i < n; i++) indices[i] = i;

        Arrays.sort(indices, Comparator.comparing(i -> text.substring(i)));

        int[] sa = new int[n];
        for (int i = 0; i < n; i++) sa[i] = indices[i];
        return sa;
    }

    @Test
    @DisplayName("Normal railway text - match reference")
    void testNormalRailwayText() {
        String text = "DELHI_EXPRESS";
        SuffixArrayResult result = suffixArrayAlgorithm.execute(SuffixArrayInput.builder().text(text).build());
        int[] expected = naiveSuffixArrayReference(text);

        assertThat(result.getSuffixArray()).isEqualTo(expected);
    }

    @Test
    @DisplayName("Empty string")
    void testEmptyString() {
        SuffixArrayResult result = suffixArrayAlgorithm.execute(SuffixArrayInput.builder().text("").build());
        assertThat(result.getSuffixArray()).isEmpty();
    }

    @Test
    @DisplayName("Single character")
    void testSingleCharacter() {
        SuffixArrayResult result = suffixArrayAlgorithm.execute(SuffixArrayInput.builder().text("A").build());
        assertThat(result.getSuffixArray()).isEqualTo(new int[]{0});
    }

    @Test
    @DisplayName("Repeated characters")
    void testRepeatedCharacters() {
        String text = "AAAAA";
        SuffixArrayResult result = suffixArrayAlgorithm.execute(SuffixArrayInput.builder().text(text).build());
        int[] expected = naiveSuffixArrayReference(text);
        assertThat(result.getSuffixArray()).isEqualTo(expected);
    }

    @Test
    @DisplayName("Repeated substrings")
    void testRepeatedSubstrings() {
        String text = "banana";
        SuffixArrayResult result = suffixArrayAlgorithm.execute(SuffixArrayInput.builder().text(text).build());
        int[] expected = naiveSuffixArrayReference(text);
        assertThat(result.getSuffixArray()).isEqualTo(expected);
    }

    @Test
    @DisplayName("Unicode string")
    void testUnicodeString() {
        String text = "🚉DELHI_🚉MUMBAI";
        SuffixArrayResult result = suffixArrayAlgorithm.execute(SuffixArrayInput.builder().text(text).build());
        int[] expected = naiveSuffixArrayReference(text);
        assertThat(result.getSuffixArray()).isEqualTo(expected);
    }

    @Test
    @DisplayName("Strings containing spaces")
    void testStringsWithSpaces() {
        String text = "Platform 3 Express Arrival";
        SuffixArrayResult result = suffixArrayAlgorithm.execute(SuffixArrayInput.builder().text(text).build());
        int[] expected = naiveSuffixArrayReference(text);
        assertThat(result.getSuffixArray()).isEqualTo(expected);
    }

    @Test
    @DisplayName("Already ordered and reverse ordered patterns")
    void testOrderedAndReverseOrdered() {
        String text1 = "ABCDEFGHIJKLMNOP";
        String text2 = "PONMLKJIHGFEDCBA";

        SuffixArrayResult r1 = suffixArrayAlgorithm.execute(SuffixArrayInput.builder().text(text1).build());
        SuffixArrayResult r2 = suffixArrayAlgorithm.execute(SuffixArrayInput.builder().text(text2).build());

        assertThat(r1.getSuffixArray()).isEqualTo(naiveSuffixArrayReference(text1));
        assertThat(r2.getSuffixArray()).isEqualTo(naiveSuffixArrayReference(text2));
    }
}
