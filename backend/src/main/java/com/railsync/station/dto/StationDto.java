package com.railsync.station.dto;

import com.railsync.station.entity.Station;
import lombok.Builder;
import lombok.Getter;
import java.time.OffsetDateTime;

@Getter @Builder
public class StationDto {
    private final Long id;
    private final String stationCode;
    private final String name;
    private final String city;
    private final String state;
    private final Double latitude;
    private final Double longitude;
    private final String zone;
    private final String address;
    private final Integer platformCount;
    private final String status;
    private final String dataOrigin;
    private final String sourceDataset;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime updatedAt;

    public static StationDto fromEntity(Station e) {
        if (e == null) return null;
        return StationDto.builder().id(e.getId()).stationCode(e.getStationCode()).name(e.getName())
                .city(e.getCity()).state(e.getState()).latitude(e.getLatitude()).longitude(e.getLongitude())
                .zone(e.getZone()).address(e.getAddress()).platformCount(e.getPlatformCount())
                .status(e.getStatus()).dataOrigin(e.getDataOrigin()).sourceDataset(e.getSourceDataset())
                .createdAt(e.getCreatedAt()).updatedAt(e.getUpdatedAt()).build();
    }
}