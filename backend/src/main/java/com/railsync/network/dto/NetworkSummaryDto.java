package com.railsync.network.dto;

import com.railsync.station.dto.StationDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class NetworkSummaryDto {
    private final List<StationDto> stations;
    private final List<NetworkEdgeDto> edges;
    private final int stationCount;
    private final int edgeCount;
    private final int publicStationCount;
    private final int syntheticStationCount;
    private final int publicEdgeCount;
    private final int syntheticEdgeCount;
}
