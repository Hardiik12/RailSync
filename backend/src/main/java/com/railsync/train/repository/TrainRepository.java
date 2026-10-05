package com.railsync.train.repository;

import com.railsync.train.entity.Train;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainRepository extends JpaRepository<Train, Long> {

    Optional<Train> findByTrainNumber(String trainNumber);

    boolean existsByTrainNumber(String trainNumber);

    Page<Train> findByTrainNumberContainingIgnoreCaseOrTrainNameContainingIgnoreCase(
            String trainNumber, String trainName, Pageable pageable);

    @Query("SELECT t FROM Train t WHERE " +
           "(:search IS NULL OR LOWER(t.trainNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(t.trainName) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:status IS NULL OR t.status = :status)")
    Page<Train> searchTrains(@Param("search") String search,
                             @Param("status") String status,
                             Pageable pageable);
}
