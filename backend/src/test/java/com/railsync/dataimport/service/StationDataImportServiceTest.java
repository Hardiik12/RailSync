package com.railsync.dataimport.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.railsync.dataimport.dto.ImportResult;
import com.railsync.station.entity.Station;
import com.railsync.station.repository.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StationDataImportServiceTest {

    @Mock
    private StationRepository stationRepository;

    private StationDataImportService importService;

    @BeforeEach
    void setUp() {
        importService = new StationDataImportService(stationRepository, new ObjectMapper());
    }

    @Test
    @DisplayName("Successfully process GeoJSON fixture with inserts, updates, duplicates, and invalid records")
    void testGeoJsonImportFixture() {
        String geoJson = """
        {
          "type": "FeatureCollection",
          "features": [
            {
              "type": "Feature",
              "geometry": { "type": "Point", "coordinates": [72.8347, 18.9398] },
              "properties": { "stationCode": "TEST_CSMT", "name": "CSMT Station", "city": "Mumbai", "state": "MH" }
            },
            {
              "type": "Feature",
              "geometry": { "type": "Point", "coordinates": [73.8567, 18.5204] },
              "properties": { "stationCode": "TEST_PUNE", "name": "Pune Station", "city": "Pune", "state": "MH" }
            },
            {
              "type": "Feature",
              "geometry": { "type": "Point", "coordinates": [0, 0] },
              "properties": { "stationCode": "", "name": "" }
            },
            {
              "type": "Feature",
              "geometry": { "type": "Point", "coordinates": [73.8567, 18.5204] },
              "properties": { "stationCode": "TEST_PUNE", "name": "Duplicate Pune", "city": "Pune", "state": "MH" }
            }
          ]
        }
        """;

        when(stationRepository.findByStationCode("TEST_CSMT")).thenReturn(Optional.empty());
        Station existingPune = Station.builder().stationCode("TEST_PUNE").name("Old Pune").city("Pune").state("MH").build();
        when(stationRepository.findByStationCode("TEST_PUNE")).thenReturn(Optional.of(existingPune));

        ImportResult result = importService.importGeoJsonStations(geoJson);

        assertThat(result.getRecordsRead()).isEqualTo(4);
        assertThat(result.getRecordsInserted()).isEqualTo(1);
        assertThat(result.getRecordsUpdated()).isEqualTo(1);
        assertThat(result.getDuplicates()).isEqualTo(1);
        assertThat(result.getInvalidRecords()).isEqualTo(1);

        verify(stationRepository, times(2)).save(any(Station.class));
    }
}
