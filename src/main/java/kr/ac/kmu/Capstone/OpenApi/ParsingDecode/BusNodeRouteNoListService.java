package kr.ac.kmu.Capstone.OpenApi.ParsingDecode;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.kmu.Capstone.OpenApi.Model.BusNodeRouteNoListServiceItems;
import kr.ac.kmu.Capstone.OpenApi.VO.BusNodeRouteNoListServiceItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BusNodeRouteNoListService {
    public BusNodeRouteNoListServiceItems parsingJsonObject(String json) {
        BusNodeRouteNoListServiceItems result = new BusNodeRouteNoListServiceItems(new ArrayList<>());
        try {
            ObjectMapper mapper = new ObjectMapper();
            BusNodeRouteNoListServiceItems items = mapper.readValue(json, BusNodeRouteNoListServiceItems.class);

            for(BusNodeRouteNoListServiceItem item : items.getBusApiServiceItem()) {
                result.getBusApiServiceItem().add(decodeCategory(item));
            }
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("이 서비스를 이용할 수 없습니다.", e);
        }
        return result;
    }

    private BusNodeRouteNoListServiceItem decodeCategory(BusNodeRouteNoListServiceItem item) {


        return item;
    }
}
