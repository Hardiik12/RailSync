package com.railsync.train.repository;

import com.railsync.train.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {

    List<Route> findByTrainId(Long trainId);

    Optional<Route> findFirstByTrainTrainNumber(String trainNumber);
}
