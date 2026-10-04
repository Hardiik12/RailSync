package com.railsync.document.service;

import com.railsync.document.domain.RailwayDocument;
import com.railsync.document.dto.StationIndexResponse;
import com.railsync.document.repository.RailwayDocumentRepository;
import com.railsync.station.entity.Station;
import com.railsync.station.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StationDocumentIndexService {

    private final StationRepository stationRepository;
    private final RailwayDocumentRepository railwayDocumentRepository;

    @Transactional
    public StationIndexResponse indexStationDocuments() {
        long startTime = System.currentTimeMillis();
        List<Station> stations = stationRepository.findAll();

        int recordsConsidered = stations.size();
        int documentsCreated = 0;
        int documentsUpdated = 0;
        int skippedRecords = 0;

        for (Station station : stations) {
            String docIdentifier = "STATION_DOC_" + station.getStationCode().trim().toUpperCase();
            String title = "Station Record: " + station.getName();
            String docType = "STATION_RECORD";

            StringBuilder contentBuilder = new StringBuilder();
            contentBuilder.append("Station: ").append(station.getName()).append("\n");
            contentBuilder.append("Code: ").append(station.getStationCode()).append("\n");
            contentBuilder.append("City: ").append(station.getCity()).append("\n");
            contentBuilder.append("State: ").append(station.getState()).append("\n font");
            contentBuilder.append("Platforms: ").append(station.getPlatformCount()).append("\n");
            contentBuilder.append("Status: ").append(station.getStatus()).append("\n");
            contentBuilder.append("Origin: ").append(station.getDataOrigin());

            String deterministicContent = contentBuilder.toString();

            Optional<RailwayDocument> existingOpt = railwayDocumentRepository.findByDocIdentifier(docIdentifier);

            if (existingOpt.isPresent()) {
                RailwayDocument existing = existingOpt.get();
                if (deterministicContent.equals(existing.getContent()) && title.equals(existing.getTitle())) {
                    skippedRecords++;
                } else {
                    existing.setTitle(title);
                    existing.setContent(deterministicContent);
                    existing.setStatus("INDEXED");
                    railwayDocumentRepository.save(existing);
                    documentsUpdated++;
                }
            } else {
                RailwayDocument newDoc = RailwayDocument.builder()
                        .docIdentifier(docIdentifier)
                        .title(title)
                        .docType(docType)
                        .content(deterministicContent)
                        .status("INDEXED")
                        .build();
                railwayDocumentRepository.save(newDoc);
                documentsCreated++;
            }
        }

        long durationMillis = System.currentTimeMillis() - startTime;
        return StationIndexResponse.builder()
                .recordsConsidered(recordsConsidered)
                .documentsCreated(documentsCreated)
                .documentsUpdated(documentsUpdated)
                .skippedRecords(skippedRecords)
                .durationMillis(durationMillis)
                .source("STATION_TABLE_HYBRID")
                .build();
    }
}
