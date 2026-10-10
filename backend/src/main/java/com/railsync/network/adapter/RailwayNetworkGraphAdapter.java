package com.railsync.network.adapter;

import com.railsync.algorithm.m4.common.dto.EdgeInputDto;
import com.railsync.algorithm.m4.dinic.DinicAlgorithm;
import com.railsync.algorithm.m4.dinic.dto.DinicInput;
import com.railsync.algorithm.m4.dinic.dto.DinicResult;
import com.railsync.algorithm.m4.edmondskarp.EdmondsKarpAlgorithm;
import com.railsync.algorithm.m4.edmondskarp.dto.EdmondsKarpInput;
import com.railsync.algorithm.m4.edmondskarp.dto.EdmondsKarpResult;
import com.railsync.algorithm.m4.fordfulkerson.FordFulkersonAlgorithm;
import com.railsync.algorithm.m4.fordfulkerson.dto.FordFulkersonInput;
import com.railsync.algorithm.m4.fordfulkerson.dto.FordFulkersonResult;
import com.railsync.algorithm.m4.maxflowmincut.MaxFlowMinCutAlgorithm;
import com.railsync.algorithm.m4.maxflowmincut.dto.CutEdgeDto;
import com.railsync.algorithm.m4.maxflowmincut.dto.MaxFlowMinCutInput;
import com.railsync.algorithm.m4.maxflowmincut.dto.MaxFlowMinCutResult;
import com.railsync.common.error.ApiException;
import com.railsync.common.error.ErrorCode;
import com.railsync.network.dto.*;
import com.railsync.network.entity.NetworkEdge;
import com.railsync.station.dto.StationDto;
import com.railsync.station.entity.Station;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class RailwayNetworkGraphAdapter {

    private final DinicAlgorithm dinicAlgorithm;
    private final FordFulkersonAlgorithm fordFulkersonAlgorithm;
    private final EdmondsKarpAlgorithm edmondsKarpAlgorithm;
    private final MaxFlowMinCutAlgorithm maxFlowMinCutAlgorithm;

    public NetworkFlowResponse solveFlow(List<Station> stations, List<NetworkEdge> edges, NetworkFlowRequest request) {
        if (request == null) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Flow request cannot be null");
        }

        String sourceCode = request.getSourceStationCode();
        String sinkCode = request.getSinkStationCode();

        if (sourceCode == null || sourceCode.isBlank() || sinkCode == null || sinkCode.isBlank()) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Both sourceStationCode and sinkStationCode are required");
        }

        if (sourceCode.trim().equalsIgnoreCase(sinkCode.trim())) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Source and Sink stations must be distinct");
        }

        // Build vertex index map
        Map<String, Integer> stationCodeToIndex = new HashMap<>();
        Map<Integer, Station> indexToStation = new HashMap<>();

        for (Station st : stations) {
            String codeKey = st.getStationCode().trim().toUpperCase();
            if (!stationCodeToIndex.containsKey(codeKey)) {
                int idx = stationCodeToIndex.size();
                stationCodeToIndex.put(codeKey, idx);
                indexToStation.put(idx, st);
            }
        }

        String srcKey = sourceCode.trim().toUpperCase();
        String snkKey = sinkCode.trim().toUpperCase();

        if (!stationCodeToIndex.containsKey(srcKey)) {
            throw new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "Source station with code '" + sourceCode + "' not found in network");
        }
        if (!stationCodeToIndex.containsKey(snkKey)) {
            throw new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "Sink station with code '" + sinkCode + "' not found in network");
        }

        int sourceIdx = stationCodeToIndex.get(srcKey);
        int sinkIdx = stationCodeToIndex.get(snkKey);

        // Map NetworkEdge entities to EdgeInputDto
        List<EdgeInputDto> edgeInputs = new ArrayList<>();
        for (NetworkEdge edge : edges) {
            String uCode = edge.getFromStation().getStationCode().trim().toUpperCase();
            String vCode = edge.getToStation().getStationCode().trim().toUpperCase();

            Integer uIdx = stationCodeToIndex.get(uCode);
            Integer vIdx = stationCodeToIndex.get(vCode);

            if (uIdx != null && vIdx != null) {
                edgeInputs.add(EdgeInputDto.builder()
                        .u(uIdx)
                        .v(vIdx)
                        .capacity(edge.getCapacity())
                        .uName(edge.getFromStation().getStationCode())
                        .vName(edge.getToStation().getStationCode())
                        .build());
            }
        }

        int vertexCount = stationCodeToIndex.size();
        boolean traceEnabled = Boolean.TRUE.equals(request.getTraceEnabled());
        int maxTraceSteps = request.getMaxTraceSteps() != null ? request.getMaxTraceSteps() : 500;

        long startTime = System.nanoTime();

        // 1. Run Ford-Fulkerson
        FordFulkersonResult ffRes = fordFulkersonAlgorithm.execute(FordFulkersonInput.builder()
                .vertexCount(vertexCount)
                .source(sourceIdx)
                .sink(sinkIdx)
                .edges(edgeInputs)
                .traceEnabled(false)
                .build());

        // 2. Run Edmonds-Karp
        EdmondsKarpResult ekRes = edmondsKarpAlgorithm.execute(EdmondsKarpInput.builder()
                .vertexCount(vertexCount)
                .source(sourceIdx)
                .sink(sinkIdx)
                .edges(edgeInputs)
                .traceEnabled(false)
                .build());

        // 3. Run Dinic
        DinicResult dinicRes = dinicAlgorithm.execute(DinicInput.builder()
                .vertexCount(vertexCount)
                .source(sourceIdx)
                .sink(sinkIdx)
                .edges(edgeInputs)
                .traceEnabled(traceEnabled)
                .maxTraceSteps(maxTraceSteps)
                .build());

        long executionTimeNanos = System.nanoTime() - startTime;

        double ffFlow = ffRes.getMaxFlow();
        double ekFlow = ekRes.getMaxFlow();
        double dinicFlow = dinicRes.getMaxFlow();

        boolean valuesEqual = Math.abs(ffFlow - ekFlow) < 1e-6 && Math.abs(ekFlow - dinicFlow) < 1e-6;

        String algoStr = request.getAlgorithm() != null ? request.getAlgorithm().toUpperCase() : "DINIC";

        return NetworkFlowResponse.builder()
                .maxFlow(dinicFlow)
                .sourceStation(StationDto.fromEntity(indexToStation.get(sourceIdx)))
                .sinkStation(StationDto.fromEntity(indexToStation.get(sinkIdx)))
                .algorithm(algoStr)
                .fordFulkersonFlow(ffFlow)
                .edmondsKarpFlow(ekFlow)
                .dinicFlow(dinicFlow)
                .crossValidationPassed(valuesEqual)
                .edgesUsed(dinicRes.getFinalEdges())
                .trace(dinicRes.getTrace())
                .executionTimeNanos(executionTimeNanos)
                .build();
    }

    public BottleneckAnalysisResponse analyzeBottleneck(List<Station> stations, List<NetworkEdge> edges, BottleneckAnalysisRequest request) {
        if (request == null) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Bottleneck request cannot be null");
        }

        String sourceCode = request.getSourceStationCode();
        String sinkCode = request.getSinkStationCode();

        if (sourceCode == null || sourceCode.isBlank() || sinkCode == null || sinkCode.isBlank()) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Both sourceStationCode and sinkStationCode are required");
        }

        if (sourceCode.trim().equalsIgnoreCase(sinkCode.trim())) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Source and Sink stations must be distinct");
        }

        Map<String, Integer> stationCodeToIndex = new HashMap<>();
        Map<Integer, Station> indexToStation = new HashMap<>();

        for (Station st : stations) {
            String codeKey = st.getStationCode().trim().toUpperCase();
            if (!stationCodeToIndex.containsKey(codeKey)) {
                int idx = stationCodeToIndex.size();
                stationCodeToIndex.put(codeKey, idx);
                indexToStation.put(idx, st);
            }
        }

        String srcKey = sourceCode.trim().toUpperCase();
        String snkKey = sinkCode.trim().toUpperCase();

        if (!stationCodeToIndex.containsKey(srcKey)) {
            throw new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "Source station with code '" + sourceCode + "' not found in network");
        }
        if (!stationCodeToIndex.containsKey(snkKey)) {
            throw new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "Sink station with code '" + sinkCode + "' not found in network");
        }

        int sourceIdx = stationCodeToIndex.get(srcKey);
        int sinkIdx = stationCodeToIndex.get(snkKey);

        List<EdgeInputDto> edgeInputs = new ArrayList<>();
        Map<String, NetworkEdge> edgeMap = new HashMap<>();

        for (NetworkEdge edge : edges) {
            String uCode = edge.getFromStation().getStationCode().trim().toUpperCase();
            String vCode = edge.getToStation().getStationCode().trim().toUpperCase();

            Integer uIdx = stationCodeToIndex.get(uCode);
            Integer vIdx = stationCodeToIndex.get(vCode);

            if (uIdx != null && vIdx != null) {
                edgeInputs.add(EdgeInputDto.builder()
                        .u(uIdx)
                        .v(vIdx)
                        .capacity(edge.getCapacity())
                        .uName(edge.getFromStation().getStationCode())
                        .vName(edge.getToStation().getStationCode())
                        .build());
                edgeMap.put(uCode + "->" + vCode, edge);
            }
        }

        int vertexCount = stationCodeToIndex.size();
        boolean traceEnabled = Boolean.TRUE.equals(request.getTraceEnabled());
        int maxTraceSteps = request.getMaxTraceSteps() != null ? request.getMaxTraceSteps() : 500;

        long startTime = System.nanoTime();

        FordFulkersonResult ffRes = fordFulkersonAlgorithm.execute(FordFulkersonInput.builder()
                .vertexCount(vertexCount).source(sourceIdx).sink(sinkIdx).edges(edgeInputs).traceEnabled(false).build());

        EdmondsKarpResult ekRes = edmondsKarpAlgorithm.execute(EdmondsKarpInput.builder()
                .vertexCount(vertexCount).source(sourceIdx).sink(sinkIdx).edges(edgeInputs).traceEnabled(false).build());

        DinicResult dinicRes = dinicAlgorithm.execute(DinicInput.builder()
                .vertexCount(vertexCount).source(sourceIdx).sink(sinkIdx).edges(edgeInputs).traceEnabled(false).build());

        MaxFlowMinCutResult minCutRes = maxFlowMinCutAlgorithm.execute(MaxFlowMinCutInput.builder()
                .vertexCount(vertexCount)
                .source(sourceIdx)
                .sink(sinkIdx)
                .edges(edgeInputs)
                .traceEnabled(traceEnabled)
                .maxTraceSteps(maxTraceSteps)
                .build());

        long executionTimeNanos = System.nanoTime() - startTime;

        double ffFlow = ffRes.getMaxFlow();
        double ekFlow = ekRes.getMaxFlow();
        double dinicFlow = dinicRes.getMaxFlow();
        double minCutCapacity = minCutRes.getMinCutCapacity();

        boolean valuesEqual = Math.abs(ffFlow - ekFlow) < 1e-6 
                && Math.abs(ekFlow - dinicFlow) < 1e-6 
                && Math.abs(dinicFlow - minCutCapacity) < 1e-6;

        Station srcStation = indexToStation.get(sourceIdx);
        Station snkStation = indexToStation.get(sinkIdx);

        List<BottleneckSegmentDto> bottleneckSegments = new ArrayList<>();
        if (minCutRes.getCutEdges() != null) {
            for (CutEdgeDto cutEdge : minCutRes.getCutEdges()) {
                String key = cutEdge.getUName().trim().toUpperCase() + "->" + cutEdge.getVName().trim().toUpperCase();
                NetworkEdge netEdge = edgeMap.get(key);
                Double dist = netEdge != null ? netEdge.getDistanceKm() : 0.0;

                bottleneckSegments.add(BottleneckSegmentDto.builder()
                        .fromStationCode(cutEdge.getUName())
                        .fromStationName(netEdge != null ? netEdge.getFromStation().getName() : cutEdge.getUName())
                        .toStationCode(cutEdge.getVName())
                        .toStationName(netEdge != null ? netEdge.getToStation().getName() : cutEdge.getVName())
                        .capacity(cutEdge.getCapacity())
                        .flow(cutEdge.getFlow())
                        .distanceKm(dist)
                        .isBottleneck(true)
                        .build());
            }
        }

        String explanation = String.format(
                "Railway Bottleneck Analysis identified a maximum network flow capacity of %.2f units between station %s (%s) and %s (%s). " +
                "The bottleneck is formed by %d track segment(s) with a total min-cut capacity of %.2f units. " +
                "Note: All capacity values represent configured line throughput parameters and do not represent real-world passenger limits.",
                dinicFlow,
                srcStation.getName(), srcStation.getStationCode(),
                snkStation.getName(), snkStation.getStationCode(),
                bottleneckSegments.size(),
                minCutCapacity
        );

        String algoStr = request.getAlgorithm() != null ? request.getAlgorithm().toUpperCase() : "MAX_FLOW_MIN_CUT";

        return BottleneckAnalysisResponse.builder()
                .sourceStation(StationDto.fromEntity(srcStation))
                .destinationStation(StationDto.fromEntity(snkStation))
                .maxFlow(dinicFlow)
                .minCutCapacity(minCutCapacity)
                .fordFulkersonFlow(ffFlow)
                .edmondsKarpFlow(ekFlow)
                .dinicFlow(dinicFlow)
                .crossValidationPassed(valuesEqual)
                .algorithm(algoStr)
                .bottleneckSegments(bottleneckSegments)
                .sourceCutVertices(minCutRes.getSourceCutVertices())
                .sinkCutVertices(minCutRes.getSinkCutVertices())
                .explanation(explanation)
                .executionTimeNanos(executionTimeNanos)
                .trace(minCutRes.getTrace())
                .build();
    }
}
