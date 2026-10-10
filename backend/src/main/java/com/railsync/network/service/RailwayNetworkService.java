package com.railsync.network.service;

import com.railsync.common.error.ApiException;
import com.railsync.common.error.ErrorCode;
import com.railsync.network.adapter.RailwayNetworkGraphAdapter;
import com.railsync.network.dto.*;
import com.railsync.network.entity.NetworkEdge;
import com.railsync.network.repository.NetworkEdgeRepository;
import com.railsync.station.dto.StationDto;
import com.railsync.station.entity.Station;
import com.railsync.station.repository.StationRepository;
import com.railsync.train.entity.Route;
import com.railsync.train.entity.RouteStop;
import com.railsync.train.repository.RouteRepository;
import com.railsync.train.repository.RouteStopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RailwayNetworkService {

    private final StationRepository stationRepository;
    private final NetworkEdgeRepository networkEdgeRepository;
    private final RouteRepository routeRepository;
    private final RouteStopRepository routeStopRepository;
    private final RailwayNetworkGraphAdapter graphAdapter;

    public List<StationDto> getStations() {
        return stationRepository.findAll().stream()
                .map(StationDto::fromEntity)
                .toList();
    }

    public List<NetworkEdgeDto> getEdges() {
        return networkEdgeRepository.findAllWithStations().stream()
                .map(NetworkEdgeDto::fromEntity)
                .toList();
    }

    public NetworkSummaryDto getNetworkSummary() {
        List<Station> stations = stationRepository.findAll();
        List<NetworkEdge> edges = networkEdgeRepository.findAllWithStations();

        int publicStations = (int) stations.stream().filter(s -> "PUBLIC_DATA".equalsIgnoreCase(s.getDataOrigin())).count();
        int syntheticStations = stations.size() - publicStations;

        int publicEdges = (int) edges.stream().filter(e -> "PUBLIC_DATA".equalsIgnoreCase(e.getDataOrigin())).count();
        int syntheticEdges = edges.size() - publicEdges;

        List<StationDto> stationDtos = stations.stream().map(StationDto::fromEntity).toList();
        List<NetworkEdgeDto> edgeDtos = edges.stream().map(NetworkEdgeDto::fromEntity).toList();

        return NetworkSummaryDto.builder()
                .stations(stationDtos)
                .edges(edgeDtos)
                .stationCount(stations.size())
                .edgeCount(edges.size())
                .publicStationCount(publicStations)
                .syntheticStationCount(syntheticStations)
                .publicEdgeCount(publicEdges)
                .syntheticEdgeCount(syntheticEdges)
                .build();
    }

    @Transactional
    public NetworkEdgeDto createNetworkEdge(CreateNetworkEdgeRequest request) {
        if (request == null) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Edge creation request cannot be null");
        }

        String fromCode = request.getFromStationCode();
        String toCode = request.getToStationCode();

        if (fromCode == null || fromCode.isBlank() || toCode == null || toCode.isBlank()) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Both fromStationCode and toStationCode are required");
        }

        if (fromCode.trim().equalsIgnoreCase(toCode.trim())) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Self-loop edges are not allowed (fromStation and toStation must be distinct)");
        }

        if (request.getCapacity() == null || request.getCapacity() < 0) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Capacity must be non-negative (>= 0)");
        }

        if (request.getDistanceKm() == null || request.getDistanceKm() < 0) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Distance in km must be non-negative (>= 0)");
        }

        if (request.getTravelTimeMinutes() == null || request.getTravelTimeMinutes() < 0) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Travel time in minutes must be non-negative (>= 0)");
        }

        Station fromStation = stationRepository.findByStationCode(fromCode.trim().toUpperCase())
                .orElseThrow(() -> new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "From-station with code '" + fromCode + "' not found"));

        Station toStation = stationRepository.findByStationCode(toCode.trim().toUpperCase())
                .orElseThrow(() -> new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "To-station with code '" + toCode + "' not found"));

        if (networkEdgeRepository.existsByFromStationAndToStation(fromStation, toStation)) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Duplicate directed edge already exists from " + fromCode + " to " + toCode);
        }

        String dataOrigin = request.getDataOrigin() != null && !request.getDataOrigin().isBlank() ? request.getDataOrigin().toUpperCase() : "SYNTHETIC";
        if (!"SYNTHETIC".equals(dataOrigin) && !"PUBLIC_DATA".equals(dataOrigin)) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "dataOrigin must be either 'SYNTHETIC' or 'PUBLIC_DATA'");
        }

        String sourceDataset = request.getSourceDataset() != null && !request.getSourceDataset().isBlank()
                ? request.getSourceDataset().trim() : "SYNTHETIC_TOPOLOGY_2026";

        NetworkEdge edge = NetworkEdge.builder()
                .fromStation(fromStation)
                .toStation(toStation)
                .capacity(request.getCapacity())
                .distanceKm(request.getDistanceKm())
                .travelTimeMinutes(request.getTravelTimeMinutes())
                .dataOrigin(dataOrigin)
                .sourceDataset(sourceDataset)
                .build();

        NetworkEdge saved = networkEdgeRepository.save(edge);
        return NetworkEdgeDto.fromEntity(saved);
    }

    @Transactional
    public NetworkSummaryDto buildSyntheticNetworkTopology() {
        // If edges are empty, sync from routes first or fallback to connecting adjacent stations
        if (routeRepository.count() > 0) {
            return syncRouteTopology();
        }

        List<Station> stations = stationRepository.findAll();
        if (stations.size() >= 2) {
            for (int i = 0; i < stations.size() - 1; i++) {
                Station u = stations.get(i);
                Station v = stations.get(i + 1);
                if (!networkEdgeRepository.existsByFromStationAndToStation(u, v)) {
                    networkEdgeRepository.save(NetworkEdge.builder()
                            .fromStation(u)
                            .toStation(v)
                            .capacity(30.0 + (i % 5) * 5.0)
                            .distanceKm(20.0 + (i % 10) * 15.0)
                            .travelTimeMinutes(30 + (i % 10) * 10)
                            .dataOrigin("SYNTHETIC")
                            .sourceDataset("AUTO_TOPOLOGY_BUILD")
                            .build());
                }
            }
        }
        return getNetworkSummary();
    }

    @Transactional
    public NetworkSummaryDto syncRouteTopology() {
        List<Route> routes = routeRepository.findAll();
        for (Route route : routes) {
            List<RouteStop> stops = routeStopRepository.findByRouteIdOrderByStopSequenceAsc(route.getId());
            if (stops.size() < 2) continue;

            for (int i = 0; i < stops.size() - 1; i++) {
                RouteStop r1 = stops.get(i);
                RouteStop r2 = stops.get(i + 1);

                Station u = r1.getStation();
                Station v = r2.getStation();

                if (u.getId().equals(v.getId())) continue;

                Double distKm = null;
                if (r2.getDistanceFromOrigin() != null && r1.getDistanceFromOrigin() != null) {
                    distKm = r2.getDistanceFromOrigin() - r1.getDistanceFromOrigin();
                }
                if (distKm == null || distKm <= 0) {
                    distKm = calculateHaversineDistance(u, v);
                }

                int travelTime = estimateTravelTimeMinutes(r1.getScheduledDeparture(), r2.getScheduledArrival(), distKm);
                double baseCapacity = 50.0;
                if (route.getTrain() != null && route.getTrain().getCapacity() != null) {
                    baseCapacity = Math.max(50.0, route.getTrain().getCapacity() / 10.0);
                }

                Optional<NetworkEdge> existingEdgeOpt = networkEdgeRepository.findByFromStationAndToStation(u, v);
                if (existingEdgeOpt.isPresent()) {
                    NetworkEdge existing = existingEdgeOpt.get();
                    existing.setCapacity(existing.getCapacity() + baseCapacity);
                    existing.setDistanceKm(distKm);
                    existing.setTravelTimeMinutes(travelTime);
                    existing.setDataOrigin("PUBLIC_DATA");
                    existing.setSourceDataset("ROUTE_STOP_TOPOLOGY");
                    networkEdgeRepository.save(existing);
                } else {
                    NetworkEdge newEdge = NetworkEdge.builder()
                            .fromStation(u)
                            .toStation(v)
                            .capacity(baseCapacity)
                            .distanceKm(distKm)
                            .travelTimeMinutes(travelTime)
                            .dataOrigin("PUBLIC_DATA")
                            .sourceDataset("ROUTE_STOP_TOPOLOGY")
                            .build();
                    networkEdgeRepository.save(newEdge);
                }
            }
        }
        return getNetworkSummary();
    }

    private double calculateHaversineDistance(Station u, Station v) {
        if (u.getLatitude() == null || u.getLongitude() == null || v.getLatitude() == null || v.getLongitude() == null) {
            return 15.0;
        }
        double lat1 = Math.toRadians(u.getLatitude());
        double lon1 = Math.toRadians(u.getLongitude());
        double lat2 = Math.toRadians(v.getLatitude());
        double lon2 = Math.toRadians(v.getLongitude());

        double dlat = lat2 - lat1;
        double dlon = lon2 - lon1;

        double a = Math.sin(dlat / 2) * Math.sin(dlat / 2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.sin(dlon / 2) * Math.sin(dlon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double r = 6371.0;
        double dist = r * c;
        return dist > 0 ? Math.round(dist * 10.0) / 10.0 : 15.0;
    }

    private int estimateTravelTimeMinutes(String depTimeStr, String arrTimeStr, double distKm) {
        if (depTimeStr != null && arrTimeStr != null && !depTimeStr.isBlank() && !arrTimeStr.isBlank()) {
            try {
                String[] depParts = depTimeStr.trim().split(":");
                String[] arrParts = arrTimeStr.trim().split(":");
                if (depParts.length >= 2 && arrParts.length >= 2) {
                    int depMin = Integer.parseInt(depParts[0]) * 60 + Integer.parseInt(depParts[1]);
                    int arrMin = Integer.parseInt(arrParts[0]) * 60 + Integer.parseInt(arrParts[1]);
                    int diff = arrMin - depMin;
                    if (diff < 0) diff += 1440;
                    if (diff > 0) return diff;
                }
            } catch (Exception ignored) {}
        }
        return (int) Math.max(10, Math.round(distKm / 60.0 * 60.0));
    }

    @Transactional
    public NetworkFlowResponse solveNetworkFlow(NetworkFlowRequest request) {
        if (networkEdgeRepository.count() == 0) {
            if (routeRepository.count() > 0) {
                syncRouteTopology();
            } else {
                buildSyntheticNetworkTopology();
            }
        }
        List<Station> stations = stationRepository.findAll();
        List<NetworkEdge> edges = networkEdgeRepository.findAllWithStations();
        return graphAdapter.solveFlow(stations, edges, request);
    }

    @Transactional
    public BottleneckAnalysisResponse analyzeBottleneck(BottleneckAnalysisRequest request) {
        if (networkEdgeRepository.count() == 0) {
            if (routeRepository.count() > 0) {
                syncRouteTopology();
            } else {
                buildSyntheticNetworkTopology();
            }
        }
        List<Station> stations = stationRepository.findAll();
        List<NetworkEdge> edges = networkEdgeRepository.findAllWithStations();
        return graphAdapter.analyzeBottleneck(stations, edges, request);
    }
}
