package kr.ac.kmu.Capstone.OpenApi.ParsingDecode;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.kmu.Capstone.OpenApi.Model.BusNodegetRouteInfoIemServiceItems;
import kr.ac.kmu.Capstone.OpenApi.VO.BusNodegetRouteInfoIemServiceItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BusNodegetRouteInfoIemService {
    public BusNodegetRouteInfoIemServiceItems parsingJsonObject(String json) {
        BusNodegetRouteInfoIemServiceItems result = new BusNodegetRouteInfoIemServiceItems(new ArrayList<>());
        try {
            ObjectMapper mapper = new ObjectMapper();
            BusNodegetRouteInfoIemServiceItems items = mapper.readValue(json, BusNodegetRouteInfoIemServiceItems.class);

            for(BusNodegetRouteInfoIemServiceItem item : items.getBusApiServiceItem()) {
                result.getBusApiServiceItem().add(decodeCategory(item));
            }
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("이 서비스를 이용할 수 없습니다.", e);
        }
        return result;
    }

    private BusNodegetRouteInfoIemServiceItem decodeCategory(BusNodegetRouteInfoIemServiceItem item) {


        return item;
    }
}
