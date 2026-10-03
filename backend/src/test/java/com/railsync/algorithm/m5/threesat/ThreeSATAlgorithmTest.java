package com.railsync.algorithm.m5.threesat;

import com.railsync.algorithm.m5.common.SATInput;
import com.railsync.algorithm.m5.common.SATResult;
import com.railsync.algorithm.m5.sat.SATAlgorithm;
import com.railsync.common.error.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ThreeSATAlgorithmTest {

    private ThreeSATAlgorithm threeSATAlgorithm;
    private ThreeSATService threeSATService;

    @BeforeEach
    void setUp() {
        SATAlgorithm satAlgorithm = new SATAlgorithm();
        threeSATAlgorithm = new ThreeSATAlgorithm(satAlgorithm);
        threeSATService = new ThreeSATService(threeSATAlgorithm);
    }

    @Test
    void testValidThreeSATSatisfiable() {
        SATInput input = SATInput.builder()
                .variables(List.of("A", "B", "C"))
                .stringClauses(List.of(
                        List.of("A", "!B", "C"),
                        List.of("!A", "B", "C")
                ))
                .build();

        SATResult result = threeSATService.execute(input);

        assertTrue(result.isSatisfiable());
        assertNotNull(result.getAssignment());
        assertEquals("3-SAT", result.getAlgorithm());
    }

    @Test
    void testValidThreeSATUnsatisfiable() {
        SATInput input = SATInput.builder()
                .variables(List.of("A", "B", "C"))
                .stringClauses(List.of(
                        List.of("A", "B", "C"),
                        List.of("A", "B", "!C"),
                        List.of("A", "!B", "C"),
                        List.of("A", "!B", "!C"),
                        List.of("!A", "B", "C"),
                        List.of("!A", "B", "!C"),
                        List.of("!A", "!B", "C"),
                        List.of("!A", "!B", "!C")
                ))
                .build();

        SATResult result = threeSATService.execute(input);

        assertFalse(result.isSatisfiable());
    }

    @Test
    void testInvalidClauseSizeTwoLiterals() {
        SATInput input = SATInput.builder()
                .variables(List.of("A", "B", "C"))
                .stringClauses(List.of(
                        List.of("A", "B"), // Only 2 literals!
                        List.of("!A", "B", "C")
                ))
                .build();

        ApiException exception = assertThrows(ApiException.class, () -> threeSATService.execute(input));
        assertTrue(exception.getMessage().contains("exactly 3 literals"));
    }

    @Test
    void testInvalidClauseSizeFourLiterals() {
        SATInput input = SATInput.builder()
                .variables(List.of("A", "B", "C", "D"))
                .stringClauses(List.of(
                        List.of("A", "B", "C", "D") // 4 literals!
                ))
                .build();

        ApiException exception = assertThrows(ApiException.class, () -> threeSATService.execute(input));
        assertTrue(exception.getMessage().contains("exactly 3 literals"));
    }
}
