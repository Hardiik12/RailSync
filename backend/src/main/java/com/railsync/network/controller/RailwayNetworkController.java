package com.railsync.network.controller;

import com.railsync.common.api.ApiResponse;
import com.railsync.network.dto.*;
import com.railsync.network.service.RailwayNetworkService;
import com.railsync.station.dto.StationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/network")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RailwayNetworkController {

    private final RailwayNetworkService railwayNetworkService;

    @GetMapping
    public ResponseEntity<ApiResponse<NetworkSummaryDto>> getNetwork() {
        NetworkSummaryDto summary = railwayNetworkService.getNetworkSummary();
        return ResponseEntity.ok(ApiResponse.success(summary));
    }

    @GetMapping("/stations")
    public ResponseEntity<ApiResponse<List<StationDto>>> getNetworkStations() {
        List<StationDto> stations = railwayNetworkService.getStations();
        return ResponseEntity.ok(ApiResponse.success(stations));
    }

    @GetMapping("/edges")
    public ResponseEntity<ApiResponse<List<NetworkEdgeDto>>> getNetworkEdges() {
        List<NetworkEdgeDto> edges = railwayNetworkService.getEdges();
        return ResponseEntity.ok(ApiResponse.success(edges));
    }

    @PostMapping("/build")
    public ResponseEntity<ApiResponse<NetworkSummaryDto>> buildNetworkTopology() {
        NetworkSummaryDto summary = railwayNetworkService.buildSyntheticNetworkTopology();
        return ResponseEntity.ok(ApiResponse.success(summary));
    }

    @PostMapping("/sync-routes")
    public ResponseEntity<ApiResponse<NetworkSummaryDto>> syncRouteTopology() {
        NetworkSummaryDto summary = railwayNetworkService.syncRouteTopology();
        return ResponseEntity.ok(ApiResponse.success(summary));
    }

    @PostMapping("/edges")
    public ResponseEntity<ApiResponse<NetworkEdgeDto>> createEdge(@RequestBody CreateNetworkEdgeRequest request) {
        NetworkEdgeDto edge = railwayNetworkService.createNetworkEdge(request);
        return ResponseEntity.ok(ApiResponse.success(edge));
    }

    @PostMapping("/flow")
    public ResponseEntity<ApiResponse<NetworkFlowResponse>> solveFlow(@RequestBody NetworkFlowRequest request) {
        NetworkFlowResponse response = railwayNetworkService.solveNetworkFlow(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/bottleneck")
    public ResponseEntity<ApiResponse<BottleneckAnalysisResponse>> analyzeBottleneck(@RequestBody BottleneckAnalysisRequest request) {
        BottleneckAnalysisResponse response = railwayNetworkService.analyzeBottleneck(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
