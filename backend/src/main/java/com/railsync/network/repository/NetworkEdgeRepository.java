package com.railsync.network.repository;

import com.railsync.network.entity.NetworkEdge;
import com.railsync.station.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NetworkEdgeRepository extends JpaRepository<NetworkEdge, Long> {

    Optional<NetworkEdge> findByFromStationAndToStation(Station fromStation, Station toStation);

    boolean existsByFromStationAndToStation(Station fromStation, Station toStation);

    @Query("SELECT e FROM NetworkEdge e JOIN FETCH e.fromStation JOIN FETCH e.toStation")
    List<NetworkEdge> findAllWithStations();

    long countByDataOrigin(String dataOrigin);
}
