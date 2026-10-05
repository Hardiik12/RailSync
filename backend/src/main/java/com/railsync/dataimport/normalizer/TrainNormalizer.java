package com.railsync.dataimport.normalizer;

import com.railsync.dataimport.dto.RawTrainRecord;
import com.railsync.station.entity.Station;
import com.railsync.train.entity.Train;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrainNormalizer {

    public Train normalize(RawTrainRecord raw, Station sourceStation, Station destinationStation) {
        if (raw == null || sourceStation == null || destinationStation == null) {
            return null;
        }

        String trainNumber = raw.getTrainNumber() != null ? raw.getTrainNumber().trim().toUpperCase() : null;
        String trainName = raw.getTrainName() != null ? raw.getTrainName().trim() : "Express Service";
        String trainType = raw.getTrainType() != null ? raw.getTrainType().trim().toUpperCase() : "EXPRESS";

        String runningDaysStr = null;
        if (raw.getRunningDays() != null && !raw.getRunningDays().isEmpty()) {
            runningDaysStr = String.join(",", raw.getRunningDays().stream().map(String::trim).toList());
        }

        return Train.builder()
                .trainNumber(trainNumber)
                .trainName(trainName)
                .trainType(trainType)
                .sourceStation(sourceStation)
                .destinationStation(destinationStation)
                .capacity(500)
                .status("ACTIVE")
                .delayMinutes(0)
                .runningDays(runningDaysStr)
                .distanceKm(raw.getDistanceKm())
                .build();
    }
}
