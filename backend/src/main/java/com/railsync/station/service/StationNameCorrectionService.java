package com.railsync.station.service;

import com.railsync.algorithm.common.TraceStep;
import com.railsync.algorithm.m3.damerau.DamerauLevenshteinAlgorithm;
import com.railsync.algorithm.m3.damerau.dto.DamerauLevenshteinInput;
import com.railsync.algorithm.m3.damerau.dto.DamerauLevenshteinResult;
import com.railsync.algorithm.m3.levenshtein.LevenshteinAlgorithm;
import com.railsync.algorithm.m3.levenshtein.dto.LevenshteinInput;
import com.railsync.algorithm.m3.levenshtein.dto.LevenshteinResult;
import com.railsync.common.error.ApiException;
import com.railsync.common.error.ErrorCode;
import com.railsync.station.dto.StationCorrectionRequest;
import com.railsync.station.dto.StationCorrectionResponse;
import com.railsync.station.dto.StationDto;
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
public class StationNameCorrectionService {

    private final StationRepository stationRepository;
    private final LevenshteinAlgorithm levenshteinAlgorithm;
    private final DamerauLevenshteinAlgorithm damerauLevenshteinAlgorithm;

    public StationCorrectionResponse correctStationName(StationCorrectionRequest request) {
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

        String algoStr = request.getAlgorithm() != null ? request.getAlgorithm().trim().toUpperCase() : "LEVENSHTEIN";
        boolean useDamerau = "DAMERAU_LEVENSHTEIN".equals(algoStr) || "DAMERAU".equals(algoStr);

        int maxCandidates = request.getMaxCandidates() != null ? request.getMaxCandidates() : 5;
        if (maxCandidates < 1 || maxCandidates > 20) {
            maxCandidates = 5;
        }

        boolean traceEnabled = Boolean.TRUE.equals(request.getTraceEnabled());
        int maxTraceSteps = request.getMaxTraceSteps() != null ? request.getMaxTraceSteps() : 500;

        List<StationCorrectionResponse.CandidateMatchDto> candidateList = new ArrayList<>();
        List<TraceStep> traceSteps = new ArrayList<>();
        long startTime = System.nanoTime();

        for (Station station : stations) {
            int distName;
            int distCity = Integer.MAX_VALUE;
            int distCode;
            List<TraceStep> stepTrace = new ArrayList<>();

            if (useDamerau) {
                DamerauLevenshteinResult r1 = damerauLevenshteinAlgorithm.execute(DamerauLevenshteinInput.builder()
                        .source(queryStr)
                        .target(station.getName())
                        .traceEnabled(traceEnabled && traceSteps.isEmpty())
                        .maxTraceSteps(maxTraceSteps)
                        .build());
                distName = r1.getDistance();
                if (r1.getTrace() != null) stepTrace = r1.getTrace();

                if (station.getCity() != null) {
                    distCity = damerauLevenshteinAlgorithm.execute(DamerauLevenshteinInput.builder()
                            .source(queryStr)
                            .target(station.getCity())
                            .build()).getDistance();
                }

                DamerauLevenshteinResult r2 = damerauLevenshteinAlgorithm.execute(DamerauLevenshteinInput.builder()
                        .source(queryStr)
                        .target(station.getStationCode())
                        .build());
                distCode = r2.getDistance();
            } else {
                LevenshteinResult r1 = levenshteinAlgorithm.execute(LevenshteinInput.builder()
                        .source(queryStr)
                        .target(station.getName())
                        .traceEnabled(traceEnabled && traceSteps.isEmpty())
                        .maxTraceSteps(maxTraceSteps)
                        .build());
                distName = r1.getDistance();
                if (r1.getTrace() != null) stepTrace = r1.getTrace();

                if (station.getCity() != null) {
                    distCity = levenshteinAlgorithm.execute(LevenshteinInput.builder()
                            .source(queryStr)
                            .target(station.getCity())
                            .build()).getDistance();
                }

                LevenshteinResult r2 = levenshteinAlgorithm.execute(LevenshteinInput.builder()
                        .source(queryStr)
                        .target(station.getStationCode())
                        .build());
                distCode = r2.getDistance();
            }

            int minDist = Math.min(distName, Math.min(distCity, distCode));
            String matchedField = station.getName();
            if (minDist == distCity) {
                matchedField = station.getCity();
            } else if (minDist == distCode) {
                matchedField = station.getStationCode();
            }

            candidateList.add(StationCorrectionResponse.CandidateMatchDto.builder()
                    .station(StationDto.fromEntity(station))
                    .distance(minDist)
                    .matchedField(matchedField)
                    .dataOrigin(station.getDataOrigin())
                    .build());

            if (traceSteps.isEmpty() && !stepTrace.isEmpty()) {
                traceSteps.addAll(stepTrace);
            }
        }

        // Sort candidates by edit distance ascending
        candidateList.sort(Comparator.comparingInt(StationCorrectionResponse.CandidateMatchDto::getDistance));

        List<StationCorrectionResponse.CandidateMatchDto> topCandidates = candidateList.subList(0, Math.min(candidateList.size(), maxCandidates));
        long executionTimeNanos = System.nanoTime() - startTime;

        return StationCorrectionResponse.builder()
                .query(queryStr)
                .algorithm(useDamerau ? "DAMERAU_LEVENSHTEIN" : "LEVENSHTEIN")
                .totalStationsEvaluated(stations.size())
                .candidates(topCandidates)
                .trace(traceSteps)
                .executionTimeNanos(executionTimeNanos)
                .build();
    }
}
