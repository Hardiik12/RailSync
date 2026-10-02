package com.railsync.algorithm.m4.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlowEdgeDto {
    private int u;
    private int v;
    private String uName;
    private String vName;
    private double capacity;
    private double flow;
    private double residualCapacity;
}
