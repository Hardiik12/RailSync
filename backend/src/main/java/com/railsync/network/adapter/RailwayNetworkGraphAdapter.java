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
import com.railsync.common.error.ApiException;
import com.railsync.common.error.ErrorCode;
import com.railsync.network.dto.NetworkFlowRequest;
import com.railsync.network.dto.NetworkFlowResponse;
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
}
