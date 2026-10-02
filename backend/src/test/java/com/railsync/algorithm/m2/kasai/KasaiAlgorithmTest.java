package com.railsync.algorithm.m2.kasai;

import com.railsync.algorithm.m2.kasai.dto.KasaiInput;
import com.railsync.algorithm.m2.kasai.dto.KasaiResult;
import com.railsync.algorithm.m2.suffixarray.SuffixArrayAlgorithmTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class KasaiAlgorithmTest {

    private KasaiAlgorithm kasaiAlgorithm;

    @BeforeEach
    void setUp() {
        kasaiAlgorithm = new KasaiAlgorithm();
    }

    // Naive reference LCP implementation for test validation ONLY
    public static int[] naiveLcpReference(String text, int[] sa) {
        if (text == null || text.isEmpty() || sa == null || sa.length == 0) return new int[0];
        int n = text.length();
        int[] lcp = new int[n];
        lcp[0] = 0;

        for (int i = 1; i < n; i++) {
            String s1 = text.substring(sa[i - 1]);
            String s2 = text.substring(sa[i]);
            int len = 0;
            while (len < s1.length() && len < s2.length() && s1.charAt(len) == s2.charAt(len)) {
                len++;
            }
            lcp[i] = len;
        }
        return lcp;
    }

    @Test
    @DisplayName("Normal railway text - match naive LCP reference")
    void testNormalRailwayText() {
        String text = "DELHI_EXPRESS_DELHI_JN";
        int[] sa = SuffixArrayAlgorithmTest.naiveSuffixArrayReference(text);

        KasaiResult result = kasaiAlgorithm.execute(KasaiInput.builder().text(text).suffixArray(sa).build());
        int[] expectedLcp = naiveLcpReference(text, sa);

        assertThat(result.getLcpArray()).isEqualTo(expectedLcp);
        assertThat(result.getLongestRepeatedSubstring()).isEqualTo("DELHI_");
    }

    @Test
    @DisplayName("Empty text")
    void testEmptyText() {
        KasaiResult result = kasaiAlgorithm.execute(KasaiInput.builder().text("").build());
        assertThat(result.getLcpArray()).isEmpty();
        assertThat(result.getLongestRepeatedSubstring()).isEmpty();
    }

    @Test
    @DisplayName("Single character")
    void testSingleCharacter() {
        KasaiResult result = kasaiAlgorithm.execute(KasaiInput.builder().text("A").build());
        assertThat(result.getLcpArray()).isEqualTo(new int[]{0});
    }

    @Test
    @DisplayName("Repeated characters ('AAAAA')")
    void testRepeatedCharacters() {
        String text = "AAAAA";
        int[] sa = SuffixArrayAlgorithmTest.naiveSuffixArrayReference(text);
        KasaiResult result = kasaiAlgorithm.execute(KasaiInput.builder().text(text).suffixArray(sa).build());
        int[] expectedLcp = naiveLcpReference(text, sa);

        assertThat(result.getLcpArray()).isEqualTo(expectedLcp);
        assertThat(result.getLongestRepeatedSubstring()).isEqualTo("AAAA");
    }

    @Test
    @DisplayName("Repeated substrings ('banana')")
    void testRepeatedSubstrings() {
        String text = "banana";
        int[] sa = SuffixArrayAlgorithmTest.naiveSuffixArrayReference(text);
        KasaiResult result = kasaiAlgorithm.execute(KasaiInput.builder().text(text).suffixArray(sa).build());
        int[] expectedLcp = naiveLcpReference(text, sa);

        assertThat(result.getLcpArray()).isEqualTo(expectedLcp);
        assertThat(result.getLongestRepeatedSubstring()).isEqualTo("ana");
    }

    @Test
    @DisplayName("Unicode text")
    void testUnicodeText() {
        String text = "🚉DELHI_EXPRESS_🚉DELHI";
        int[] sa = SuffixArrayAlgorithmTest.naiveSuffixArrayReference(text);
        KasaiResult result = kasaiAlgorithm.execute(KasaiInput.builder().text(text).suffixArray(sa).build());
        int[] expectedLcp = naiveLcpReference(text, sa);

        assertThat(result.getLcpArray()).isEqualTo(expectedLcp);
    }
}
