package kr.ac.kmu.Capstone.OpenApi.ParsingDecode;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.kmu.Capstone.OpenApi.Model.SubwayServiceItems;
import kr.ac.kmu.Capstone.OpenApi.Utils.DateUtils;
import kr.ac.kmu.Capstone.OpenApi.Utils.TimeUtils;
import kr.ac.kmu.Capstone.OpenApi.VO.CategoryCode;
import kr.ac.kmu.Capstone.OpenApi.VO.SubwayTimeApiServiceItem;
import kr.ac.kmu.Capstone.dto.OpenApi.SubwayScheduleResponseDTO;
import kr.ac.kmu.Capstone.entity.SubwayTimeEntity;
import kr.ac.kmu.Capstone.repository.SubwayRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SubwayTimeApiService {
    @Autowired
    private SubwayRepository subwayTimeRepository;

    private List<String> scheduleList = new ArrayList<>();

    private List<Map<String, List<String>>> scheduleLists = new ArrayList<>();

    private static final Logger logger = LoggerFactory.getLogger(SubwayTimeApiService.class);

    public void saveSubwayTimeItems(List<SubwayTimeApiServiceItem> items) {
        List<SubwayTimeEntity> entities = new ArrayList<>();
        for (SubwayTimeApiServiceItem item : items) {
            if (!subwayTimeRepository.existsByUniqueKeys(item.getDailyTypeCode(), item.getUpDownTypeCode(), item.getArrTime(), item.getDepTime())) {
                SubwayTimeEntity entity = new SubwayTimeEntity();
                entity.setArrivalTime(item.getArrTime());
                entity.setDepartureTime(item.getDepTime());
                entity.setDailyType(item.getDailyTypeCode());
                entity.setEndStation(item.getEndSubwayStationNm());
                entity.setStation(item.getSubwayStationNm());
                entity.setupDownTypeCode(item.getUpDownTypeCode());
                entities.add(entity);
            }
        }
        subwayTimeRepository.saveAll(entities);
    }

    public List<SubwayTimeEntity> getNextSchedules(String subwayStationNm, String dailyTypeCode, String upDownTypeCode) {
        List<SubwayTimeEntity> schedules = subwayTimeRepository.findSchedules(subwayStationNm, dailyTypeCode, upDownTypeCode);
        return schedules.stream()
                .filter(schedule -> TimeUtils.isTimeAfterNow(schedule.getDepTime()))
                .sorted((s1, s2) -> TimeUtils.parseTime(s1.getDepTime()).compareTo(TimeUtils.parseTime(s2.getDepTime())))
                .collect(Collectors.toList());
    }

    public SubwayScheduleResponseDTO getTravelTime(String startStation, String endStation, String dailyTypeCode, String upDownTypeCode) {
        logger.info("getTravelTime called with startStation: {}, endStation: {}, dailyTypeCode: {}, upDownTypeCode: {}", startStation, endStation, dailyTypeCode, upDownTypeCode);

        LocalTime currentTime = LocalTime.now();
        List<SubwayTimeEntity> startSchedules = subwayTimeRepository.findSchedules(startStation, dailyTypeCode, upDownTypeCode);
        List<SubwayTimeEntity> endSchedules = subwayTimeRepository.findSchedules(endStation, dailyTypeCode, upDownTypeCode);

        logger.info("Start schedules size: {}", startSchedules.size());
        logger.info("End schedules size: {}", endSchedules.size());

        List<SubwayTimeEntity> nextStartSchedules = startSchedules.stream()
                .filter(schedule -> TimeUtils.isTimeAfterNow(schedule.getDepTime()))
                .sorted(Comparator.comparing(schedule -> TimeUtils.parseTime(schedule.getDepTime()).isAfter(currentTime)))
                .limit(4)
                .collect(Collectors.toList());

        List<SubwayTimeEntity> nextEndSchedules = endSchedules.stream()
                .filter(schedule -> TimeUtils.isTimeAfterNow(schedule.getArrTime()))
                .sorted(Comparator.comparing(schedule -> TimeUtils.parseTime(schedule.getArrTime()).isAfter(currentTime)))
                .limit(4)
                .collect(Collectors.toList());

        if (!nextStartSchedules.isEmpty() && !nextEndSchedules.isEmpty()) {
            LocalTime startDepTime1 = TimeUtils.parseTime(nextStartSchedules.get(0).getDepTime());
            LocalTime startDepTime2 = nextStartSchedules.size() > 1 ? TimeUtils.parseTime(nextStartSchedules.get(1).getDepTime()) : null;
            LocalTime startDepTime3 = nextStartSchedules.size() > 2 ? TimeUtils.parseTime(nextStartSchedules.get(2).getDepTime()) : null;
            LocalTime startDepTime4 = nextStartSchedules.size() > 3 ? TimeUtils.parseTime(nextStartSchedules.get(3).getDepTime()) : null;


            LocalTime endArrTime1 = TimeUtils.parseTime(nextEndSchedules.get(0).getArrTime());

            long timeUntilNextTrain1 = Duration.between(currentTime, startDepTime1).getSeconds();
            long travelTime = Duration.between(startDepTime1, endArrTime1).getSeconds();

            long timeUntilNextTrain2 = startDepTime2 != null ? Duration.between(currentTime, startDepTime2).getSeconds() : -1;
            long timeUntilNextTrain3 = startDepTime3 != null ? Duration.between(currentTime, startDepTime3).getSeconds() : -1;
            long timeUntilNextTrain4 = startDepTime4 != null ? Duration.between(currentTime, startDepTime4).getSeconds() : -1;


            return new SubwayScheduleResponseDTO(startStation, endStation, dailyTypeCode, travelTime, timeUntilNextTrain1, timeUntilNextTrain2, timeUntilNextTrain3, timeUntilNextTrain4);
        }

        logger.info("Not enough schedules found.");
        return new SubwayScheduleResponseDTO(startStation, endStation, dailyTypeCode, -1, -1, -1, -1, -1);
    }

    public void logSubwaySchedule(String subwayStationNm, String directionStationNm) {
        String dailyTypeCode = DateUtils.getDailyTypeCode();
        String upDownTypeCode = determineUpDownType(subwayStationNm, directionStationNm);
        SubwayScheduleResponseDTO scheduleResponse = getTravelTime(subwayStationNm, directionStationNm, dailyTypeCode, upDownTypeCode);



        if (scheduleResponse.getTravelTime() != -1) {
            String formattedTimeUntilNextTrain1 = formatSecondsToHMS(scheduleResponse.getTimeUntilNextTrain());
            String formattedTimeUntilNextTrain2 = scheduleResponse.getSecondNextTrain() != -1 ? formatSecondsToHMS(scheduleResponse.getSecondNextTrain()) : "N/A";
            String formattedTimeUntilNextTrain3 = scheduleResponse.getThirdNextTrain() != -1 ? formatSecondsToHMS(scheduleResponse.getThirdNextTrain()) : "N/A";
            String formattedTimeUntilNextTrain4 = scheduleResponse.getfourthNextTrain() != -1 ? formatSecondsToHMS(scheduleResponse.getfourthNextTrain()) : "N/A";

            String schedule = String.format("Subway from %s to %s", subwayStationNm, directionStationNm);
            scheduleList.add(schedule);

            logger.info("Time until next train from {}: {}", subwayStationNm, formattedTimeUntilNextTrain1);
            logger.info("Time until second next train from {}: {}", subwayStationNm, formattedTimeUntilNextTrain2);
            logger.info("Time until third next train from {}: {}", subwayStationNm, formattedTimeUntilNextTrain3);
            logger.info("Time until third next train from {}: {}", subwayStationNm, formattedTimeUntilNextTrain4);

            String logMessage = String.format(
                    "{ %s역" +
                            "\n%s," +
                            "\n%s," +
                            "\n%s," +
                            "\n%s" + "}",
                    subwayStationNm,
                    formattedTimeUntilNextTrain1,
                    formattedTimeUntilNextTrain2,
                    formattedTimeUntilNextTrain3,
                    formattedTimeUntilNextTrain4
            );
            /*
            String logMessage = String.format(
                    "Time until next train from %s to %s: %s, " + "Time until second next train: %s, Time until third next train: %s,Time until fourth next train: %s",
                    subwayStationNm, directionStationNm, formattedTimeUntilNextTrain1, formattedTimeUntilNextTrain2, formattedTimeUntilNextTrain3, formattedTimeUntilNextTrain4
            );*/
            scheduleList.add(logMessage);
            logger.info(logMessage);

            List<String> times = Arrays.asList(formattedTimeUntilNextTrain1, formattedTimeUntilNextTrain2, formattedTimeUntilNextTrain3, formattedTimeUntilNextTrain4);

            Map<String, List<String>> scheduleEntry = new HashMap<>();
            scheduleEntry.put(subwayStationNm, times);

            scheduleLists.add(scheduleEntry);
        }

        else {
            logger.info("Could not find travel time from {} to {}", subwayStationNm, directionStationNm);
        }
    }

    public List<Map<String, List<String>>> getSubwayScheduleLists() {
        return scheduleLists;
    }
    public List<String> getSubwayScheduleList() {
        return scheduleList;
    }

    public String determineUpDownType(String subwayStationNm, String directionStationNm) {
        if (subwayStationNm.equals("계명대") && directionStationNm.equals("강창")) {
            return "상행"; // 상행
        } else if (subwayStationNm.equals("강창") && directionStationNm.equals("계명대")) {
            return "하행"; // 하행
        } else if (subwayStationNm.equals("계명대") && directionStationNm.equals("성서산업단지")) {
            return "하행"; // 하행
        } else if (subwayStationNm.equals("강창") && directionStationNm.equals("대실")) {
            return "상행"; // 상행
        }
        return "상행";
    }

    private String formatSecondsToHMS(long totalSeconds) {
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d시 %02d분 %02d초", hours, minutes, seconds);
    }

    public SubwayServiceItems parsingJsonObject(String json) {
        SubwayServiceItems result = new SubwayServiceItems(new ArrayList<>());
        try {
            ObjectMapper mapper = new ObjectMapper();
            SubwayServiceItems items = mapper.readValue(json, SubwayServiceItems.class);

            for(SubwayTimeApiServiceItem item : items.getSubwayServiceItem()) {
                result.getSubwayServiceItem().add(decodeCategory(item));
            }
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("이 서비스를 이용할 수 없습니다.", e);
        }
        return result;
    }

    private SubwayTimeApiServiceItem decodeCategory(SubwayTimeApiServiceItem item) {
        String dailyTypeCode = item.getDailyTypeCode();
        String upDownTypeCode = item.getUpDownTypeCode();
        String depTime = item.getDepTime();

        if (dailyTypeCode == null) {
            dailyTypeCode = "01"; // 기본값 설정
        }
        if (upDownTypeCode == null) {
            upDownTypeCode = "U"; // 기본값 설정
        }
        // CategoryCode를 사용하여 값을 가져옴
        String date = CategoryCode.getCodeValue("dailyTypeCode", dailyTypeCode);
        String direction = CategoryCode.getCodeValue("upDownTypeCode", upDownTypeCode);

        item.setDailyTypeCode(date);
        item.setUpDownTypeCode(direction);

        return item;
    }
}
