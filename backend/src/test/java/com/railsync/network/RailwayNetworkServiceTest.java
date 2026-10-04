package com.railsync.network;

import com.railsync.algorithm.m4.dinic.DinicAlgorithm;
import com.railsync.algorithm.m4.edmondskarp.EdmondsKarpAlgorithm;
import com.railsync.algorithm.m4.fordfulkerson.FordFulkersonAlgorithm;
import com.railsync.common.error.ApiException;
import com.railsync.common.error.ErrorCode;
import com.railsync.network.adapter.RailwayNetworkGraphAdapter;
import com.railsync.network.dto.*;
import com.railsync.network.entity.NetworkEdge;
import com.railsync.network.repository.NetworkEdgeRepository;
import com.railsync.network.service.RailwayNetworkService;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RailwayNetworkServiceTest {

    @Mock
    private StationRepository stationRepository;

    @Mock
    private NetworkEdgeRepository networkEdgeRepository;

    private RailwayNetworkGraphAdapter graphAdapter;
    private RailwayNetworkService networkService;

    private Station stationCsmt;
    private Station stationDadar;
    private Station stationPune;

    @BeforeEach
    void setUp() {
        graphAdapter = new RailwayNetworkGraphAdapter(
                new DinicAlgorithm(),
                new FordFulkersonAlgorithm(),
                new EdmondsKarpAlgorithm()
        );
        networkService = new RailwayNetworkService(stationRepository, networkEdgeRepository, graphAdapter);

        stationCsmt = Station.builder().id(1L).stationCode("CSMT").name("Mumbai CSMT").city("Mumbai").state("MH").platformCount(18).status("ACTIVE").dataOrigin("SYNTHETIC").build();
        stationDadar = Station.builder().id(2L).stationCode("DADAR").name("Dadar Central").city("Mumbai").state("MH").platformCount(8).status("ACTIVE").dataOrigin("SYNTHETIC").build();
        stationPune = Station.builder().id(3L).stationCode("PUNE").name("Pune Junction").city("Pune").state("MH").platformCount(6).status("ACTIVE").dataOrigin("PUBLIC_DATA").build();
    }

    @Test
    @DisplayName("Create Network Edge - Successful Validation & Persistence")
    void testCreateNetworkEdgeSuccess() {
        CreateNetworkEdgeRequest req = CreateNetworkEdgeRequest.builder()
                .fromStationCode("CSMT")
                .toStationCode("DADAR")
                .capacity(50.0)
                .distanceKm(9.0)
                .travelTimeMinutes(15)
                .dataOrigin("SYNTHETIC")
                .sourceDataset("TEST_DATASET")
                .build();

        when(stationRepository.findByStationCode("CSMT")).thenReturn(Optional.of(stationCsmt));
        when(stationRepository.findByStationCode("DADAR")).thenReturn(Optional.of(stationDadar));
        when(networkEdgeRepository.existsByFromStationAndToStation(stationCsmt, stationDadar)).thenReturn(false);

        NetworkEdge savedEdge = NetworkEdge.builder()
                .id(100L)
                .fromStation(stationCsmt)
                .toStation(stationDadar)
                .capacity(50.0)
                .distanceKm(9.0)
                .travelTimeMinutes(15)
                .dataOrigin("SYNTHETIC")
                .sourceDataset("TEST_DATASET")
                .build();
        when(networkEdgeRepository.save(any(NetworkEdge.class))).thenReturn(savedEdge);

        NetworkEdgeDto dto = networkService.createNetworkEdge(req);

        assertThat(dto.getFromStationCode()).isEqualTo("CSMT");
        assertThat(dto.getToStationCode()).isEqualTo("DADAR");
        assertThat(dto.getCapacity()).isEqualTo(50.0);
        assertThat(dto.getDistanceKm()).isEqualTo(9.0);
        assertThat(dto.getTravelTimeMinutes()).isEqualTo(15);
    }

    @Test
    @DisplayName("Reject Self-Loop Network Edge")
    void testRejectSelfLoop() {
        CreateNetworkEdgeRequest req = CreateNetworkEdgeRequest.builder()
                .fromStationCode("CSMT")
                .toStationCode("CSMT")
                .capacity(10.0)
                .distanceKm(5.0)
                .travelTimeMinutes(10)
                .build();

        assertThatThrownBy(() -> networkService.createNetworkEdge(req))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_INPUT);
    }

    @Test
    @DisplayName("Reject Negative Capacity or Distance Edge")
    void testRejectNegativeCapacity() {
        CreateNetworkEdgeRequest req = CreateNetworkEdgeRequest.builder()
                .fromStationCode("CSMT")
                .toStationCode("DADAR")
                .capacity(-10.0)
                .distanceKm(5.0)
                .travelTimeMinutes(10)
                .build();

        assertThatThrownBy(() -> networkService.createNetworkEdge(req))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_INPUT);
    }

    @Test
    @DisplayName("Reject Duplicate Directed Edge")
    void testRejectDuplicateEdge() {
        CreateNetworkEdgeRequest req = CreateNetworkEdgeRequest.builder()
                .fromStationCode("CSMT")
                .toStationCode("DADAR")
                .capacity(50.0)
                .distanceKm(9.0)
                .travelTimeMinutes(15)
                .build();

        when(stationRepository.findByStationCode("CSMT")).thenReturn(Optional.of(stationCsmt));
        when(stationRepository.findByStationCode("DADAR")).thenReturn(Optional.of(stationDadar));
        when(networkEdgeRepository.existsByFromStationAndToStation(stationCsmt, stationDadar)).thenReturn(true);

        assertThatThrownBy(() -> networkService.createNetworkEdge(req))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_INPUT);
    }

    @Test
    @DisplayName("Reject Unknown Station Reference")
    void testRejectUnknownStation() {
        CreateNetworkEdgeRequest req = CreateNetworkEdgeRequest.builder()
                .fromStationCode("UNKNOWN_SRC")
                .toStationCode("DADAR")
                .capacity(50.0)
                .distanceKm(9.0)
                .travelTimeMinutes(15)
                .build();

        when(stationRepository.findByStationCode("UNKNOWN_SRC")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> networkService.createNetworkEdge(req))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.RESOURCE_NOT_FOUND);
    }

    @Test
    @DisplayName("Graph Adapter & M4 Flow Integration: FF == EK == Dinic Agreement")
    void testFlowAgreementFF_EK_Dinic() {
        List<Station> stations = List.of(stationCsmt, stationDadar, stationPune);
        List<NetworkEdge> edges = List.of(
                NetworkEdge.builder().id(1L).fromStation(stationCsmt).toStation(stationDadar).capacity(30.0).distanceKm(9.0).travelTimeMinutes(15).dataOrigin("SYNTHETIC").build(),
                NetworkEdge.builder().id(2L).fromStation(stationDadar).toStation(stationPune).capacity(20.0).distanceKm(150.0).travelTimeMinutes(120).dataOrigin("SYNTHETIC").build(),
                NetworkEdge.builder().id(3L).fromStation(stationCsmt).toStation(stationPune).capacity(15.0).distanceKm(160.0).travelTimeMinutes(130).dataOrigin("SYNTHETIC").build()
        );

        when(stationRepository.findAll()).thenReturn(stations);
        when(networkEdgeRepository.findAllWithStations()).thenReturn(edges);

        NetworkFlowRequest req = NetworkFlowRequest.builder()
                .sourceStationCode("CSMT")
                .sinkStationCode("PUNE")
                .algorithm("DINIC")
                .traceEnabled(true)
                .build();

        NetworkFlowResponse flowRes = networkService.solveNetworkFlow(req);

        assertThat(flowRes.getMaxFlow()).isEqualTo(35.0); // 20 via DADAR + 15 direct
        assertThat(flowRes.getFordFulkersonFlow()).isEqualTo(35.0);
        assertThat(flowRes.getEdmondsKarpFlow()).isEqualTo(35.0);
        assertThat(flowRes.getDinicFlow()).isEqualTo(35.0);
        assertThat(flowRes.getCrossValidationPassed()).isTrue();
        assertThat(flowRes.getSourceStation().getStationCode()).isEqualTo("CSMT");
        assertThat(flowRes.getSinkStation().getStationCode()).isEqualTo("PUNE");
    }
}
