package com.railsync.train.controller;

import com.railsync.common.api.ApiResponse;
import com.railsync.train.dto.RouteDto;
import com.railsync.train.dto.RouteStopDto;
import com.railsync.train.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    @GetMapping
    public ResponseEntity<ApiResponse<RouteDto>> getRouteByTrainNumber(@RequestParam String trainNumber) {
        RouteDto route = routeService.getRouteByTrainNumber(trainNumber);
        return ResponseEntity.ok(ApiResponse.success(route));
    }

    @GetMapping("/{id}/stops")
    public ResponseEntity<ApiResponse<List<RouteStopDto>>> getRouteStops(@PathVariable Long id) {
        List<RouteStopDto> stops = routeService.getRouteStops(id);
        return ResponseEntity.ok(ApiResponse.success(stops));
    }
}
