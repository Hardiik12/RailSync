package com.railsync.network.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BottleneckSegmentDto {
    private String fromStationCode;
    private String fromStationName;
    private String toStationCode;
    private String toStationName;
    private Double capacity;
    private Double flow;
    private Double distanceKm;
    private boolean isBottleneck;
}
