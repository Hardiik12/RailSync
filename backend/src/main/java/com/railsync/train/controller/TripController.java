package com.railsync.train.controller;

import com.railsync.common.api.ApiResponse;
import com.railsync.train.dto.StopTimeDto;
import com.railsync.train.dto.TripDto;
import com.railsync.train.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<TripDto>>> getTrips(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String trainNumber,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate serviceDate,
            @RequestParam(defaultValue = "serviceDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<TripDto> trips = tripService.searchTrips(trainNumber, serviceDate, pageable);
        return ResponseEntity.ok(ApiResponse.success(trips));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TripDto>> getTripById(@PathVariable Long id) {
        TripDto trip = tripService.getTripById(id);
        return ResponseEntity.ok(ApiResponse.success(trip));
    }

    @GetMapping("/{id}/stop-times")
    public ResponseEntity<ApiResponse<List<StopTimeDto>>> getStopTimesByTripId(@PathVariable Long id) {
        List<StopTimeDto> stopTimes = tripService.getStopTimesByTripId(id);
        return ResponseEntity.ok(ApiResponse.success(stopTimes));
    }
}
