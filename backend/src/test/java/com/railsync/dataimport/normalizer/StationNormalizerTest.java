package com.railsync.dataimport.normalizer;

import com.railsync.dataimport.dto.RawStationRecord;
import com.railsync.station.entity.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StationNormalizerTest {

    private StationNormalizer normalizer;

    @BeforeEach
    void setUp() {
        normalizer = new StationNormalizer();
    }

    @Test
    @DisplayName("Normalize uppercase code, whitespace trimming, and defaults")
    void testNormalizerDefaults() {
        RawStationRecord raw = RawStationRecord.builder()
                .stationCode(" ndls ")
                .name(" New Delhi ")
                .city("")
                .state("")
                .latitude(28.6430)
                .longitude(77.2197)
                .build();

        Station station = normalizer.normalize(raw, "TEST_SNAPSHOT");

        assertThat(station.getStationCode()).isEqualTo("NDLS");
        assertThat(station.getName()).isEqualTo("New Delhi");
        assertThat(station.getCity()).isEqualTo("New Delhi");
        assertThat(station.getState()).isEqualTo("UNKNOWN");
        assertThat(station.getStatus()).isEqualTo("ACTIVE");
        assertThat(station.getPlatformCount()).isEqualTo(1);
        assertThat(station.getDataOrigin()).isEqualTo("PUBLIC_DATA");
        assertThat(station.getSourceDataset()).isEqualTo("TEST_SNAPSHOT");
    }
}
