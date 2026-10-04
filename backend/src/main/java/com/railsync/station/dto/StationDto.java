package com.railsync.station.dto;

import com.railsync.station.entity.Station;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@Builder
public class StationDto {

    private final Long id;
    private final String stationCode;
    private final String name;
    private final String city;
    private final String state;
    private final Integer platformCount;
    private final String status;
    private final String dataOrigin;
    private final String sourceDataset;
    private final Double latitude;
    private final Double longitude;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime updatedAt;

    public static StationDto fromEntity(Station entity) {
        if (entity == null) return null;
        return StationDto.builder()
                .id(entity.getId())
                .stationCode(entity.getStationCode())
                .name(entity.getName())
                .city(entity.getCity())
                .state(entity.getState())
                .platformCount(entity.getPlatformCount())
                .status(entity.getStatus())
                .dataOrigin(entity.getDataOrigin())
                .sourceDataset(entity.getSourceDataset())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
