package com.railsync.train.dto;

import com.railsync.train.entity.Route;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteDto {
    private Long id;
    private Long trainId;
    private String trainNumber;
    private String trainName;
    private String routeName;
    private Double distanceKm;
    private String status;
    @Builder.Default
    private List<RouteStopDto> stops = new ArrayList<>();
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public static RouteDto fromEntity(Route route) {
        if (route == null) return null;
        List<RouteStopDto> stopDtos = route.getStops() != null
                ? route.getStops().stream().map(RouteStopDto::fromEntity).toList()
                : new ArrayList<>();

        return RouteDto.builder()
                .id(route.getId())
                .trainId(route.getTrain() != null ? route.getTrain().getId() : null)
                .trainNumber(route.getTrain() != null ? route.getTrain().getTrainNumber() : null)
                .trainName(route.getTrain() != null ? route.getTrain().getTrainName() : null)
                .routeName(route.getRouteName())
                .distanceKm(route.getDistanceKm())
                .status(route.getStatus())
                .stops(stopDtos)
                .createdAt(route.getCreatedAt())
                .updatedAt(route.getUpdatedAt())
                .build();
    }
}
