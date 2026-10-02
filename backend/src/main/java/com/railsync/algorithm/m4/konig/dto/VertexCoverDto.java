package com.railsync.algorithm.m4.konig.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VertexCoverDto {
    private String name;
    private String partition; // "LEFT" or "RIGHT"
}
