package com.railsync.algorithm.m4.bipartitematching;

import com.railsync.algorithm.m4.bipartitematching.dto.BipartiteEdgeDto;
import com.railsync.algorithm.m4.bipartitematching.dto.BipartiteMatchingInput;
import com.railsync.algorithm.m4.bipartitematching.dto.BipartiteMatchingResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BipartiteMatchingAlgorithmTest {

    private BipartiteMatchingAlgorithm bipartiteMatchingAlgorithm;

    @BeforeEach
    void setUp() {
        bipartiteMatchingAlgorithm = new BipartiteMatchingAlgorithm();
    }

    @Test
    @DisplayName("Bipartite Matching: Simple train to platform matching")
    void testTrainPlatformMatching() {
        List<String> trains = List.of("Train_101", "Train_102", "Train_103");
        List<String> platforms = List.of("Platform_1", "Platform_2", "Platform_3");
        List<BipartiteEdgeDto> edges = List.of(
                BipartiteEdgeDto.builder().left("Train_101").right("Platform_1").build(),
                BipartiteEdgeDto.builder().left("Train_101").right("Platform_2").build(),
                BipartiteEdgeDto.builder().left("Train_102").right("Platform_2").build(),
                BipartiteEdgeDto.builder().left("Train_103").right("Platform_3").build()
        );

        BipartiteMatchingInput input = BipartiteMatchingInput.builder()
                .leftVertices(trains)
                .rightVertices(platforms)
                .edges(edges)
                .traceEnabled(true)
                .build();

        BipartiteMatchingResult result = bipartiteMatchingAlgorithm.execute(input);
        assertThat(result.getMatchingSize()).isEqualTo(3);
        assertThat(result.getMatchedPairs()).hasSize(3);
    }
}
