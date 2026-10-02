package com.railsync.algorithm.m4;

import com.railsync.algorithm.m4.bipartitematching.BipartiteMatchingAlgorithm;
import com.railsync.algorithm.m4.bipartitematching.dto.BipartiteMatchingInput;
import com.railsync.algorithm.m4.bipartitematching.service.BipartiteMatchingService;
import com.railsync.algorithm.m4.fordfulkerson.FordFulkersonAlgorithm;
import com.railsync.algorithm.m4.fordfulkerson.dto.FordFulkersonInput;
import com.railsync.algorithm.m4.fordfulkerson.service.FordFulkersonService;
import com.railsync.common.error.ApiException;
import com.railsync.common.error.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class M4ServiceTest {

    private FordFulkersonService fordFulkersonService;
    private BipartiteMatchingService bipartiteMatchingService;

    @BeforeEach
    void setUp() {
        fordFulkersonService = new FordFulkersonService(new FordFulkersonAlgorithm());
        bipartiteMatchingService = new BipartiteMatchingService(new BipartiteMatchingAlgorithm());
    }

    @Test
    @DisplayName("Validation: Ford-Fulkerson vertexCount > 500 throws INPUT_TOO_LARGE")
    void testFordFulkersonVertexLimit() {
        FordFulkersonInput input = FordFulkersonInput.builder()
                .vertexCount(501)
                .source(0)
                .sink(500)
                .edges(Collections.emptyList())
                .build();

        assertThatThrownBy(() -> fordFulkersonService.execute(input))
                .isInstanceOf(ApiException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INPUT_TOO_LARGE);
    }

    @Test
    @DisplayName("Validation: Ford-Fulkerson source == sink throws INVALID_INPUT")
    void testFordFulkersonSameSourceSink() {
        FordFulkersonInput input = FordFulkersonInput.builder()
                .vertexCount(5)
                .source(2)
                .sink(2)
                .edges(Collections.emptyList())
                .build();

        assertThatThrownBy(() -> fordFulkersonService.execute(input))
                .isInstanceOf(ApiException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_INPUT);
    }

    @Test
    @DisplayName("Validation: Bipartite Matching overlapping vertex sets throws INVALID_INPUT")
    void testBipartiteOverlappingSets() {
        BipartiteMatchingInput input = BipartiteMatchingInput.builder()
                .leftVertices(List.of("A", "B"))
                .rightVertices(List.of("B", "C"))
                .edges(Collections.emptyList())
                .build();

        assertThatThrownBy(() -> bipartiteMatchingService.execute(input))
                .isInstanceOf(ApiException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_INPUT);
    }
}
