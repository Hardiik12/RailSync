package com.railsync.algorithm.m4.maxflowmincut.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CutEdgeDto {
    private int u;
    private int v;
    private String uName;
    private String vName;
    private double capacity;
    private double flow;
}
