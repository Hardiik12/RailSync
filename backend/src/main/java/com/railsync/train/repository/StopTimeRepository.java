package com.railsync.train.repository;

import com.railsync.train.entity.StopTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StopTimeRepository extends JpaRepository<StopTime, Long> {

    List<StopTime> findByTripIdOrderByStopSequenceAsc(Long tripId);

    Optional<StopTime> findByTripIdAndStationId(Long tripId, Long stationId);

    Optional<StopTime> findByTripIdAndStopSequence(Long tripId, Integer stopSequence);

    void deleteByTripId(Long tripId);
}
