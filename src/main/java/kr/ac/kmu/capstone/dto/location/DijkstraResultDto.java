package kr.ac.kmu.capstone.dto.location;

import org.neo4j.driver.types.Path;

public class DijkstraResultDto {

    private Path path;
    private double weight;


    public DijkstraResultDto(Path path, double weight) {
        this.path = path;
        this.weight = weight;
    }

    public Path getPath() {
        return path;
    }

    public DijkstraResultDto setPath(Path path) {
        this.path = path;
        return this;
    }

    public double getWeight() {
        return weight;
    }

    public DijkstraResultDto setWeight(double weight) {
        this.weight = weight;
        return this;
    }
}
