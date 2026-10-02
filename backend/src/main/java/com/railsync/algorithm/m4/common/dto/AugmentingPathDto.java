package com.railsync.algorithm.m4.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AugmentingPathDto {
    private List<Integer> nodePath;
    private List<String> nodeNamePath;
    private double bottleneckCapacity;
    private double updatedMaxFlow;
}
