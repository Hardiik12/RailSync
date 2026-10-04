package com.railsync.network.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateNetworkEdgeRequest {
    private String fromStationCode;
    private String toStationCode;
    private Double capacity;
    private Double distanceKm;
    private Integer travelTimeMinutes;
    private String dataOrigin;
    private String sourceDataset;
}
