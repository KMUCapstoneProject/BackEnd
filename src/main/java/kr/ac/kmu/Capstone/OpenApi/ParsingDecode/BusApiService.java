package kr.ac.kmu.Capstone.OpenApi.ParsingDecode;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.kmu.Capstone.OpenApi.Model.BusApiServiceItems;
import kr.ac.kmu.Capstone.OpenApi.VO.BusApiServiceItem;
import kr.ac.kmu.Capstone.OpenApi.VO.CategoryCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusApiService {
    private static final Logger logger = LoggerFactory.getLogger(BusApiService.class);

    public BusApiServiceItems parsingJsonObject(String json) { 
        BusApiServiceItems result = new BusApiServiceItems(new ArrayList<>());
        try {
            ObjectMapper mapper = new ObjectMapper();
            BusApiServiceItems items = mapper.readValue(json, BusApiServiceItems.class);

            if (items.getBusApiServiceItem().isEmpty()) {
                logger.info("버스 도착 예정 정보가 없습니다.");
                return items;
            }

            for(BusApiServiceItem item : items.getBusApiServiceItem()) {
                result.getBusApiServiceItem().add(decodeCategory(item));
            }
        } catch(Exception e) {
            e.printStackTrace();
            logger.error("버스 도착 예정 정보를 처리하는 중 오류가 발생했습니다.", e);
            throw new RuntimeException("도착예정인버스가없습니다.", e);
        }
        return result;
    }

    private BusApiServiceItem decodeCategory(BusApiServiceItem item) {
        String arrtime = item.getArrtime();
        String arrprevstationcnt = item.getArrprevstationcnt();

        // 카테고리의 남은 정류장 개수와 버스 도착 예상 시간을 처리
        String remainingStations = CategoryCode.getCodeValueWithUnit("arrprevstationcnt", String.valueOf(item.getArrprevstationcnt()));
        String arrivalTime = CategoryCode.getCodeValueWithUnit("arrtime", String.valueOf(item.getArrtime()));

        // 처리된 값을 다시 설정
        item.setArrprevstationcnt(remainingStations);
        item.setArrtime(arrivalTime);

        return item;
    }
    public List<BusApiServiceItem> sortItemsByArrivalTime(List<BusApiServiceItem> items) {
        List<BusApiServiceItem> sortedItems = items.stream()
                .sorted(Comparator.comparing(item -> convertTimeToSeconds(item.getArrtime())))
                .collect(Collectors.toList());

        logger.debug("Sorted items by arrival time: {}", sortedItems);

        return sortedItems;
    }

    private int convertTimeToSeconds(String timeString) {
        String[] parts = timeString.split(" ");
        int minutes = 0;
        if (parts.length > 0) {
            minutes = Integer.parseInt(parts[0].replaceAll("[^0-9]", ""));
        }
        int seconds = 0;
        if (parts.length > 1) {
            seconds = Integer.parseInt(parts[1].replaceAll("[^0-9]", ""));
        }
        return minutes * 60 + seconds;
    }
}
