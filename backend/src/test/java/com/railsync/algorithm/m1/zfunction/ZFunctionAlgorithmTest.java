package com.railsync.algorithm.m1.zfunction;

import com.railsync.algorithm.m1.zfunction.dto.ZFunctionInput;
import com.railsync.algorithm.m1.zfunction.dto.ZFunctionResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ZFunctionAlgorithmTest {

    private ZFunctionAlgorithm zFunctionAlgorithm;

    @BeforeEach
    void setUp() {
        zFunctionAlgorithm = new ZFunctionAlgorithm();
    }

    @Test
    @DisplayName("Normal input - computes correct Z-array for string")
    void testNormalInputZArray() {
        // "aabzaab"
        // z[0]=0 (by convention in z-array output for string itself, though prefix len is 7, z[0]=0)
        // z[1]=1 ('a')
        // z[2]=0 ('b')
        // z[3]=0 ('z')
        // z[4]=3 ("aab")
        // z[5]=1 ('a')
        // z[6]=0 ('b')
        ZFunctionInput input = ZFunctionInput.builder()
                .text("aabzaab")
                .traceEnabled(true)
                .build();

        ZFunctionResult result = zFunctionAlgorithm.execute(input);

        assertThat(result.getZArray()).isEqualTo(new int[]{0, 1, 0, 0, 3, 1, 0});
        assertThat(result.getTrace()).isNotEmpty();
        assertThat(result.getComplexity().getTime()).isEqualTo("O(n)");
    }

    @Test
    @DisplayName("Pattern search with Z-Function")
    void testPatternSearch() {
        // pattern="AB", text="ABACABA"
        // processedString = "AB$ABACABA"
        // pattern matches at index 0 and index 4
        ZFunctionInput input = ZFunctionInput.builder()
                .pattern("AB")
                .text("ABACABA")
                .traceEnabled(true)
                .build();

        ZFunctionResult result = zFunctionAlgorithm.execute(input);

        assertThat(result.getMatches()).containsExactly(0, 4);
        assertThat(result.getMatchCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("Repeated patterns")
    void testRepeatedPatterns() {
        // "abcabcabc" -> Z-array: [0, 0, 0, 6, 0, 0, 3, 0, 0]
        ZFunctionInput input = ZFunctionInput.builder()
                .text("abcabcabc")
                .build();

        ZFunctionResult result = zFunctionAlgorithm.execute(input);

        assertThat(result.getZArray()).isEqualTo(new int[]{0, 0, 0, 6, 0, 0, 3, 0, 0});
    }

    @Test
    @DisplayName("No repeated prefix")
    void testNoRepeatedPrefix() {
        // "abcdef" -> Z-array: all zeros
        ZFunctionInput input = ZFunctionInput.builder()
                .text("abcdef")
                .build();

        ZFunctionResult result = zFunctionAlgorithm.execute(input);

        assertThat(result.getZArray()).isEqualTo(new int[]{0, 0, 0, 0, 0, 0});
    }

    @Test
    @DisplayName("All-identical characters")
    void testAllIdenticalCharacters() {
        // "aaaaa" -> Z-array: [0, 4, 3, 2, 1]
        ZFunctionInput input = ZFunctionInput.builder()
                .text("aaaaa")
                .build();

        ZFunctionResult result = zFunctionAlgorithm.execute(input);

        assertThat(result.getZArray()).isEqualTo(new int[]{0, 4, 3, 2, 1});
    }

    @Test
    @DisplayName("Single character input")
    void testSingleCharacter() {
        ZFunctionInput input = ZFunctionInput.builder()
                .text("x")
                .build();

        ZFunctionResult result = zFunctionAlgorithm.execute(input);

        assertThat(result.getZArray()).isEqualTo(new int[]{0});
        assertThat(result.getMatches()).isEmpty();
    }

    @Test
    @DisplayName("Empty input")
    void testEmptyInput() {
        ZFunctionInput input = ZFunctionInput.builder()
                .text("")
                .build();

        ZFunctionResult result = zFunctionAlgorithm.execute(input);

        assertThat(result.getZArray()).isEmpty();
        assertThat(result.getMatches()).isEmpty();
    }

    @Test
    @DisplayName("Unicode input - station names with UTF-8 characters")
    void testUnicodeInput() {
        // text: "NEW_DELHI_JN_NEW_DELHI"
        // pattern: "NEW_DELHI"
        ZFunctionInput input = ZFunctionInput.builder()
                .pattern("🚉NEW_DELHI")
                .text("🚉NEW_DELHI_EXPRESS_🚉NEW_DELHI")
                .build();

        ZFunctionResult result = zFunctionAlgorithm.execute(input);

        assertThat(result.getMatches()).containsExactly(0, 20);
    }
}
