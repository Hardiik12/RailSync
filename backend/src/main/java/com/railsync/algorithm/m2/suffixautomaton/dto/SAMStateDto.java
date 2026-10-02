package com.railsync.algorithm.m2.suffixautomaton.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SAMStateDto {
    private int id;
    private int len;
    private int link;
    private boolean isClone;
    private int firstPos;
    private Map<String, Integer> transitions;
}
