package kr.ac.kmu.capstone.dto.location;

public class PointDto {

    private  String name;
    private  double latitude;
    private  double longitude;

    public PointDto() {
        name = "";
        latitude = 0;
        longitude = 0;
    }

    public PointDto(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public PointDto setName(String name) {
        this.name = name;
        return this;
    }

    public PointDto setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public PointDto setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }
}