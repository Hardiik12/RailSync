package com.railsync.station.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.OffsetDateTime;

@Entity
@Table(name = "station")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Station {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name="station_code", nullable=false, unique=true, length=20) private String stationCode;
    @Column(name="name", nullable=false, length=150) private String name;
    @Column(name="city", length=100) private String city;
    @Column(name="state", nullable=false, length=100) private String state;
    @Column(name="latitude", precision=9, scale=6) private Double latitude;
    @Column(name="longitude", precision=9, scale=6) private Double longitude;
    @Column(name="zone", length=20) private String zone;
    @Column(name="address", length=300) private String address;
    @Column(name="platform_count") private Integer platformCount;
    @Column(name="status", length=30) private String status;
    @Column(name="data_origin", nullable=false, length=30) private String dataOrigin;
    @Column(name="source_dataset", length=200) private String sourceDataset;
    @CreationTimestamp @Column(name="created_at", nullable=false, updatable=false) private OffsetDateTime createdAt;
    @UpdateTimestamp @Column(name="updated_at", nullable=false) private OffsetDateTime updatedAt;
}