package com.railsync.dataimport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RawStopRecord {
    private String trainNumber;
    private String stationCode;
    private Integer sequenceOrder;
    private String arrivalTime;
    private String departureTime;
    private Double distanceKm;
}
