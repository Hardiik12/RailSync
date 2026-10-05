package com.railsync.dataimport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RawStationRecord {
    private String stationCode;
    private String name;
    private String city;
    private String state;
    private Integer platformCount;
    private String status;
    private Double latitude;
    private Double longitude;
    private String sourceDataset;
}
