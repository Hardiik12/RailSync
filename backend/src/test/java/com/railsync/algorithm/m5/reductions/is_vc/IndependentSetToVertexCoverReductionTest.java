package com.railsync.algorithm.m5.reductions.is_vc;

import com.railsync.algorithm.m5.common.ISToVCInput;
import com.railsync.algorithm.m5.common.ISToVCResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IndependentSetToVertexCoverReductionTest {

    private IndependentSetToVertexCoverReduction reduction;
    private ISToVCService service;

    @BeforeEach
    void setUp() {
        reduction = new IndependentSetToVertexCoverReduction();
        service = new ISToVCService(reduction);
    }

    @Test
    void testReductionVerification() {
        List<String> vertices = List.of("A", "B", "C", "D", "E");
        List<List<String>> edges = List.of(
                List.of("A", "B"),
                List.of("B", "C"),
                List.of("C", "D"),
                List.of("D", "E")
        );
        List<String> independentSet = List.of("A", "C", "E");

        ISToVCInput input = ISToVCInput.builder()
                .vertices(vertices)
                .edges(edges)
                .independentSetSize(3)
                .independentSet(independentSet)
                .build();

        ISToVCResult result = service.execute(input);

        assertEquals("INDEPENDENT_SET", result.getSourceProblem());
        assertEquals("VERTEX_COVER", result.getTargetProblem());
        assertEquals(5, result.getVertexCount());
        assertEquals(3, result.getIndependentSetSize());
        assertEquals(2, result.getVertexCoverSize());
        assertTrue(result.isVerified());
        assertEquals(List.of("B", "D"), result.getVertexCover());
    }
}
