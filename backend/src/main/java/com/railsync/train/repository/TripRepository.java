package com.railsync.train.repository;

import com.railsync.train.entity.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    List<Trip> findByTrainId(Long trainId);

    Page<Trip> findByServiceDate(LocalDate serviceDate, Pageable pageable);

    Optional<Trip> findByTrainIdAndServiceDate(Long trainId, LocalDate serviceDate);

    Optional<Trip> findByTrainTrainNumberAndServiceDate(String trainNumber, LocalDate serviceDate);

    @Query("SELECT t FROM Trip t WHERE " +
           "(:trainNumber IS NULL OR LOWER(t.train.trainNumber) LIKE LOWER(CONCAT('%', :trainNumber, '%'))) AND " +
           "(:serviceDate IS NULL OR t.serviceDate = :serviceDate)")
    Page<Trip> searchTrips(@Param("trainNumber") String trainNumber,
                           @Param("serviceDate") LocalDate serviceDate,
                           Pageable pageable);
}
