package com.railsync.algorithm.m3.optimalbst.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BSTNodeDto {
    private String key;
    private double frequency;
    private BSTNodeDto left;
    private BSTNodeDto right;
}
