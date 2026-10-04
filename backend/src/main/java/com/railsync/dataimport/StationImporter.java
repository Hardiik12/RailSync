package com.railsync.dataimport;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.railsync.station.entity.Station;
import com.railsync.station.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StationImporter {
    private static final String SOURCE_DATASET = "Indian Railways Dataset";
    private static final String SOURCE_TYPE = "KAGGLE_INDIAN_RAILWAYS";
    private static final int MAX_FEATURES = 250_000;

    private final ObjectMapper objectMapper;
    private final StationRepository stationRepository;

    @Transactional
    public ImportResult importGeoJson(Path file) {
        long started = System.currentTimeMillis();
        int read=0, inserted=0, updated=0, duplicates=0, invalid=0, warnings=0;
        Set<String> seen = new HashSet<>();

        try {
            JsonNode root = objectMapper.readTree(file.toFile());
            if (!"FeatureCollection".equals(root.path("type").asText()))
                throw new IllegalArgumentException("Expected GeoJSON FeatureCollection");
            JsonNode features = root.path("features");
            if (!features.isArray()) throw new IllegalArgumentException("GeoJSON features must be an array");
            if (features.size() > MAX_FEATURES) throw new IllegalArgumentException("Station dataset exceeds "+MAX_FEATURES+" features");

            for (JsonNode feature : features) {
                read++;
                Record r = parse(feature);
                if (r == null) { invalid++; continue; }
                if (!seen.add(r.code())) { duplicates++; continue; }

                var existing = stationRepository.findByStationCode(r.code());
                Station s = existing.orElseGet(Station::new);
                boolean wasExisting = existing.isPresent();

                s.setStationCode(r.code());
                s.setName(r.name());
                s.setCity(r.city());
                s.setState(r.state());
                s.setLatitude(r.latitude());
                s.setLongitude(r.longitude());
                s.setZone(r.zone());
                s.setAddress(r.address());
                s.setDataOrigin("PUBLIC_DATA");
                s.setSourceDataset(SOURCE_DATASET);
                stationRepository.save(s);

                if (wasExisting) updated++; else inserted++;
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to read station GeoJSON: " + e.getMessage(), e);
        }

        return ImportResult.builder().source(SOURCE_TYPE).entity("STATION")
                .recordsRead(read).recordsInserted(inserted).recordsUpdated(updated)
                .duplicates(duplicates).invalidRecords(invalid).warnings(warnings)
                .durationMillis(System.currentTimeMillis()-started).build();
    }

    private Record parse(JsonNode feature) {
        if (!"Feature".equals(feature.path("type").asText())) return null;
        JsonNode p=feature.path("properties");
        String code=norm(p.path("code").asText(null));
        String name=norm(p.path("name").asText(null));
        String state=norm(p.path("state").asText(null));
        if(code==null || name==null || state==null) return null;

        Double lat=null, lon=null;
        JsonNode c=feature.path("geometry").path("coordinates");
        if(c.isArray() && c.size()>=2 && c.get(0).isNumber() && c.get(1).isNumber()) {
            lon=c.get(0).doubleValue(); lat=c.get(1).doubleValue();
            if(lon < -180 || lon > 180 || lat < -90 || lat > 90) return null;
        }
        return new Record(code,name,norm(p.path("city").asText(null)),state,lat,lon,
                norm(p.path("zone").asText(null)),norm(p.path("address").asText(null)));
    }

    private String norm(String value) {
        if(value==null) return null;
        String v=value.trim().replaceAll("\\s+"," ");
        return v.isEmpty()?null:v;
    }

    private record Record(String code,String name,String city,String state,Double latitude,Double longitude,String zone,String address) {}
}
