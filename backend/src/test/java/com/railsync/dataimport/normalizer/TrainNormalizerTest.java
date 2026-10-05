package com.railsync.dataimport.normalizer;

import com.railsync.dataimport.dto.RawTrainRecord;
import com.railsync.station.entity.Station;
import com.railsync.train.entity.Train;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TrainNormalizerTest {

    private TrainNormalizer normalizer;

    @BeforeEach
    void setUp() {
        normalizer = new TrainNormalizer();
    }

    @Test
    @DisplayName("Normalize raw train record into Train JPA entity")
    void testTrainNormalization() {
        RawTrainRecord raw = RawTrainRecord.builder()
                .trainNumber(" 12951 ")
                .trainName(" Rajdhani Express ")
                .trainType(" rajdhani ")
                .runningDays(List.of("MON", "TUE"))
                .distanceKm(1384.0)
                .build();

        Station src = Station.builder().stationCode("NDLS").name("New Delhi").build();
        Station dest = Station.builder().stationCode("CSMT").name("Mumbai CSMT").build();

        Train train = normalizer.normalize(raw, src, dest);

        assertThat(train.getTrainNumber()).isEqualTo("12951");
        assertThat(train.getTrainName()).isEqualTo("Rajdhani Express");
        assertThat(train.getTrainType()).isEqualTo("RAJDHANI");
        assertThat(train.getSourceStation().getStationCode()).isEqualTo("NDLS");
        assertThat(train.getDestinationStation().getStationCode()).isEqualTo("CSMT");
        assertThat(train.getRunningDays()).isEqualTo("MON,TUE");
        assertThat(train.getCapacity()).isEqualTo(500);
        assertThat(train.getStatus()).isEqualTo("ACTIVE");
    }
}
