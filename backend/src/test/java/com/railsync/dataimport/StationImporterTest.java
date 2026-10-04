package com.railsync.dataimport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.railsync.station.entity.Station;
import com.railsync.station.repository.StationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StationImporterTest {

    @TempDir
    Path tempDir;

    @Test
    void importsValidGeoJsonStation() throws Exception {
        Path file = write("""
            {
              "type":"FeatureCollection",
              "features":[{
                "type":"Feature",
                "properties":{
                  "code":"BZA",
                  "name":"Vijayawada Junction",
                  "state":"Andhra Pradesh",
                  "zone":"SCR",
                  "address":"Vijayawada",
                  "city":"Vijayawada"
                },
                "geometry":{"type":"Point","coordinates":[80.6480,16.5062]}
              }]
            }
            """);

        StationRepository repo = mock(StationRepository.class);
        when(repo.findByStationCode("BZA")).thenReturn(Optional.empty());
        when(repo.save(any(Station.class))).thenAnswer(inv -> inv.getArgument(0));

        ImportResult result = new StationImporter(new ObjectMapper(), repo).importGeoJson(file);

        assertThat(result.getRecordsRead()).isEqualTo(1);
        assertThat(result.getRecordsInserted()).isEqualTo(1);
        assertThat(result.getInvalidRecords()).isZero();

        ArgumentCaptor<Station> captor = ArgumentCaptor.forClass(Station.class);
        verify(repo).save(captor.capture());
        Station station = captor.getValue();
        assertThat(station.getStationCode()).isEqualTo("BZA");
        assertThat(station.getLatitude()).isEqualTo(16.5062);
        assertThat(station.getLongitude()).isEqualTo(80.6480);
        assertThat(station.getDataOrigin()).isEqualTo("PUBLIC_DATA");
    }

    @Test
    void rejectsMissingRequiredStationFields() throws Exception {
        Path file = write("""
            {
              "type":"FeatureCollection",
              "features":[{
                "type":"Feature",
                "properties":{"code":"BZA","name":"","state":"Andhra Pradesh"},
                "geometry":{"type":"Point","coordinates":[80.6480,16.5062]}
              }]
            }
            """);

        StationRepository repo = mock(StationRepository.class);
        ImportResult result = new StationImporter(new ObjectMapper(), repo).importGeoJson(file);

        assertThat(result.getRecordsRead()).isEqualTo(1);
        assertThat(result.getInvalidRecords()).isEqualTo(1);
        verify(repo, never()).save(any());
    }

    @Test
    void repeatedCodeInSameFileIsReportedAsDuplicate() throws Exception {
        Path file = write("""
            {
              "type":"FeatureCollection",
              "features":[
                {"type":"Feature","properties":{"code":"BZA","name":"A","state":"AP"},"geometry":{"type":"Point","coordinates":[80,16]}},
                {"type":"Feature","properties":{"code":"BZA","name":"B","state":"AP"},"geometry":{"type":"Point","coordinates":[81,17]}}
              ]
            }
            """);

        StationRepository repo = mock(StationRepository.class);
        when(repo.findByStationCode("BZA")).thenReturn(Optional.empty());
        when(repo.save(any(Station.class))).thenAnswer(inv -> inv.getArgument(0));

        ImportResult result = new StationImporter(new ObjectMapper(), repo).importGeoJson(file);

        assertThat(result.getRecordsRead()).isEqualTo(2);
        assertThat(result.getRecordsInserted()).isEqualTo(1);
        assertThat(result.getDuplicates()).isEqualTo(1);
        verify(repo, times(1)).save(any(Station.class));
    }

    @Test
    void existingStationIsUpdatedWithoutChangingDatabaseIdentity() throws Exception {
        Path file = write("""
            {
              "type":"FeatureCollection",
              "features":[{
                "type":"Feature",
                "properties":{"code":"BZA","name":"Updated Name","state":"AP"},
                "geometry":{"type":"Point","coordinates":[80,16]}
              }]
            }
            """);

        Station existing = Station.builder().id(42L).stationCode("BZA").name("Old").build();
        StationRepository repo = mock(StationRepository.class);
        when(repo.findByStationCode("BZA")).thenReturn(Optional.of(existing));
        when(repo.save(any(Station.class))).thenAnswer(inv -> inv.getArgument(0));

        ImportResult result = new StationImporter(new ObjectMapper(), repo).importGeoJson(file);

        assertThat(result.getRecordsUpdated()).isEqualTo(1);
        assertThat(result.getRecordsInserted()).isZero();
        assertThat(existing.getId()).isEqualTo(42L);
        assertThat(existing.getName()).isEqualTo("Updated Name");
    }

    private Path write(String content) throws Exception {
        Path file = tempDir.resolve("stations.geojson");
        Files.writeString(file, content);
        return file;
    }
}
