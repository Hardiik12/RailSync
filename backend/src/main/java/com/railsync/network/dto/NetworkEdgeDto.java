package com.railsync.network.dto;

import com.railsync.network.entity.NetworkEdge;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@Builder
public class NetworkEdgeDto {
    private final Long id;
    private final String fromStationCode;
    private final String fromStationName;
    private final String toStationCode;
    private final String toStationName;
    private final Double capacity;
    private final Double distanceKm;
    private final Integer travelTimeMinutes;
    private final String dataOrigin;
    private final String sourceDataset;
    private final OffsetDateTime createdAt;

    public static NetworkEdgeDto fromEntity(NetworkEdge entity) {
        if (entity == null) return null;
        return NetworkEdgeDto.builder()
                .id(entity.getId())
                .fromStationCode(entity.getFromStation() != null ? entity.getFromStation().getStationCode() : null)
                .fromStationName(entity.getFromStation() != null ? entity.getFromStation().getName() : null)
                .toStationCode(entity.getToStation() != null ? entity.getToStation().getStationCode() : null)
                .toStationName(entity.getToStation() != null ? entity.getToStation().getName() : null)
                .capacity(entity.getCapacity())
                .distanceKm(entity.getDistanceKm())
                .travelTimeMinutes(entity.getTravelTimeMinutes())
                .dataOrigin(entity.getDataOrigin())
                .sourceDataset(entity.getSourceDataset())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
