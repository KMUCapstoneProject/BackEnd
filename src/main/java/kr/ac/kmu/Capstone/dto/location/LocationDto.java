package kr.ac.kmu.Capstone.dto.location;

public class LocationDto {

    private  String name;

    private  double latitude;
    private  double longitude;

    public LocationDto() {
        name = "";
        latitude = 0;
        longitude = 0;
    }

    public LocationDto(String name, double latitude, double longitude) {
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

    public LocationDto setName(String name) {
        this.name = name;
        return this;
    }

    public LocationDto setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public LocationDto setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }
}