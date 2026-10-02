package com.railsync.algorithm.m3.bitmask;

import com.railsync.algorithm.m3.bitmask.dto.BitmaskInput;
import com.railsync.algorithm.m3.bitmask.dto.BitmaskResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BitmaskAlgorithmTest {

    private BitmaskAlgorithm bitmaskAlgorithm;

    @BeforeEach
    void setUp() {
        bitmaskAlgorithm = new BitmaskAlgorithm();
    }

    @Test
    @DisplayName("Single node graph")
    void testSingleNodeGraph() {
        double[][] cost = {{0}};
        BitmaskResult result = bitmaskAlgorithm.execute(BitmaskInput.builder()
                .nodeCount(1)
                .costMatrix(cost)
                .build());

        assertThat(result.getOptimalCost()).isEqualTo(0.0);
        assertThat(result.getPathSequence()).containsExactly(0);
    }

    @Test
    @DisplayName("Two node graph")
    void testTwoNodeGraph() {
        double[][] cost = {
                {0, 10},
                {10, 0}
        };
        BitmaskResult result = bitmaskAlgorithm.execute(BitmaskInput.builder()
                .nodeCount(2)
                .costMatrix(cost)
                .startNode(0)
                .build());

        assertThat(result.getOptimalCost()).isEqualTo(10.0);
        assertThat(result.getPathSequence()).containsExactly(0, 1);
    }

    @Test
    @DisplayName("Four node small synthetic railway graph")
    void testFourNodeGraph() {
        // Nodes: 0: New Delhi, 1: Mathura, 2: Agra, 3: Kanpur
        double[][] cost = {
                {0, 10, 15, 20},
                {10, 0, 35, 25},
                {15, 35, 0, 30},
                {20, 25, 30, 0}
        };

        BitmaskResult result = bitmaskAlgorithm.execute(BitmaskInput.builder()
                .nodeCount(4)
                .costMatrix(cost)
                .startNode(0)
                .traceEnabled(true)
                .build());

        assertThat(result.getOptimalCost()).isGreaterThan(0);
        assertThat(result.getPathSequence()).hasSize(4);
        assertThat(result.getPathSequence().get(0)).isEqualTo(0);
    }
}
