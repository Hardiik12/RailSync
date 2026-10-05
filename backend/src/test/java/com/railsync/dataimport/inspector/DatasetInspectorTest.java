package com.railsync.dataimport.inspector;

import com.railsync.dataimport.dto.InspectionReport;
import com.railsync.dataimport.dto.RawStationRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DatasetInspectorTest {

    private DatasetInspector inspector;

    @BeforeEach
    void setUp() {
        inspector = new DatasetInspector();
    }

    @Test
    @DisplayName("Inspect valid records with correct counts and zero errors")
    void testValidRecords() {
        List<RawStationRecord> records = List.of(
                RawStationRecord.builder().stationCode("NDLS").name("New Delhi").latitude(28.6430).longitude(77.2197).build(),
                RawStationRecord.builder().stationCode("CSMT").name("Mumbai CSMT").latitude(18.9398).longitude(72.8347).build()
        );

        InspectionReport report = inspector.inspectStations(records);

        assertThat(report.getTotalRecords()).isEqualTo(2);
        assertThat(report.getValidRecords()).isEqualTo(2);
        assertThat(report.getDuplicateIdentifiers()).isEqualTo(0);
        assertThat(report.getMissingRequiredFields()).isEqualTo(0);
        assertThat(report.getInvalidCoordinates()).isEqualTo(0);
    }

    @Test
    @DisplayName("Detect duplicate station codes and invalid coordinates")
    void testDuplicatesAndInvalidCoordinates() {
        List<RawStationRecord> records = List.of(
                RawStationRecord.builder().stationCode("NDLS").name("New Delhi").latitude(28.6430).longitude(77.2197).build(),
                RawStationRecord.builder().stationCode("NDLS").name("Duplicate NDLS").latitude(28.6430).longitude(77.2197).build(),
                RawStationRecord.builder().stationCode("BAD_COORD").name("Bad Station").latitude(120.0).longitude(77.2197).build(),
                RawStationRecord.builder().stationCode("").name("Empty Code").build()
        );

        InspectionReport report = inspector.inspectStations(records);

        assertThat(report.getTotalRecords()).isEqualTo(4);
        assertThat(report.getValidRecords()).isEqualTo(1);
        assertThat(report.getDuplicateIdentifiers()).isEqualTo(1);
        assertThat(report.getInvalidCoordinates()).isEqualTo(1);
        assertThat(report.getMissingRequiredFields()).isEqualTo(1);
    }
}
