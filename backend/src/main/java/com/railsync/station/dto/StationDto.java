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
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
