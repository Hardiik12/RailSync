package com.railsync.algorithm.m4.edmondskarp.dto;

import com.railsync.algorithm.m4.common.dto.EdgeInputDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EdmondsKarpInput {
    private Integer vertexCount;
    private Integer source;
    private Integer sink;
    private List<EdgeInputDto> edges;
    private Boolean traceEnabled;
    private Integer maxTraceSteps;
}
