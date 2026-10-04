package com.railsync.station.service;

import com.railsync.algorithm.m1.ahocorasick.AhoCorasickAlgorithm;
import com.railsync.algorithm.m1.kmp.KMPAlgorithm;
import com.railsync.algorithm.m1.rabinkarp.RabinKarpAlgorithm;
import com.railsync.algorithm.m1.zfunction.ZFunctionAlgorithm;
import com.railsync.station.dto.StationSearchRequest;
import com.railsync.station.dto.StationSearchResponse;
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
class StationSearchServiceTest {

    @Mock
    private StationRepository stationRepository;

    private StationSearchService searchService;

    private Station stationPublicNdls;
    private Station stationSyntheticCsmt;

    @BeforeEach
    void setUp() {
        searchService = new StationSearchService(
                stationRepository,
                new KMPAlgorithm(),
                new ZFunctionAlgorithm(),
                new RabinKarpAlgorithm(),
                new AhoCorasickAlgorithm()
        );

        stationPublicNdls = Station.builder().id(1L).stationCode("PUBLIC_NDLS").name("New Delhi Public Station").city("New Delhi").state("Delhi").platformCount(16).status("ACTIVE").dataOrigin("PUBLIC_DATA").build();
        stationSyntheticCsmt = Station.builder().id(2L).stationCode("CSMT").name("Mumbai CSMT").city("Mumbai").state("Maharashtra").platformCount(18).status("ACTIVE").dataOrigin("SYNTHETIC").build();
    }

    @Test
    @DisplayName("Search Public & Synthetic Station Corpus using KMP")
    void testSearchKMP() {
        when(stationRepository.findAll()).thenReturn(List.of(stationPublicNdls, stationSyntheticCsmt));

        StationSearchRequest request = StationSearchRequest.builder()
                .query("Delhi")
                .algorithm("KMP")
                .dataOriginFilter("ALL")
                .build();

        StationSearchResponse response = searchService.searchStations(request);

        assertThat(response.getMatchedStationCount()).isEqualTo(1);
        assertThat(response.getMatches().get(0).getStation().getStationCode()).isEqualTo("PUBLIC_NDLS");
        assertThat(response.getMatches().get(0).getStation().getDataOrigin()).isEqualTo("PUBLIC_DATA");
    }

    @Test
    @DisplayName("Filter Search by Data Origin (SYNTHETIC only)")
    void testSearchFilterOrigin() {
        when(stationRepository.findAll()).thenReturn(List.of(stationPublicNdls, stationSyntheticCsmt));

        StationSearchRequest request = StationSearchRequest.builder()
                .query("Mumbai")
                .algorithm("Z_FUNCTION")
                .dataOriginFilter("SYNTHETIC")
                .build();

        StationSearchResponse response = searchService.searchStations(request);

        assertThat(response.getMatchedStationCount()).isEqualTo(1);
        assertThat(response.getMatches().get(0).getStation().getStationCode()).isEqualTo("CSMT");
    }
}
