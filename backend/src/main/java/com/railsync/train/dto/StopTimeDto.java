package com.railsync.train.dto;

import com.railsync.station.dto.StationDto;
import com.railsync.train.entity.StopTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StopTimeDto {
    private Long id;
    private Long tripId;
    private StationDto station;
    private Integer stopSequence;
    private String scheduledArrival;
    private String scheduledDeparture;
    private String actualArrival;
    private String actualDeparture;
    private Integer arrivalDelayMinutes;
    private Integer departureDelayMinutes;

    public static StopTimeDto fromEntity(StopTime stopTime) {
        if (stopTime == null) return null;
        return StopTimeDto.builder()
                .id(stopTime.getId())
                .tripId(stopTime.getTrip() != null ? stopTime.getTrip().getId() : null)
                .station(StationDto.fromEntity(stopTime.getStation()))
                .stopSequence(stopTime.getStopSequence())
                .scheduledArrival(stopTime.getScheduledArrival())
                .scheduledDeparture(stopTime.getScheduledDeparture())
                .actualArrival(stopTime.getActualArrival())
                .actualDeparture(stopTime.getActualDeparture())
                .arrivalDelayMinutes(stopTime.getArrivalDelayMinutes())
                .departureDelayMinutes(stopTime.getDepartureDelayMinutes())
                .build();
    }
}
