package kr.ac.kmu.Capstone.OpenApi.ParsingDecode;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.kmu.Capstone.OpenApi.Model.BusGetRouteInfoServiceItems;
import kr.ac.kmu.Capstone.OpenApi.VO.BusGetRouteInfoServiceItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BusGetRouteInfoService {
    public BusGetRouteInfoServiceItems parsingJsonObject(String json) {
        BusGetRouteInfoServiceItems result = new BusGetRouteInfoServiceItems(new ArrayList<>());
        try {
            ObjectMapper mapper = new ObjectMapper();
            BusGetRouteInfoServiceItems items = mapper.readValue(json, BusGetRouteInfoServiceItems.class);


            for(BusGetRouteInfoServiceItem item : items.getBusApiServiceItem()) {
                result.getBusApiServiceItem().add(decodeCategory(item));
            }
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("이 서비스를 이용할 수 없습니다.", e);
        }
        return result;
    }

    private BusGetRouteInfoServiceItem decodeCategory(BusGetRouteInfoServiceItem item) {


        return item;
    }
}
