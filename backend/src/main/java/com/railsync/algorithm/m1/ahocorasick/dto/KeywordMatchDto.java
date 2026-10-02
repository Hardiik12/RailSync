package com.railsync.algorithm.m1.ahocorasick.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeywordMatchDto {
    private String keyword;
    private int startIndex;
    private int endIndex;
}
