package com.railsync.algorithm.m4.fordfulkerson;

import com.railsync.algorithm.m4.common.dto.EdgeInputDto;
import com.railsync.algorithm.m4.fordfulkerson.dto.FordFulkersonInput;
import com.railsync.algorithm.m4.fordfulkerson.dto.FordFulkersonResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FordFulkersonAlgorithmTest {

    private FordFulkersonAlgorithm fordFulkersonAlgorithm;

    @BeforeEach
    void setUp() {
        fordFulkersonAlgorithm = new FordFulkersonAlgorithm();
    }

    @Test
    @DisplayName("Ford-Fulkerson: Simple single edge graph")
    void testSingleEdgeGraph() {
        FordFulkersonInput input = FordFulkersonInput.builder()
                .vertexCount(2)
                .source(0)
                .sink(1)
                .edges(List.of(EdgeInputDto.builder().u(0).v(1).capacity(10.0).uName("Src").vName("Snk").build()))
                .build();

        FordFulkersonResult result = fordFulkersonAlgorithm.execute(input);
        assertThat(result.getMaxFlow()).isEqualTo(10.0);
    }

    @Test
    @DisplayName("Ford-Fulkerson: Textbook flow network graph")
    void testTextbookGraph() {
        // 0 -> 1 (c=10), 0 -> 2 (c=10), 1 -> 3 (c=4), 1 -> 4 (c=8), 2 -> 4 (c=9), 3 -> 5 (c=10), 4 -> 5 (c=10)
        List<EdgeInputDto> edges = List.of(
                EdgeInputDto.builder().u(0).v(1).capacity(10.0).uName("S").vName("A").build(),
                EdgeInputDto.builder().u(0).v(2).capacity(10.0).uName("S").vName("B").build(),
                EdgeInputDto.builder().u(1).v(3).capacity(4.0).uName("A").vName("C").build(),
                EdgeInputDto.builder().u(1).v(4).capacity(8.0).uName("A").vName("D").build(),
                EdgeInputDto.builder().u(2).v(4).capacity(9.0).uName("B").vName("D").build(),
                EdgeInputDto.builder().u(3).v(5).capacity(10.0).uName("C").vName("T").build(),
                EdgeInputDto.builder().u(4).v(5).capacity(10.0).uName("D").vName("T").build()
        );

        FordFulkersonInput input = FordFulkersonInput.builder()
                .vertexCount(6)
                .source(0)
                .sink(5)
                .edges(edges)
                .traceEnabled(true)
                .build();

        FordFulkersonResult result = fordFulkersonAlgorithm.execute(input);
        assertThat(result.getMaxFlow()).isEqualTo(14.0);
        assertThat(result.getAugmentingPaths()).isNotEmpty();
    }

    @Test
    @DisplayName("Ford-Fulkerson: Disconnected sink graph")
    void testDisconnectedGraph() {
        List<EdgeInputDto> edges = List.of(
                EdgeInputDto.builder().u(0).v(1).capacity(10.0).uName("S").vName("A").build()
        );

        FordFulkersonInput input = FordFulkersonInput.builder()
                .vertexCount(3)
                .source(0)
                .sink(2)
                .edges(edges)
                .build();

        FordFulkersonResult result = fordFulkersonAlgorithm.execute(input);
        assertThat(result.getMaxFlow()).isEqualTo(0.0);
    }
}
