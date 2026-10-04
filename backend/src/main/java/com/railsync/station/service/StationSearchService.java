package com.railsync.station.service;

import com.railsync.algorithm.common.TraceStep;
import com.railsync.algorithm.m1.ahocorasick.AhoCorasickAlgorithm;
import com.railsync.algorithm.m1.ahocorasick.dto.AhoCorasickInput;
import com.railsync.algorithm.m1.ahocorasick.dto.AhoCorasickResult;
import com.railsync.algorithm.m1.ahocorasick.dto.KeywordMatchDto;
import com.railsync.algorithm.m1.kmp.KMPAlgorithm;
import com.railsync.algorithm.m1.kmp.dto.KMPInput;
import com.railsync.algorithm.m1.kmp.dto.KMPResult;
import com.railsync.algorithm.m1.rabinkarp.RabinKarpAlgorithm;
import com.railsync.algorithm.m1.rabinkarp.dto.RabinKarpInput;
import com.railsync.algorithm.m1.rabinkarp.dto.RabinKarpResult;
import com.railsync.algorithm.m1.zfunction.ZFunctionAlgorithm;
import com.railsync.algorithm.m1.zfunction.dto.ZFunctionInput;
import com.railsync.algorithm.m1.zfunction.dto.ZFunctionResult;
import com.railsync.common.error.ApiException;
import com.railsync.common.error.ErrorCode;
import com.railsync.station.dto.StationDto;
import com.railsync.station.dto.StationSearchRequest;
import com.railsync.station.dto.StationSearchResponse;
import com.railsync.station.entity.Station;
import com.railsync.station.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StationSearchService {

    private final StationRepository stationRepository;
    private final KMPAlgorithm kmpAlgorithm;
    private final ZFunctionAlgorithm zFunctionAlgorithm;
    private final RabinKarpAlgorithm rabinKarpAlgorithm;
    private final AhoCorasickAlgorithm ahoCorasickAlgorithm;

    public StationSearchResponse searchStations(StationSearchRequest request) {
        if (request == null || request.getQuery() == null || request.getQuery().trim().isEmpty()) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Query parameter cannot be empty");
        }

        String queryStr = request.getQuery().trim();
        if (queryStr.length() > 100) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Query length exceeds maximum limit of 100 characters");
        }

        List<Station> stations = stationRepository.findAll();

        String originFilter = request.getDataOriginFilter() != null ? request.getDataOriginFilter().trim().toUpperCase() : "ALL";
        if (!"ALL".equals(originFilter)) {
            stations = stations.stream()
                    .filter(s -> originFilter.equalsIgnoreCase(s.getDataOrigin()))
                    .toList();
        }

        String algoStr = request.getAlgorithm() != null ? request.getAlgorithm().trim().toUpperCase() : "KMP";
        boolean traceEnabled = Boolean.TRUE.equals(request.getTraceEnabled());
        int maxTraceSteps = request.getMaxTraceSteps() != null ? request.getMaxTraceSteps() : 500;

        List<StationSearchResponse.StationMatchDto> matchedResults = new ArrayList<>();
        List<TraceStep> combinedTrace = new ArrayList<>();
        long startTime = System.nanoTime();

        for (Station station : stations) {
            String searchableText = String.format("CODE:%s NAME:%s CITY:%s STATE:%s",
                    station.getStationCode(), station.getName(), station.getCity(), station.getState());

            List<Integer> matchIndices = new ArrayList<>();
            List<TraceStep> stepTrace = new ArrayList<>();

            switch (algoStr) {
                case "Z_FUNCTION", "Z" -> {
                    ZFunctionResult zRes = zFunctionAlgorithm.execute(ZFunctionInput.builder()
                            .text(searchableText)
                            .pattern(queryStr)
                            .traceEnabled(traceEnabled)
                            .maxTraceSteps(maxTraceSteps)
                            .build());
                    if (zRes.getMatches() != null) matchIndices = zRes.getMatches();
                    if (zRes.getTrace() != null) stepTrace = zRes.getTrace();
                }
                case "RABIN_KARP" -> {
                    RabinKarpResult rkRes = rabinKarpAlgorithm.execute(RabinKarpInput.builder()
                            .text(searchableText)
                            .pattern(queryStr)
                            .traceEnabled(traceEnabled)
                            .maxTraceSteps(maxTraceSteps)
                            .build());
                    if (rkRes.getMatches() != null) matchIndices = rkRes.getMatches();
                    if (rkRes.getTrace() != null) stepTrace = rkRes.getTrace();
                }
                case "AHO_CORASICK" -> {
                    AhoCorasickResult acRes = ahoCorasickAlgorithm.execute(AhoCorasickInput.builder()
                            .text(searchableText)
                            .keywords(List.of(queryStr))
                            .traceEnabled(traceEnabled)
                            .maxTraceSteps(maxTraceSteps)
                            .build());
                    if (acRes.getMatches() != null) {
                        for (KeywordMatchDto km : acRes.getMatches()) {
                            matchIndices.add(km.getStartIndex());
                        }
                    }
                    if (acRes.getTrace() != null) stepTrace = acRes.getTrace();
                }
                default -> { // KMP default
                    KMPResult kmpRes = kmpAlgorithm.execute(KMPInput.builder()
                            .text(searchableText)
                            .pattern(queryStr)
                            .traceEnabled(traceEnabled)
                            .maxTraceSteps(maxTraceSteps)
                            .build());
                    if (kmpRes.getMatches() != null) matchIndices = kmpRes.getMatches();
                    if (kmpRes.getTrace() != null) stepTrace = kmpRes.getTrace();
                }
            }

            if (!matchIndices.isEmpty()) {
                matchedResults.add(StationSearchResponse.StationMatchDto.builder()
                        .station(StationDto.fromEntity(station))
                        .matchIndices(matchIndices)
                        .matchedField(searchableText)
                        .build());
            }

            if (combinedTrace.size() < maxTraceSteps && !stepTrace.isEmpty()) {
                combinedTrace.addAll(stepTrace.subList(0, Math.min(stepTrace.size(), maxTraceSteps - combinedTrace.size())));
            }
        }

        long executionTimeNanos = System.nanoTime() - startTime;

        return StationSearchResponse.builder()
                .query(queryStr)
                .algorithm(algoStr)
                .totalStationsSearched(stations.size())
                .matchedStationCount(matchedResults.size())
                .matches(matchedResults)
                .trace(combinedTrace)
                .executionTimeNanos(executionTimeNanos)
                .build();
    }
}
