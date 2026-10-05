package com.railsync.train.entity;

import com.railsync.station.entity.Station;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "route_stop", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"route_id", "stop_sequence"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteStop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @Column(name = "stop_sequence", nullable = false)
    private Integer stopSequence;

    @Column(name = "distance_from_origin")
    private Double distanceFromOrigin;

    @Column(name = "scheduled_dwell_minutes")
    @Builder.Default
    private Integer scheduledDwellMinutes = 2;

    @Column(name = "scheduled_arrival", length = 20)
    private String scheduledArrival;

    @Column(name = "scheduled_departure", length = 20)
    private String scheduledDeparture;
}
