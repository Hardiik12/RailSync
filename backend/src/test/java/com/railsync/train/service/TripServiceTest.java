package com.railsync.train.service;

import com.railsync.common.error.ApiException;
import com.railsync.station.entity.Station;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripServiceTest {

    @Mock
    private TripRepository tripRepository;
    @Mock
    private StopTimeRepository stopTimeRepository;
    @Mock
    private RouteRepository routeRepository;
    @Mock
    private RouteStopRepository routeStopRepository;

    private TripService tripService;

    @BeforeEach
    void setUp() {
        tripService = new TripService(tripRepository, stopTimeRepository, routeRepository, routeStopRepository);
    }

    @Test
    @DisplayName("Successfully search trips with pagination")
    void testSearchTrips() {
        Train train = Train.builder().id(1L).trainNumber("12951").trainName("Rajdhani").build();
        Trip trip = Trip.builder().id(10L).train(train).serviceDate(LocalDate.now()).scheduledStatus("SCHEDULED").build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Trip> tripPage = new PageImpl<>(List.of(trip), pageable, 1);

        when(tripRepository.searchTrips(eq("12951"), any(), eq(pageable))).thenReturn(tripPage);

        Page<TripDto> result = tripService.searchTrips("12951", null, pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getTrain().getTrainNumber()).isEqualTo("12951");
    }

    @Test
    @DisplayName("Successfully fetch trip by ID with stop times")
    void testGetTripByIdSuccess() {
        Station ndls = Station.builder().id(1L).stationCode("NDLS").name("New Delhi").build();
        Train train = Train.builder().id(2L).trainNumber("12951").build();
        Trip trip = Trip.builder().id(10L).train(train).serviceDate(LocalDate.now()).scheduledStatus("SCHEDULED").build();
        StopTime stopTime = StopTime.builder().id(100L).trip(trip).station(ndls).stopSequence(1).scheduledArrival("16:55").scheduledDeparture("16:55").build();

        when(tripRepository.findById(10L)).thenReturn(Optional.of(trip));
        when(stopTimeRepository.findByTripIdOrderByStopSequenceAsc(10L)).thenReturn(List.of(stopTime));

        TripDto dto = tripService.getTripById(10L);

        assertThat(dto.getId()).isEqualTo(10L);
        assertThat(dto.getStopTimes()).hasSize(1);
        assertThat(dto.getStopTimes().get(0).getStation().getStationCode()).isEqualTo("NDLS");
    }

    @Test
    @DisplayName("Throw ApiException when trip ID is not found")
    void testGetTripByIdNotFound() {
        when(tripRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tripService.getTripById(999L))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("Trip not found");
    }

    @Test
    @DisplayName("Successfully generate trip and stop times from train route")
    void testGenerateTripForTrain() {
        Station ndls = Station.builder().id(1L).stationCode("NDLS").build();
        Train train = Train.builder().id(5L).trainNumber("12951").build();
        Route route = Route.builder().id(20L).train(train).build();
        RouteStop routeStop = RouteStop.builder().id(200L).route(route).station(ndls).stopSequence(1).scheduledArrival("16:55").scheduledDeparture("16:55").build();

        LocalDate serviceDate = LocalDate.of(2026, 10, 5);

        when(tripRepository.findByTrainIdAndServiceDate(5L, serviceDate)).thenReturn(Optional.empty());

        Trip newTrip = Trip.builder().id(50L).train(train).serviceDate(serviceDate).scheduledStatus("SCHEDULED").build();
        when(tripRepository.save(any(Trip.class))).thenReturn(newTrip);
        when(routeRepository.findByTrainId(5L)).thenReturn(List.of(route));
        when(routeStopRepository.findByRouteIdOrderByStopSequenceAsc(20L)).thenReturn(List.of(routeStop));

        Trip generated = tripService.generateTripForTrain(train, serviceDate);

        assertThat(generated).isNotNull();
        assertThat(generated.getId()).isEqualTo(50L);
        verify(stopTimeRepository, times(1)).saveAll(any());
    }
}
