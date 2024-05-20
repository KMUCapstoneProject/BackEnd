package kr.ac.kmu.capstone.dto.location;

public class StatusDto {
    // 관계 id
    private String start_name;
    private String end_name;

    // 관계 value ( road / stair / hill)
    private String value;
    // 거리 크기
    private double weight;

    public StatusDto() {
    }

    public StatusDto(String start_name, String end_name, String value, double weight) {
        this.start_name = start_name;
        this.end_name = end_name;
        this.value = value;
        this.weight = weight;
    }

    public String getStart_name() {
        return start_name;
    }

    public StatusDto setStart_name(String start_name) {
        this.start_name = start_name;
        return this;
    }

    public String getEnd_name() {
        return end_name;
    }

    public StatusDto setEnd_name(String end_name) {
        this.end_name = end_name;
        return this;
    }

    public String getValue() {
        return value;
    }

    public StatusDto setValue(String value) {
        this.value = value;
        return this;
    }

    public double getWeight() {
        return weight;
    }

    public StatusDto setWeight(double weight) {
        this.weight = weight;
        return this;
    }
}
