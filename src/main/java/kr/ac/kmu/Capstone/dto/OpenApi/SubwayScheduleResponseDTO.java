package kr.ac.kmu.Capstone.dto.OpenApi;

public class SubwayScheduleResponseDTO {
    private String startStation;
    private String endStation;
    private String dayOfWeek;
    private long travelTime;
    private long timeUntilNextTrain;
    private long secondNextTrain;
    private long thirdNextTrain;
    private long fourthNextTrain;

    public SubwayScheduleResponseDTO(String startStation, String endStation, String dayOfWeek, long travelTime, long timeUntilNextTrain, long secondNextTrain, long thirdNextTrain, long fourthNextTrain) {
        this.startStation = startStation;
        this.endStation = endStation;
        this.dayOfWeek = dayOfWeek;
        this.travelTime = travelTime;
        this.timeUntilNextTrain = timeUntilNextTrain;
        this.secondNextTrain = secondNextTrain;
        this.thirdNextTrain = thirdNextTrain;
        this.fourthNextTrain = fourthNextTrain;
    }

    public String getStartStation() {
        return startStation;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public long getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(long travelTime) {
        this.travelTime = travelTime;
    }

    public long getTimeUntilNextTrain() {
        return timeUntilNextTrain;
    }

    public void setTimeUntilNextTrain(long timeUntilNextTrain) {
        this.timeUntilNextTrain = timeUntilNextTrain;
    }

    public long getSecondNextTrain() {
        return secondNextTrain;
    }

    public void setSecondNextTrain(long secondNextTrain) {
        this.secondNextTrain = secondNextTrain;
    }

    public long getThirdNextTrain() {
        return thirdNextTrain;
    }

    public void setThirdNextTrain(long thirdNextTrain) {
        this.thirdNextTrain = thirdNextTrain;
    }

    public long getfourthNextTrain() {
        return fourthNextTrain;
    }

    public void setfourthNextTrain(long thirdNextTrain) {
        this.fourthNextTrain = fourthNextTrain;
    }

    @Override
    public String toString() {
        return "SubwayScheduleResponseDTO{" +
                "startStation='" + startStation + '\'' +
                ", endStation='" + endStation + '\'' +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", travelTime=" + travelTime +
                ", timeUntilNextTrain=" + timeUntilNextTrain +
                ", secondNextTrain=" + secondNextTrain +
                ", thirdNextTrain=" + thirdNextTrain +
                '}';
    }
}
