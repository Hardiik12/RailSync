package com.railsync.train.dto;

import com.railsync.station.dto.StationDto;
import com.railsync.train.entity.Train;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainDto {
    private Long id;
    private String trainNumber;
    private String trainName;
    private String trainType;
    private StationDto sourceStation;
    private StationDto destinationStation;
    private Integer capacity;
    private String status;
    private StationDto currentStation;
    private Integer delayMinutes;
    private String runningDays;
    private Double distanceKm;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public static TrainDto fromEntity(Train train) {
        if (train == null) return null;
        return TrainDto.builder()
                .id(train.getId())
                .trainNumber(train.getTrainNumber())
                .trainName(train.getTrainName())
                .trainType(train.getTrainType())
                .sourceStation(StationDto.fromEntity(train.getSourceStation()))
                .destinationStation(StationDto.fromEntity(train.getDestinationStation()))
                .capacity(train.getCapacity())
                .status(train.getStatus())
                .currentStation(StationDto.fromEntity(train.getCurrentStation()))
                .delayMinutes(train.getDelayMinutes())
                .runningDays(train.getRunningDays())
                .distanceKm(train.getDistanceKm())
                .createdAt(train.getCreatedAt())
                .updatedAt(train.getUpdatedAt())
                .build();
    }
}
