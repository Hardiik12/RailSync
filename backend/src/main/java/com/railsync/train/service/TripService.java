package com.railsync.train.service;

import com.railsync.common.error.ApiException;
import com.railsync.common.error.ErrorCode;
import com.railsync.train.dto.StopTimeDto;
import com.railsync.train.dto.TripDto;
import com.railsync.train.entity.Route;
import com.railsync.train.entity.RouteStop;
import com.railsync.train.entity.StopTime;
import com.railsync.train.entity.Train;
import com.railsync.train.entity.Trip;
import com.railsync.train.repository.RouteRepository;
import com.railsync.train.repository.RouteStopRepository;
import com.railsync.train.repository.StopTimeRepository;
import com.railsync.train.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TripService {

    private final TripRepository tripRepository;
    private final StopTimeRepository stopTimeRepository;
    private final RouteRepository routeRepository;
    private final RouteStopRepository routeStopRepository;

    @Transactional(readOnly = true)
    public Page<TripDto> searchTrips(String trainNumber, LocalDate serviceDate, Pageable pageable) {
        return tripRepository.searchTrips(trainNumber, serviceDate, pageable)
                .map(TripDto::fromEntity);
    }

    @Transactional(readOnly = true)
    public TripDto getTripById(Long id) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "Trip not found with id: " + id));
        List<StopTimeDto> stopTimes = getStopTimesByTripId(id);
        return TripDto.fromEntityWithStops(trip, stopTimes);
    }

    @Transactional(readOnly = true)
    public List<StopTimeDto> getStopTimesByTripId(Long tripId) {
        return stopTimeRepository.findByTripIdOrderByStopSequenceAsc(tripId)
                .stream()
                .map(StopTimeDto::fromEntity)
                .toList();
    }

    @Transactional
    public Trip generateTripForTrain(Train train, LocalDate serviceDate) {
        if (train == null || serviceDate == null) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Train and service date must not be null");
        }

        Optional<Trip> existingOpt = tripRepository.findByTrainIdAndServiceDate(train.getId(), serviceDate);
        Trip trip;
        if (existingOpt.isPresent()) {
            trip = existingOpt.get();
            log.info("Trip already exists for train {} on date {}", train.getTrainNumber(), serviceDate);
        } else {
            trip = Trip.builder()
                    .train(train)
                    .serviceDate(serviceDate)
                    .scheduledStatus("SCHEDULED")
                    .build();
            trip = tripRepository.save(trip);
        }

        List<Route> routes = routeRepository.findByTrainId(train.getId());
        if (!routes.isEmpty()) {
            Route route = routes.get(0);
            List<RouteStop> routeStops = routeStopRepository.findByRouteIdOrderByStopSequenceAsc(route.getId());

            List<StopTime> stopTimesToSave = new ArrayList<>();
            for (RouteStop rs : routeStops) {
                Optional<StopTime> existingStOpt = stopTimeRepository.findByTripIdAndStopSequence(trip.getId(), rs.getStopSequence());
                StopTime st;
                if (existingStOpt.isPresent()) {
                    st = existingStOpt.get();
                    st.setStation(rs.getStation());
                    st.setScheduledArrival(rs.getScheduledArrival());
                    st.setScheduledDeparture(rs.getScheduledDeparture());
                } else {
                    st = StopTime.builder()
                            .trip(trip)
                            .station(rs.getStation())
                            .stopSequence(rs.getStopSequence())
                            .scheduledArrival(rs.getScheduledArrival())
                            .scheduledDeparture(rs.getScheduledDeparture())
                            .arrivalDelayMinutes(0)
                            .departureDelayMinutes(0)
                            .build();
                }
                stopTimesToSave.add(st);
            }

            if (!stopTimesToSave.isEmpty()) {
                stopTimeRepository.saveAll(stopTimesToSave);
            }
        }

        return trip;
    }
}
