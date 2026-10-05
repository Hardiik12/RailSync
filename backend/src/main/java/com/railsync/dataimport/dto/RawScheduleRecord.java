package com.railsync.dataimport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RawScheduleRecord {
    private String trainNumber;
    private String trainName;
    private List<RawStopRecord> stops;
}
