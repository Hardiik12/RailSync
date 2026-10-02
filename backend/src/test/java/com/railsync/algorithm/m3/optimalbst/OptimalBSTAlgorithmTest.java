package com.railsync.algorithm.m3.optimalbst;

import com.railsync.algorithm.m3.optimalbst.dto.OptimalBSTInput;
import com.railsync.algorithm.m3.optimalbst.dto.OptimalBSTResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OptimalBSTAlgorithmTest {

    private OptimalBSTAlgorithm optimalBSTAlgorithm;

    @BeforeEach
    void setUp() {
        optimalBSTAlgorithm = new OptimalBSTAlgorithm();
    }

    @Test
    @DisplayName("Textbook 3-key example: keys ['A', 'B', 'C'], freqs [0.2, 0.5, 0.3]")
    void testTextbookThreeKeyExample() {
        String[] keys = {"A", "B", "C"};
        double[] freqs = {0.2, 0.5, 0.3};

        OptimalBSTResult result = optimalBSTAlgorithm.execute(OptimalBSTInput.builder()
                .keys(keys)
                .frequencies(freqs)
                .traceEnabled(true)
                .build());

        assertThat(result.getKeyCount()).isEqualTo(3);
        assertThat(result.getMinCost()).isGreaterThan(0.0);
        assertThat(result.getRootNode()).isNotNull();
        assertThat(result.getRootNode().getKey()).isEqualTo("B"); // 'B' should be root
    }

    @Test
    @DisplayName("Single key case")
    void testSingleKey() {
        String[] keys = {"NDLS"};
        double[] freqs = {1.0};

        OptimalBSTResult result = optimalBSTAlgorithm.execute(OptimalBSTInput.builder()
                .keys(keys)
                .frequencies(freqs)
                .build());

        assertThat(result.getMinCost()).isEqualTo(1.0);
        assertThat(result.getRootNode().getKey()).isEqualTo("NDLS");
    }
}
