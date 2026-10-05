package com.railsync.dataimport.inspector;

import com.railsync.dataimport.dto.InspectionReport;
import com.railsync.dataimport.dto.RawStationRecord;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DatasetInspector {

    public InspectionReport inspectStations(List<RawStationRecord> records) {
        if (records == null || records.isEmpty()) {
            return InspectionReport.builder()
                    .totalRecords(0)
                    .validRecords(0)
                    .build();
        }

        int totalRecords = records.size();
        int validRecords = 0;
        int duplicateIdentifiers = 0;
        int missingRequiredFields = 0;
        int invalidCoordinates = 0;

        List<String> warnings = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        Set<String> seenCodes = new HashSet<>();

        for (int i = 0; i < records.size(); i++) {
            RawStationRecord rec = records.get(i);
            boolean isValid = true;

            if (rec == null) {
                missingRequiredFields++;
                errors.add("Record at index " + i + " is null");
                continue;
            }

            // Code and name validation
            if (rec.getStationCode() == null || rec.getStationCode().trim().isEmpty()) {
                missingRequiredFields++;
                errors.add("Record index " + i + " missing stationCode");
                isValid = false;
            }

            if (rec.getName() == null || rec.getName().trim().isEmpty()) {
                missingRequiredFields++;
                errors.add("Record index " + i + " missing name");
                isValid = false;
            }

            // Duplicate identifier check
            if (rec.getStationCode() != null && !rec.getStationCode().trim().isEmpty()) {
                String codeKey = rec.getStationCode().trim().toUpperCase();
                if (seenCodes.contains(codeKey)) {
                    duplicateIdentifiers++;
                    warnings.add("Duplicate stationCode detected: " + codeKey + " at index " + i);
                    isValid = false;
                } else {
                    seenCodes.add(codeKey);
                }
            }

            // Coordinate validation
            if (rec.getLatitude() != null || rec.getLongitude() != null) {
                Double lat = rec.getLatitude();
                Double lng = rec.getLongitude();
                if (lat == null || lng == null || lat < -90.0 || lat > 90.0 || lng < -180.0 || lng > 180.0) {
                    invalidCoordinates++;
                    warnings.add("Invalid coordinates for stationCode " + rec.getStationCode() + ": lat=" + lat + ", lng=" + lng);
                    isValid = false;
                }
            }

            if (isValid) {
                validRecords++;
            }
        }

        return InspectionReport.builder()
                .totalRecords(totalRecords)
                .validRecords(validRecords)
                .duplicateIdentifiers(duplicateIdentifiers)
                .missingRequiredFields(missingRequiredFields)
                .invalidCoordinates(invalidCoordinates)
                .warnings(warnings)
                .errors(errors)
                .build();
    }
}
