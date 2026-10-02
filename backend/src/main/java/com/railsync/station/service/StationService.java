package com.railsync.station.service;

import com.railsync.common.api.PageMeta;

import com.railsync.common.error.ApiException;
import com.railsync.common.error.ErrorCode;
import com.railsync.station.dto.StationDto;
import com.railsync.station.entity.Station;
import com.railsync.station.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StationService {

    private final StationRepository stationRepository;

    public Map<String, Object> getStations(int page, int size, String search, String sortBy, String sortDir) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir != null ? sortDir : "ASC"),
                sortBy != null ? sortBy : "stationCode");

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Station> stationPage;

        if (search != null && !search.trim().isEmpty()) {
            stationPage = stationRepository.searchStations(search.trim(), pageable);
        } else {
            stationPage = stationRepository.findAll(pageable);
        }

        List<StationDto> items = stationPage.getContent().stream()
                .map(StationDto::fromEntity)
                .toList();

        PageMeta pageMeta = PageMeta.builder()
                .page(stationPage.getNumber())
                .size(stationPage.getSize())
                .totalElements(stationPage.getTotalElements())
                .totalPages(stationPage.getTotalPages())
                .first(stationPage.isFirst())
                .last(stationPage.isLast())
                .build();

        Map<String, Object> result = new HashMap<>();
        result.put("items", items);
        result.put("pagination", pageMeta);
        return result;
    }

    public StationDto getStationByCode(String stationCode) {
        Station station = stationRepository.findByStationCode(stationCode)
                .orElseThrow(() -> new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "Station with code " + stationCode + " not found"));
        return StationDto.fromEntity(station);
    }
}
