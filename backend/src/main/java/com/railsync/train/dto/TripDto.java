package com.railsync.train.dto;

import com.railsync.train.entity.Trip;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripDto {
    private Long id;
    private TrainDto train;
    private LocalDate serviceDate;
    private String scheduledStatus;
    private String actualStatus;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private List<StopTimeDto> stopTimes;

    public static TripDto fromEntity(Trip trip) {
        if (trip == null) return null;
        return TripDto.builder()
                .id(trip.getId())
                .train(TrainDto.fromEntity(trip.getTrain()))
                .serviceDate(trip.getServiceDate())
                .scheduledStatus(trip.getScheduledStatus())
                .actualStatus(trip.getActualStatus())
                .createdAt(trip.getCreatedAt())
                .updatedAt(trip.getUpdatedAt())
                .build();
    }

    public static TripDto fromEntityWithStops(Trip trip, List<StopTimeDto> stopTimes) {
        TripDto dto = fromEntity(trip);
        if (dto != null) {
            dto.setStopTimes(stopTimes);
        }
        return dto;
    }
}
