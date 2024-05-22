package kr.ac.kmu.Capstone.OpenApi.ParsingDecode;


import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.kmu.Capstone.OpenApi.Model.BusGetRouteAcctoThrghSttnServiceItems;
import kr.ac.kmu.Capstone.OpenApi.VO.BusGetRouteAcctoThrghSttnServiceItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BusGetRouteAcctoThrghSttnService {
    public BusGetRouteAcctoThrghSttnServiceItems parsingJsonObject(String json) {
        BusGetRouteAcctoThrghSttnServiceItems result = new BusGetRouteAcctoThrghSttnServiceItems(new ArrayList<>());
        try {
            ObjectMapper mapper = new ObjectMapper();
            BusGetRouteAcctoThrghSttnServiceItems items = mapper.readValue(json, BusGetRouteAcctoThrghSttnServiceItems.class);

            for(BusGetRouteAcctoThrghSttnServiceItem item : items.getBusApiServiceItem()) {
                result.getBusApiServiceItem().add(decodeCategory(item));
            }
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("이 서비스를 이용할 수 없습니다.", e);
        }
        return result;
    }

    private BusGetRouteAcctoThrghSttnServiceItem decodeCategory(BusGetRouteAcctoThrghSttnServiceItem item) {

        return item;
    }
}
