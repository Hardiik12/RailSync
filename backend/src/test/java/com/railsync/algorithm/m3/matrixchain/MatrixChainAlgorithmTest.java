package com.railsync.algorithm.m3.matrixchain;

import com.railsync.algorithm.m3.matrixchain.dto.MatrixChainInput;
import com.railsync.algorithm.m3.matrixchain.dto.MatrixChainResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MatrixChainAlgorithmTest {

    private MatrixChainAlgorithm matrixChainAlgorithm;

    @BeforeEach
    void setUp() {
        matrixChainAlgorithm = new MatrixChainAlgorithm();
    }

    @Test
    @DisplayName("Textbook 6-matrix example: [30, 35, 15, 5, 10, 20, 25]")
    void testTextbookSixMatrixExample() {
        int[] dims = {30, 35, 15, 5, 10, 20, 25};
        MatrixChainResult result = matrixChainAlgorithm.execute(MatrixChainInput.builder()
                .dimensions(dims)
                .traceEnabled(true)
                .build());

        assertThat(result.getMatrixCount()).isEqualTo(6);
        assertThat(result.getMinScalarMultiplications()).isEqualTo(15125);
        assertThat(result.getOptimalParenthesization()).isEqualTo("((A1 x (A2 x A3)) x ((A4 x A5) x A6))");
        assertThat(result.getTrace()).isNotEmpty();
    }

    @Test
    @DisplayName("Single matrix: [10, 20]")
    void testSingleMatrix() {
        int[] dims = {10, 20};
        MatrixChainResult result = matrixChainAlgorithm.execute(MatrixChainInput.builder()
                .dimensions(dims)
                .build());

        assertThat(result.getMatrixCount()).isEqualTo(1);
        assertThat(result.getMinScalarMultiplications()).isEqualTo(0);
        assertThat(result.getOptimalParenthesization()).isEqualTo("A1");
    }

    @Test
    @DisplayName("Two matrices: [10, 20, 30]")
    void testTwoMatrices() {
        int[] dims = {10, 20, 30};
        MatrixChainResult result = matrixChainAlgorithm.execute(MatrixChainInput.builder()
                .dimensions(dims)
                .build());

        assertThat(result.getMatrixCount()).isEqualTo(2);
        assertThat(result.getMinScalarMultiplications()).isEqualTo(6000);
        assertThat(result.getOptimalParenthesization()).isEqualTo("(A1 x A2)");
    }
}
