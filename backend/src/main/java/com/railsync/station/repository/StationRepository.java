package com.railsync.station.repository;

import com.railsync.station.entity.Station;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {

    Optional<Station> findByStationCode(String stationCode);

    @Query("SELECT s FROM Station s WHERE " +
           "LOWER(s.stationCode) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(s.city) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(s.state) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Station> searchStations(@Param("search") String search, Pageable pageable);
}
