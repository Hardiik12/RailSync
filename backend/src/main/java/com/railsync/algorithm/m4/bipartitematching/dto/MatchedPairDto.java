package com.railsync.algorithm.m4.bipartitematching.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchedPairDto {
    private String left;
    private String right;
}
