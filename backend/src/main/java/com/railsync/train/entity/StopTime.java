package com.railsync.train.entity;

import com.railsync.station.entity.Station;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stop_time", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"trip_id", "stop_sequence"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StopTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @Column(name = "stop_sequence", nullable = false)
    private Integer stopSequence;

    @Column(name = "scheduled_arrival", length = 20)
    private String scheduledArrival;

    @Column(name = "scheduled_departure", length = 20)
    private String scheduledDeparture;

    @Column(name = "actual_arrival", length = 20)
    private String actualArrival;

    @Column(name = "actual_departure", length = 20)
    private String actualDeparture;

    @Column(name = "arrival_delay_minutes")
    @Builder.Default
    private Integer arrivalDelayMinutes = 0;

    @Column(name = "departure_delay_minutes")
    @Builder.Default
    private Integer departureDelayMinutes = 0;
}
