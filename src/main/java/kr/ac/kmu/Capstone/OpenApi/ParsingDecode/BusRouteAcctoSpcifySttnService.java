package kr.ac.kmu.Capstone.OpenApi.ParsingDecode;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.kmu.Capstone.OpenApi.Model.BusRouteAcctoSpcifySttnServiceItems;
import kr.ac.kmu.Capstone.OpenApi.VO.BusRouteAcctoSpcifySttnServiceItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BusRouteAcctoSpcifySttnService {
    //노선별특정정류소접근 버스위치정보조회
    public BusRouteAcctoSpcifySttnServiceItems parsingJsonObject(String json) {
        BusRouteAcctoSpcifySttnServiceItems result = new BusRouteAcctoSpcifySttnServiceItems(new ArrayList<>());
        try {
            ObjectMapper mapper = new ObjectMapper();
            BusRouteAcctoSpcifySttnServiceItems items = mapper.readValue(json, BusRouteAcctoSpcifySttnServiceItems.class);

            for(BusRouteAcctoSpcifySttnServiceItem item : items.getBusRouteAcctoSpcifySttnServiceItem()) {
                result.getBusRouteAcctoSpcifySttnServiceItem().add(decodeCategory(item));
            }
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("이 서비스를 이용할 수 없습니다.", e);
        }
        return result;
    }

    private BusRouteAcctoSpcifySttnServiceItem decodeCategory(BusRouteAcctoSpcifySttnServiceItem item) {


        return item;
    }
}
