package kr.ac.kmu.Capstone.OpenApi.ParsingDecode;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.kmu.Capstone.OpenApi.Model.BusGetRouteNoListServiceItems;
import kr.ac.kmu.Capstone.OpenApi.VO.BusGetRouteNoListServiceItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BusGetRouteNoListService {
    public BusGetRouteNoListServiceItems parsingJsonObject(String json) {
        BusGetRouteNoListServiceItems result = new BusGetRouteNoListServiceItems(new ArrayList<>());
        try {
            ObjectMapper mapper = new ObjectMapper();
            BusGetRouteNoListServiceItems items = mapper.readValue(json, BusGetRouteNoListServiceItems.class);

            for(BusGetRouteNoListServiceItem item : items.getBusApiServiceItem()) {
                result.getBusApiServiceItem().add(decodeCategory(item));
            }
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("이 서비스를 이용할 수 없습니다.", e);
        }
        return result;
    }

    private BusGetRouteNoListServiceItem decodeCategory(BusGetRouteNoListServiceItem item) {


        return item;
    }
}
