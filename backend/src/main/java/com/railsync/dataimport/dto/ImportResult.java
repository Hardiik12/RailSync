package com.railsync.dataimport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportResult {
    private int recordsRead;
    private int recordsInserted;
    private int recordsUpdated;
    private int duplicates;
    private int invalidRecords;
    private long durationMillis;
}
