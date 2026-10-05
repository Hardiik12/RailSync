package com.railsync.dataimport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RawTrainRecord {
    private String trainNumber;
    private String trainName;
    private String sourceStationCode;
    private String destinationStationCode;
    private String trainType;
    private List<String> runningDays;
    private Double distanceKm;
    private List<RawStopRecord> stops;
}
