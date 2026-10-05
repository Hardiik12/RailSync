package com.railsync.dataimport.normalizer;

import com.railsync.dataimport.dto.RawStationRecord;
import com.railsync.station.entity.Station;
import org.springframework.stereotype.Component;

@Component
public class StationNormalizer {

    public Station normalize(RawStationRecord raw, String defaultSourceDataset) {
        if (raw == null) {
            return null;
        }

        String code = raw.getStationCode() != null ? raw.getStationCode().trim().toUpperCase() : null;
        String name = raw.getName() != null ? raw.getName().trim() : null;
        
        String city = raw.getCity() != null && !raw.getCity().isBlank() 
                ? raw.getCity().trim() 
                : (name != null ? name : "UNKNOWN");

        String state = raw.getState() != null && !raw.getState().isBlank() 
                ? raw.getState().trim() 
                : "UNKNOWN";

        String status = raw.getStatus() != null && !raw.getStatus().isBlank() 
                ? raw.getStatus().trim().toUpperCase() 
                : "ACTIVE";

        Integer platformCount = (raw.getPlatformCount() != null && raw.getPlatformCount() > 0) 
                ? raw.getPlatformCount() 
                : 1;

        String sourceDataset = raw.getSourceDataset() != null && !raw.getSourceDataset().isBlank()
                ? raw.getSourceDataset().trim()
                : (defaultSourceDataset != null ? defaultSourceDataset : "PUBLIC_RAILWAY_DATASET");

        return Station.builder()
                .stationCode(code)
                .name(name)
                .city(city)
                .state(state)
                .platformCount(platformCount)
                .status(status)
                .dataOrigin("PUBLIC_DATA")
                .sourceDataset(sourceDataset)
                .latitude(raw.getLatitude())
                .longitude(raw.getLongitude())
                .build();
    }
}
