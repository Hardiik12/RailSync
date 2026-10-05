package com.railsync.train.service;

import com.railsync.common.error.ApiException;
import com.railsync.common.error.ErrorCode;
import com.railsync.train.dto.TrainDto;
import com.railsync.train.entity.Train;
import com.railsync.train.repository.TrainRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TrainService {

    private final TrainRepository trainRepository;

    public Page<TrainDto> getTrains(String search, String status, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        String cleanSearch = (search != null && !search.isBlank()) ? search.trim() : null;
        String cleanStatus = (status != null && !status.isBlank() && !"ALL".equalsIgnoreCase(status)) ? status.trim().toUpperCase() : null;

        Page<Train> trainPage = trainRepository.searchTrains(cleanSearch, cleanStatus, pageable);
        return trainPage.map(TrainDto::fromEntity);
    }

    public TrainDto getTrainByNumber(String trainNumber) {
        if (trainNumber == null || trainNumber.isBlank()) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Train number cannot be empty");
        }
        Train train = trainRepository.findByTrainNumber(trainNumber.trim().toUpperCase())
                .orElseThrow(() -> new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "Train not found with number: " + trainNumber));
        return TrainDto.fromEntity(train);
    }

    public TrainDto getTrainById(Long id) {
        Train train = trainRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "Train not found with id: " + id));
        return TrainDto.fromEntity(train);
    }
}
