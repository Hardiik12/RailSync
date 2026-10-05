package com.railsync.train.dto;

import com.railsync.station.dto.StationDto;
import com.railsync.train.entity.RouteStop;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteStopDto {
    private Long id;
    private Long routeId;
    private StationDto station;
    private Integer stopSequence;
    private Double distanceFromOrigin;
    private Integer scheduledDwellMinutes;
    private String scheduledArrival;
    private String scheduledDeparture;

    public static RouteStopDto fromEntity(RouteStop stop) {
        if (stop == null) return null;
        return RouteStopDto.builder()
                .id(stop.getId())
                .routeId(stop.getRoute() != null ? stop.getRoute().getId() : null)
                .station(StationDto.fromEntity(stop.getStation()))
                .stopSequence(stop.getStopSequence())
                .distanceFromOrigin(stop.getDistanceFromOrigin())
                .scheduledDwellMinutes(stop.getScheduledDwellMinutes())
                .scheduledArrival(stop.getScheduledArrival())
                .scheduledDeparture(stop.getScheduledDeparture())
                .build();
    }
}
