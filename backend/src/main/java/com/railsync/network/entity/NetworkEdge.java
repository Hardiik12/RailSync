package com.railsync.network.entity;

import com.railsync.station.entity.Station;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "network_edge", uniqueConstraints = {
    @UniqueConstraint(name = "uq_from_to_station", columnNames = {"from_station_id", "to_station_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NetworkEdge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "from_station_id", nullable = false)
    private Station fromStation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "to_station_id", nullable = false)
    private Station toStation;

    @Column(name = "capacity", nullable = false)
    private Double capacity;

    @Column(name = "distance_km", nullable = false)
    private Double distanceKm;

    @Column(name = "travel_time_minutes", nullable = false)
    private Integer travelTimeMinutes;

    @Column(name = "data_origin", nullable = false, length = 50)
    @Builder.Default
    private String dataOrigin = "SYNTHETIC";

    @Column(name = "source_dataset", length = 150)
    private String sourceDataset;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
