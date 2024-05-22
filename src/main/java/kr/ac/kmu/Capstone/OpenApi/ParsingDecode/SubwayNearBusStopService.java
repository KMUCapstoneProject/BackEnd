package kr.ac.kmu.Capstone.OpenApi.ParsingDecode;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.kmu.Capstone.OpenApi.Model.SubwayNearBusStopServiceItems;
import kr.ac.kmu.Capstone.OpenApi.VO.SubwayNearBusStopServiceItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SubwayNearBusStopService {
    public SubwayNearBusStopServiceItems parsingJsonObject(String json) {
        SubwayNearBusStopServiceItems result = new SubwayNearBusStopServiceItems(new ArrayList<>());
        try {
            ObjectMapper mapper = new ObjectMapper();
            SubwayNearBusStopServiceItems items = mapper.readValue(json, SubwayNearBusStopServiceItems.class);

            for(SubwayNearBusStopServiceItem item : items.getSubwayServiceItem()) {
                result.getSubwayServiceItem().add(decodeCategory(item));
            }
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("이 서비스를 이용할 수 없습니다.", e);
        }
        return result;
    }

    private SubwayNearBusStopServiceItem decodeCategory(SubwayNearBusStopServiceItem item) {


        return item;
    }
}
