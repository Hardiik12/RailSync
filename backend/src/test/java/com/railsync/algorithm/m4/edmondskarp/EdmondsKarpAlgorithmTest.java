package com.railsync.algorithm.m4.edmondskarp;

import com.railsync.algorithm.m4.common.dto.EdgeInputDto;
import com.railsync.algorithm.m4.edmondskarp.dto.EdmondsKarpInput;
import com.railsync.algorithm.m4.edmondskarp.dto.EdmondsKarpResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class EdmondsKarpAlgorithmTest {

    private EdmondsKarpAlgorithm edmondsKarpAlgorithm;

    @BeforeEach
    void setUp() {
        edmondsKarpAlgorithm = new EdmondsKarpAlgorithm();
    }

    @Test
    @DisplayName("Edmonds-Karp: Textbook flow network graph")
    void testTextbookGraph() {
        List<EdgeInputDto> edges = List.of(
                EdgeInputDto.builder().u(0).v(1).capacity(10.0).uName("S").vName("A").build(),
                EdgeInputDto.builder().u(0).v(2).capacity(10.0).uName("S").vName("B").build(),
                EdgeInputDto.builder().u(1).v(3).capacity(4.0).uName("A").vName("C").build(),
                EdgeInputDto.builder().u(1).v(4).capacity(8.0).uName("A").vName("D").build(),
                EdgeInputDto.builder().u(2).v(4).capacity(9.0).uName("B").vName("D").build(),
                EdgeInputDto.builder().u(3).v(5).capacity(10.0).uName("C").vName("T").build(),
                EdgeInputDto.builder().u(4).v(5).capacity(10.0).uName("D").vName("T").build()
        );

        EdmondsKarpInput input = EdmondsKarpInput.builder()
                .vertexCount(6)
                .source(0)
                .sink(5)
                .edges(edges)
                .traceEnabled(true)
                .build();

        EdmondsKarpResult result = edmondsKarpAlgorithm.execute(input);
        assertThat(result.getMaxFlow()).isEqualTo(14.0);
    }
}
