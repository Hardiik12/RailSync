package com.railsync.station.service;

import com.railsync.common.api.PageMeta;
import com.railsync.station.dto.StationDto;
import com.railsync.station.entity.Station;
import com.railsync.station.repository.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StationServiceTest {

    @Mock
    private StationRepository stationRepository;

    @InjectMocks
    private StationService stationService;

    private Station sampleStation;

    @BeforeEach
    void setUp() {
        sampleStation = Station.builder()
                .id(1L)
                .stationCode("BZA")
                .name("Vijayawada Junction")
                .city("Vijayawada")
                .state("Andhra Pradesh")
                .platformCount(10)
                .status("ACTIVE")
                .build();
    }

    @Test
    @DisplayName("Should return paginated stations list")
    void shouldReturnPaginatedStations() {
        Page<Station> page = new PageImpl<>(List.of(sampleStation), PageRequest.of(0, 10), 1);
        when(stationRepository.findAll(any(Pageable.class))).thenReturn(page);

        Map<String, Object> result = stationService.getStations(0, 10, null, "stationCode", "ASC");

        assertThat(result).containsKey("items");
        assertThat(result).containsKey("pagination");

        List<?> items = (List<?>) result.get("items");
        assertThat(items).hasSize(1);

        PageMeta meta = (PageMeta) result.get("pagination");
        assertThat(meta.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should return station details when code exists")
    void shouldReturnStationByCode() {
        when(stationRepository.findByStationCode("BZA")).thenReturn(Optional.of(sampleStation));

        StationDto dto = stationService.getStationByCode("BZA");

        assertThat(dto).isNotNull();
        assertThat(dto.getStationCode()).isEqualTo("BZA");
        assertThat(dto.getName()).isEqualTo("Vijayawada Junction");
    }
}
