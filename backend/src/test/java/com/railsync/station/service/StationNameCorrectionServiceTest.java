package com.railsync.station.service;

import com.railsync.algorithm.m3.damerau.DamerauLevenshteinAlgorithm;
import com.railsync.algorithm.m3.levenshtein.LevenshteinAlgorithm;
import com.railsync.station.dto.StationCorrectionRequest;
import com.railsync.station.dto.StationCorrectionResponse;
import com.railsync.station.entity.Station;
import com.railsync.station.repository.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StationNameCorrectionServiceTest {

    @Mock
    private StationRepository stationRepository;

    private StationNameCorrectionService correctionService;

    private Station stationVijayawada;
    private Station stationMumbai;

    @BeforeEach
    void setUp() {
        correctionService = new StationNameCorrectionService(
                stationRepository,
                new LevenshteinAlgorithm(),
                new DamerauLevenshteinAlgorithm()
        );

        stationVijayawada = Station.builder().id(1L).stationCode("BZA").name("Vijayawada Junction").city("Vijayawada").state("Andhra Pradesh").platformCount(10).status("ACTIVE").dataOrigin("SYNTHETIC").build();
        stationMumbai = Station.builder().id(2L).stationCode("CSMT").name("Mumbai CSMT").city("Mumbai").state("Maharashtra").platformCount(18).status("ACTIVE").dataOrigin("SYNTHETIC").build();
    }

    @Test
    @DisplayName("Fuzzy Station Name Correction using Levenshtein")
    void testCorrectionLevenshtein() {
        when(stationRepository.findAll()).thenReturn(List.of(stationVijayawada, stationMumbai));

        StationCorrectionRequest request = StationCorrectionRequest.builder()
                .query("Vijayawda Junction")
                .algorithm("LEVENSHTEIN")
                .maxCandidates(5)
                .build();

        StationCorrectionResponse response = correctionService.correctStationName(request);

        assertThat(response.getCandidates()).isNotEmpty();
        assertThat(response.getCandidates().get(0).getStation().getStationCode()).isEqualTo("BZA");
        assertThat(response.getCandidates().get(0).getDistance()).isEqualTo(1); // 1 deletion ('a')
    }

    @Test
    @DisplayName("Fuzzy Station Name Correction using Damerau-Levenshtein (Transposition)")
    void testCorrectionDamerauTransposition() {
        when(stationRepository.findAll()).thenReturn(List.of(stationVijayawada, stationMumbai));

        StationCorrectionRequest request = StationCorrectionRequest.builder()
                .query("Muambai CSMT") // 'ua' transposed
                .algorithm("DAMERAU_LEVENSHTEIN")
                .maxCandidates(5)
                .build();

        StationCorrectionResponse response = correctionService.correctStationName(request);

        assertThat(response.getCandidates()).isNotEmpty();
        assertThat(response.getCandidates().get(0).getStation().getStationCode()).isEqualTo("CSMT");
        assertThat(response.getCandidates().get(0).getDistance()).isEqualTo(1); // 1 transposition
    }
}
