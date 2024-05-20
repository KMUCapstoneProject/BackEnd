package kr.ac.kmu.capstone.entity;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@RelationshipProperties
public class Status {
    @Id
    @GeneratedValue
    private Long ID;
    private String value;
    private double weight;

    @TargetNode
    private Point destination;

    public Status() {
    }

    public Status(String value, double weight, Point destination) {
        this.value = value;
        this.weight = weight;
        this.destination = destination;
    }

    public Long getId() {
        return ID;
    }

    public Status setId(long id) {
        this.ID = id;
        return this;
    }

    public String getValue() {
        return value;
    }

    public Status setValue(String value) {
        this.value = value;
        return this;
    }

    public double getWeight() {
        return weight;
    }

    public Status setWeight(double weight) {
        this.weight = weight;
        return this;
    }

    public Point getDestination() {
        return destination;
    }

    public Status setDestination(Point destination) {
        this.destination = destination;
        return this;
    }
}
