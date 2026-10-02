package com.railsync.algorithm.m1.ahocorasick.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrieNodeDto {
    private int id;
    private char charLabel;
    private int parentId;
    private int failLink;
    private Map<String, Integer> transitions; // character -> target node ID
    private List<String> outputs;
    private boolean isRoot;
}
