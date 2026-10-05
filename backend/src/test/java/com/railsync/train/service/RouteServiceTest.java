package com.railsync.train.service;

import com.railsync.station.entity.Station;
import com.railsync.train.dto.RouteDto;
import com.railsync.train.dto.RouteStopDto;
import com.railsync.train.entity.Route;
import com.railsync.train.entity.RouteStop;
import com.railsync.train.entity.Train;
import com.railsync.train.repository.RouteRepository;
import com.railsync.train.repository.RouteStopRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RouteServiceTest {

    @Mock
    private RouteRepository routeRepository;
    @Mock
    private RouteStopRepository routeStopRepository;

    private RouteService routeService;

    @BeforeEach
    void setUp() {
        routeService = new RouteService(routeRepository, routeStopRepository);
    }

    @Test
    @DisplayName("Fetch route by train number successfully")
    void testGetRouteByTrainNumberSuccess() {
        Train train = Train.builder().id(1L).trainNumber("12951").trainName("Rajdhani").build();
        Route route = Route.builder().id(10L).train(train).routeName("Main Route").distanceKm(1384.0).build();

        when(routeRepository.findFirstByTrainTrainNumber("12951")).thenReturn(Optional.of(route));

        RouteDto result = routeService.getRouteByTrainNumber("12951");

        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getTrainNumber()).isEqualTo("12951");
        assertThat(result.getRouteName()).isEqualTo("Main Route");
    }

    @Test
    @DisplayName("Fetch route stops ordered by sequence")
    void testGetRouteStops() {
        Station ndls = Station.builder().stationCode("NDLS").name("New Delhi").build();
        Station csmt = Station.builder().stationCode("CSMT").name("Mumbai CSMT").build();

        RouteStop stop1 = RouteStop.builder().id(101L).station(ndls).stopSequence(1).build();
        RouteStop stop2 = RouteStop.builder().id(102L).station(csmt).stopSequence(2).build();

        when(routeStopRepository.findByRouteIdOrderByStopSequenceAsc(10L)).thenReturn(List.of(stop1, stop2));

        List<RouteStopDto> stops = routeService.getRouteStops(10L);

        assertThat(stops).hasSize(2);
        assertThat(stops.get(0).getStopSequence()).isEqualTo(1);
        assertThat(stops.get(1).getStopSequence()).isEqualTo(2);
    }
}
