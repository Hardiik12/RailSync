package com.railsync.dataimport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspectionReport {
    private int totalRecords;
    private int validRecords;
    private int duplicateIdentifiers;
    private int missingRequiredFields;
    private int invalidCoordinates;
    private int invalidStationReferences;

    @Builder.Default
    private List<String> warnings = new ArrayList<>();

    @Builder.Default
    private List<String> errors = new ArrayList<>();
}
