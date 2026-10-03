package com.railsync.algorithm.m5.sat;

import com.railsync.algorithm.m5.common.Literal;
import com.railsync.algorithm.m5.common.SATInput;
import com.railsync.algorithm.m5.common.SATResult;
import com.railsync.common.error.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SATAlgorithmTest {

    private SATAlgorithm satAlgorithm;
    private SATService satService;

    @BeforeEach
    void setUp() {
        satAlgorithm = new SATAlgorithm();
        satService = new SATService(satAlgorithm);
    }

    @Test
    void testSatisfiableFormula() {
        SATInput input = SATInput.builder()
                .variables(List.of("A", "B"))
                .stringClauses(List.of(
                        List.of("A", "B"),
                        List.of("!A", "B")
                ))
                .traceEnabled(true)
                .build();

        SATResult result = satService.execute(input);

        assertTrue(result.isSatisfiable());
        assertNotNull(result.getAssignment());
        assertTrue(result.getAssignment().get("B"));
        assertFalse(result.getTrace().isEmpty());
    }

    @Test
    void testUnsatisfiableFormula() {
        SATInput input = SATInput.builder()
                .variables(List.of("A"))
                .stringClauses(List.of(
                        List.of("A"),
                        List.of("!A")
                ))
                .build();

        SATResult result = satService.execute(input);

        assertFalse(result.isSatisfiable());
        assertNull(result.getAssignment());
    }

    @Test
    void testDeterministicResult() {
        SATInput input = SATInput.builder()
                .variables(List.of("X", "Y", "Z"))
                .stringClauses(List.of(
                        List.of("X", "Y"),
                        List.of("!X", "Z"),
                        List.of("!Y", "!Z")
                ))
                .build();

        SATResult res1 = satService.execute(input);
        SATResult res2 = satService.execute(input);

        assertEquals(res1.isSatisfiable(), res2.isSatisfiable());
        assertEquals(res1.getAssignment(), res2.getAssignment());
    }

    @Test
    void testInvalidEmptyInput() {
        SATInput input = SATInput.builder().stringClauses(List.of()).build();
        assertThrows(ApiException.class, () -> satService.execute(input));
    }

    @Test
    void testInputTooLarge() {
        List<String> vars = List.of(
                "V1", "V2", "V3", "V4", "V5", "V6", "V7", "V8", "V9", "V10",
                "V11", "V12", "V13", "V14", "V15", "V16", "V17", "V18", "V19", "V20", "V21"
        );
        SATInput input = SATInput.builder()
                .variables(vars)
                .stringClauses(List.of(List.of("V1")))
                .build();

        assertThrows(ApiException.class, () -> satService.execute(input));
    }
}
