package kr.ac.kmu.Capstone.entity;

import lombok.Getter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import reactor.core.publisher.Mono;

@Getter
@Node("point")
public class Location {

    @Id
    @GeneratedValue
    private Long ID;
    @Property(name="name")
    private String name;
    @Property(name="latitude")
    private double latitude;
    @Property(name="longitude")
    private double longitude;

    public Location() {
        this.name = null;
        this.latitude = 0.0;
        this.longitude = 0.0;
    }

    public Location(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public String getName() {
        return name;
    }

    public Location setName(String name) {
        this.name = name;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public Location setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public Location setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }
}