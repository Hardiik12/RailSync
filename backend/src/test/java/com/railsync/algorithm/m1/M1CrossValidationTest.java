package com.railsync.algorithm.m1;

import com.railsync.algorithm.m1.kmp.KMPAlgorithm;
import com.railsync.algorithm.m1.kmp.dto.KMPInput;
import com.railsync.algorithm.m1.kmp.dto.KMPResult;
import com.railsync.algorithm.m1.rabinkarp.RabinKarpAlgorithm;
import com.railsync.algorithm.m1.rabinkarp.dto.RabinKarpInput;
import com.railsync.algorithm.m1.rabinkarp.dto.RabinKarpResult;
import com.railsync.algorithm.m1.zfunction.ZFunctionAlgorithm;
import com.railsync.algorithm.m1.zfunction.dto.ZFunctionInput;
import com.railsync.algorithm.m1.zfunction.dto.ZFunctionResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class M1CrossValidationTest {

    private KMPAlgorithm kmpAlgorithm;
    private ZFunctionAlgorithm zFunctionAlgorithm;
    private RabinKarpAlgorithm rabinKarpAlgorithm;

    @BeforeEach
    void setUp() {
        kmpAlgorithm = new KMPAlgorithm();
        zFunctionAlgorithm = new ZFunctionAlgorithm();
        rabinKarpAlgorithm = new RabinKarpAlgorithm();
    }

    static Stream<Arguments> provideSinglePatternTestCases() {
        return Stream.of(
                // 1. Standard railway text and pattern
                Arguments.of("NEW_DELHI_EXPRESS_PASSING_NEW_DELHI_JN", "NEW_DELHI"),
                // 2. Overlapping matches
                Arguments.of("ABABABA", "ABA"),
                // 3. All identical characters
                Arguments.of("AAAAAAA", "AA"),
                // 4. Repeated pattern
                Arguments.of("abcabcabcabc", "abc"),
                // 5. No match
                Arguments.of("RAILWAY_STATION_CENTRAL", "SHATABDI"),
                // 6. Single character match
                Arguments.of("X_MARKS_THE_SPOT_X", "X"),
                // 7. Unicode input
                Arguments.of("🚉NEW_DELHI_EXPRESS_🚉NEW_DELHI", "🚉NEW_DELHI"),
                // 8. Large synthetic input
                Arguments.of("A".repeat(500) + "B" + "A".repeat(500) + "B", "A".repeat(10) + "B")
        );
    }

    @ParameterizedTest(name = "Cross-validate pattern \"{1}\" in text \"{0}\"")
    @MethodSource("provideSinglePatternTestCases")
    @DisplayName("Cross-Validation: KMP == Z-Function == Rabin-Karp match results must be identical")
    void crossValidateSinglePatternSearch(String text, String pattern) {
        // Execute KMP
        KMPResult kmpResult = kmpAlgorithm.execute(KMPInput.builder()
                .text(text)
                .pattern(pattern)
                .build());

        // Execute Z-Function
        ZFunctionResult zResult = zFunctionAlgorithm.execute(ZFunctionInput.builder()
                .text(text)
                .pattern(pattern)
                .build());

        // Execute Rabin-Karp
        RabinKarpResult rkResult = rabinKarpAlgorithm.execute(RabinKarpInput.builder()
                .text(text)
                .pattern(pattern)
                .build());

        List<Integer> kmpMatches = kmpResult.getMatches();
        List<Integer> zMatches = zResult.getMatches();
        List<Integer> rkMatches = rkResult.getMatches();

        // Cross-validation assertion
        assertThat(kmpMatches)
                .as("KMP matches for text '%s' with pattern '%s'", text, pattern)
                .isEqualTo(zMatches);

        assertThat(zMatches)
                .as("Z-Function matches for text '%s' with pattern '%s'", text, pattern)
                .isEqualTo(rkMatches);

        assertThat(kmpMatches)
                .as("KMP vs Rabin-Karp matches for text '%s' with pattern '%s'", text, pattern)
                .isEqualTo(rkMatches);
    }
}
