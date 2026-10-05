package com.railsync.train.repository;

import com.railsync.train.entity.RouteStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteStopRepository extends JpaRepository<RouteStop, Long> {

    List<RouteStop> findByRouteIdOrderByStopSequenceAsc(Long routeId);

    List<RouteStop> findByStationId(Long stationId);
}
