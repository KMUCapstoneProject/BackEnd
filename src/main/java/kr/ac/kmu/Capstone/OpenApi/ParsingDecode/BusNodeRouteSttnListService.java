package kr.ac.kmu.Capstone.OpenApi.ParsingDecode;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.kmu.Capstone.OpenApi.Model.BusNodeRouteSttnListServiceItems;
import kr.ac.kmu.Capstone.OpenApi.VO.BusNodeRouteSttnListServiceItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BusNodeRouteSttnListService {
    public BusNodeRouteSttnListServiceItems parsingJsonObject(String json) {
        BusNodeRouteSttnListServiceItems result = new BusNodeRouteSttnListServiceItems(new ArrayList<>());
        try {
            ObjectMapper mapper = new ObjectMapper();
            BusNodeRouteSttnListServiceItems items = mapper.readValue(json, BusNodeRouteSttnListServiceItems.class);

            for(BusNodeRouteSttnListServiceItem item : items.getBusApiServiceItem()) {
                result.getBusApiServiceItem().add(decodeCategory(item));
            }
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("이 서비스를 이용할 수 없습니다.", e);
        }
        return result;
    }

    private BusNodeRouteSttnListServiceItem decodeCategory(BusNodeRouteSttnListServiceItem item) {


        return item;
    }
}
