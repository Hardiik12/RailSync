package com.railsync.dataimport;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ImportResult {
    private final String source;
    private final String entity;
    private final int recordsRead;
    private final int recordsInserted;
    private final int recordsUpdated;
    private final int duplicates;
    private final int invalidRecords;
    private final int warnings;
    private final long durationMillis;
}
