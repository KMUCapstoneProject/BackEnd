package kr.ac.kmu.Capstone.OpenApi.ParsingDecode;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.kmu.Capstone.OpenApi.Model.BusRouteArvlServiceItems;
import kr.ac.kmu.Capstone.OpenApi.VO.BusRouteArvlServiceItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BusRouteArvlService {
    public BusRouteArvlServiceItems parsingJsonObject(String json) {
        BusRouteArvlServiceItems result = new BusRouteArvlServiceItems(new ArrayList<>());
        try {
            ObjectMapper mapper = new ObjectMapper();
            BusRouteArvlServiceItems items = mapper.readValue(json, BusRouteArvlServiceItems.class);

            for(BusRouteArvlServiceItem item : items.getBusRouteArvlServiceItem()) {
                result.getBusRouteArvlServiceItem().add(decodeCategory(item));
            }
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("도착예정인버스가없습니다.", e);
        }
        return result;
    }

    private BusRouteArvlServiceItem decodeCategory(BusRouteArvlServiceItem item) {


        return item;
    }
}
