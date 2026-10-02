package com.railsync.algorithm.m3.bitmask;

import com.railsync.algorithm.m3.bitmask.dto.BitmaskInput;
import com.railsync.algorithm.m3.bitmask.service.BitmaskService;
import com.railsync.common.error.ApiException;
import com.railsync.common.error.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BitmaskServiceTest {

    private BitmaskService bitmaskService;

    @BeforeEach
    void setUp() {
        BitmaskAlgorithm bitmaskAlgorithm = new BitmaskAlgorithm();
        bitmaskService = new BitmaskService(bitmaskAlgorithm);
    }

    @Test
    @DisplayName("Validation: Null input throws INVALID_INPUT")
    void testNullInput() {
        assertThatThrownBy(() -> bitmaskService.execute(null))
                .isInstanceOf(ApiException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_INPUT);
    }

    @Test
    @DisplayName("Validation: nodeCount <= 0 throws INVALID_INPUT")
    void testInvalidNodeCount() {
        BitmaskInput input = BitmaskInput.builder().nodeCount(0).costMatrix(new double[1][1]).build();
        assertThatThrownBy(() -> bitmaskService.execute(input))
                .isInstanceOf(ApiException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_INPUT);
    }

    @Test
    @DisplayName("Validation: nodeCount > 16 throws INPUT_TOO_LARGE")
    void testNodeCountExceedsLimit() {
        BitmaskInput input = BitmaskInput.builder().nodeCount(17).costMatrix(new double[17][17]).build();
        assertThatThrownBy(() -> bitmaskService.execute(input))
                .isInstanceOf(ApiException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INPUT_TOO_LARGE);
    }

    @Test
    @DisplayName("Validation: startNode out of range throws INVALID_INPUT")
    void testStartNodeOutOfRange() {
        BitmaskInput input = BitmaskInput.builder().nodeCount(3).startNode(5).costMatrix(new double[3][3]).build();
        assertThatThrownBy(() -> bitmaskService.execute(input))
                .isInstanceOf(ApiException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_INPUT);
    }

    @Test
    @DisplayName("Validation: costMatrix null or insufficient rows throws INVALID_INPUT")
    void testCostMatrixInsufficientRows() {
        BitmaskInput input = BitmaskInput.builder().nodeCount(3).startNode(0).costMatrix(new double[2][3]).build();
        assertThatThrownBy(() -> bitmaskService.execute(input))
                .isInstanceOf(ApiException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_INPUT);
    }

    @Test
    @DisplayName("Validation: costMatrix row insufficient columns throws INVALID_INPUT")
    void testCostMatrixInsufficientCols() {
        double[][] cost = {
                {0, 1, 2},
                {1, 0},
                {2, 1, 0}
        };
        BitmaskInput input = BitmaskInput.builder().nodeCount(3).startNode(0).costMatrix(cost).build();
        assertThatThrownBy(() -> bitmaskService.execute(input))
                .isInstanceOf(ApiException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_INPUT);
    }
}
