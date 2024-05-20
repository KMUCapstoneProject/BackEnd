package kr.ac.kmu.capstone.entity;

import lombok.Getter;
import org.neo4j.driver.types.Path;
import org.springframework.data.neo4j.core.schema.*;

import java.util.Set;

@Getter
@Node("point")
public class Point {

    @Id
    @GeneratedValue
    private Long ID;
    @Property(name="name")
    private String name;
    @Property(name="latitude")
    private double latitude;
    @Property(name="longitude")
    private double longitude;

    @Relationship(type = "status")
    private Set<Path> paths;

    public Point() {
        this.name = null;
        this.latitude = 0.0;
        this.longitude = 0.0;
    }

    public Point(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public String getName() {
        return name;
    }

    public Point setName(String name) {
        this.name = name;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public Point setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public Point setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public Set<Path> getPaths() {
        return paths;
    }

    public void setPaths(Set<Path> paths) {
        this.paths = paths;
    }
}