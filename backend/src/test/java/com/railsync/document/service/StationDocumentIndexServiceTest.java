package com.railsync.document.service;

import com.railsync.document.domain.RailwayDocument;
import com.railsync.document.dto.StationIndexResponse;
import com.railsync.document.repository.RailwayDocumentRepository;
import com.railsync.station.entity.Station;
import com.railsync.station.repository.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StationDocumentIndexServiceTest {

    @Mock
    private StationRepository stationRepository;

    @Mock
    private RailwayDocumentRepository railwayDocumentRepository;

    private StationDocumentIndexService indexService;

    private Station stationPublicNdls;

    @BeforeEach
    void setUp() {
        indexService = new StationDocumentIndexService(stationRepository, railwayDocumentRepository);

        stationPublicNdls = Station.builder().id(1L).stationCode("PUBLIC_NDLS").name("New Delhi Public Station").city("New Delhi").state("Delhi").platformCount(16).status("ACTIVE").dataOrigin("PUBLIC_DATA").build();
    }

    @Test
    @DisplayName("Index Station Documents - Create New Document Deterministically")
    void testIndexStationDocumentsNew() {
        when(stationRepository.findAll()).thenReturn(List.of(stationPublicNdls));
        when(railwayDocumentRepository.findByDocIdentifier("STATION_DOC_PUBLIC_NDLS")).thenReturn(Optional.empty());

        StationIndexResponse response = indexService.indexStationDocuments();

        assertThat(response.getRecordsConsidered()).isEqualTo(1);
        assertThat(response.getDocumentsCreated()).isEqualTo(1);
        assertThat(response.getDocumentsUpdated()).isEqualTo(0);
        assertThat(response.getSkippedRecords()).isEqualTo(0);

        verify(railwayDocumentRepository, times(1)).save(any(RailwayDocument.class));
    }

    @Test
    @DisplayName("Index Station Documents - Idempotent Skip when Content Unchanged")
    void testIndexStationDocumentsIdempotent() {
        when(stationRepository.findAll()).thenReturn(List.of(stationPublicNdls));

        String expectedContent = "Station: New Delhi Public Station\nCode: PUBLIC_NDLS\nCity: New Delhi\nState: Delhi\n fontPlatforms: 16\nStatus: ACTIVE\nOrigin: PUBLIC_DATA";
        RailwayDocument existingDoc = RailwayDocument.builder()
                .docIdentifier("STATION_DOC_PUBLIC_NDLS")
                .title("Station Record: New Delhi Public Station")
                .docType("STATION_RECORD")
                .content(expectedContent)
                .status("INDEXED")
                .build();

        when(railwayDocumentRepository.findByDocIdentifier("STATION_DOC_PUBLIC_NDLS")).thenReturn(Optional.of(existingDoc));

        StationIndexResponse response = indexService.indexStationDocuments();

        assertThat(response.getRecordsConsidered()).isEqualTo(1);
        assertThat(response.getDocumentsCreated()).isEqualTo(0);
        assertThat(response.getDocumentsUpdated()).isEqualTo(0);
        assertThat(response.getSkippedRecords()).isEqualTo(1);

        verify(railwayDocumentRepository, never()).save(any(RailwayDocument.class));
    }
}
