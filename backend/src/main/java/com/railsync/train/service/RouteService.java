package com.railsync.train.service;

import com.railsync.common.error.ApiException;
import com.railsync.common.error.ErrorCode;
import com.railsync.train.dto.RouteDto;
import com.railsync.train.dto.RouteStopDto;
import com.railsync.train.entity.Route;
import com.railsync.train.entity.RouteStop;
import com.railsync.train.repository.RouteRepository;
import com.railsync.train.repository.RouteStopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RouteService {

    private final RouteRepository routeRepository;
    private final RouteStopRepository routeStopRepository;

    public List<RouteDto> getRoutesByTrainId(Long trainId) {
        List<Route> routes = routeRepository.findByTrainId(trainId);
        return routes.stream().map(RouteDto::fromEntity).toList();
    }

    public RouteDto getRouteByTrainNumber(String trainNumber) {
        Route route = routeRepository.findFirstByTrainTrainNumber(trainNumber.trim().toUpperCase())
                .orElseThrow(() -> new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "Route not found for train number: " + trainNumber));
        return RouteDto.fromEntity(route);
    }

    public List<RouteStopDto> getRouteStops(Long routeId) {
        List<RouteStop> stops = routeStopRepository.findByRouteIdOrderByStopSequenceAsc(routeId);
        return stops.stream().map(RouteStopDto::fromEntity).toList();
    }
}
