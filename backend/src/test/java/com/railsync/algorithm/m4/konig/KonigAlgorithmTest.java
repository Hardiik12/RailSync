package com.railsync.algorithm.m4.konig;

import com.railsync.algorithm.m4.bipartitematching.dto.BipartiteEdgeDto;
import com.railsync.algorithm.m4.konig.dto.KonigInput;
import com.railsync.algorithm.m4.konig.dto.KonigResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class KonigAlgorithmTest {

    private KonigAlgorithm konigAlgorithm;

    @BeforeEach
    void setUp() {
        konigAlgorithm = new KonigAlgorithm();
    }

    @Test
    @DisplayName("König's Theorem: Matching size == Vertex cover size")
    void testKonigTheorem() {
        List<String> trains = List.of("T1", "T2", "T3");
        List<String> platforms = List.of("P1", "P2", "P3");
        List<BipartiteEdgeDto> edges = List.of(
                BipartiteEdgeDto.builder().left("T1").right("P1").build(),
                BipartiteEdgeDto.builder().left("T1").right("P2").build(),
                BipartiteEdgeDto.builder().left("T2").right("P2").build(),
                BipartiteEdgeDto.builder().left("T3").right("P3").build()
        );

        KonigInput input = KonigInput.builder()
                .leftVertices(trains)
                .rightVertices(platforms)
                .edges(edges)
                .traceEnabled(true)
                .build();

        KonigResult result = konigAlgorithm.execute(input);
        assertThat(result.getMatchingSize()).isEqualTo(3);
        assertThat(result.getVertexCoverSize()).isEqualTo(3);
        assertThat(result.isSizesEqual()).isTrue();
    }
}
