package com.railsync.algorithm.m2;

import com.railsync.algorithm.m2.kasai.KasaiAlgorithm;
import com.railsync.algorithm.m2.kasai.KasaiAlgorithmTest;
import com.railsync.algorithm.m2.kasai.dto.KasaiInput;
import com.railsync.algorithm.m2.kasai.dto.KasaiResult;
import com.railsync.algorithm.m2.sais.SAISAlgorithm;
import com.railsync.algorithm.m2.sais.dto.SAISInput;
import com.railsync.algorithm.m2.sais.dto.SAISResult;
import com.railsync.algorithm.m2.suffixarray.SuffixArrayAlgorithm;
import com.railsync.algorithm.m2.suffixarray.SuffixArrayAlgorithmTest;
import com.railsync.algorithm.m2.suffixarray.dto.SuffixArrayInput;
import com.railsync.algorithm.m2.suffixarray.dto.SuffixArrayResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

public class M2CrossValidationTest {

    private SuffixArrayAlgorithm saAlgorithm;
    private SAISAlgorithm saisAlgorithm;
    private KasaiAlgorithm kasaiAlgorithm;

    @BeforeEach
    void setUp() {
        saAlgorithm = new SuffixArrayAlgorithm();
        saisAlgorithm = new SAISAlgorithm();
        kasaiAlgorithm = new KasaiAlgorithm();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "DELHI_EXPRESS",
            "INDIAN_RAILWAYS_PLATFORM_3",
            "banana",
            "abacaba",
            "AAAAAAA",
            "🚉NEW_DELHI_EXPRESS_🚉NEW_DELHI"
    })
    @DisplayName("Cross-Validation: SA-IS == SuffixArrayAlgorithm == Reference SA && Kasai LCP == Reference LCP")
    void crossValidateM2SuffixStructures(String text) {
        // 1. Compute Suffix Arrays
        SuffixArrayResult saResult = saAlgorithm.execute(SuffixArrayInput.builder().text(text).build());
        SAISResult saisResult = saisAlgorithm.execute(SAISInput.builder().text(text).build());
        int[] referenceSA = SuffixArrayAlgorithmTest.naiveSuffixArrayReference(text);

        // Assert Suffix Array cross-validation
        assertThat(saisResult.getSuffixArray())
                .as("SA-IS output vs Reference SA for text '%s'", text)
                .isEqualTo(referenceSA);

        assertThat(saResult.getSuffixArray())
                .as("SuffixArrayAlgorithm output vs Reference SA for text '%s'", text)
                .isEqualTo(referenceSA);

        assertThat(saisResult.getSuffixArray())
                .as("SA-IS output vs SuffixArrayAlgorithm for text '%s'", text)
                .isEqualTo(saResult.getSuffixArray());

        // 2. Compute Kasai LCP
        KasaiResult kasaiResult = kasaiAlgorithm.execute(KasaiInput.builder().text(text).suffixArray(saisResult.getSuffixArray()).build());
        int[] referenceLCP = KasaiAlgorithmTest.naiveLcpReference(text, saisResult.getSuffixArray());

        // Assert LCP cross-validation
        assertThat(kasaiResult.getLcpArray())
                .as("Kasai LCP vs Reference LCP for text '%s'", text)
                .isEqualTo(referenceLCP);
    }
}
