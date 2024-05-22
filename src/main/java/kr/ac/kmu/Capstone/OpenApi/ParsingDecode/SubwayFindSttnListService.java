package kr.ac.kmu.Capstone.OpenApi.ParsingDecode;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.kmu.Capstone.OpenApi.Model.SubwayFindSttnListServiceItems;
import kr.ac.kmu.Capstone.OpenApi.VO.SubwayFindSttnListServiceItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SubwayFindSttnListService {
    public SubwayFindSttnListServiceItems parsingJsonObject(String json) {
        SubwayFindSttnListServiceItems result = new SubwayFindSttnListServiceItems(new ArrayList<>());
        try {
            ObjectMapper mapper = new ObjectMapper();
            SubwayFindSttnListServiceItems items = mapper.readValue(json, SubwayFindSttnListServiceItems.class);

            for(SubwayFindSttnListServiceItem item : items.getSubwayServiceItem()) {//여기문제
                result.getSubwayServiceItem().add(decodeCategory(item));
            }
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("이 서비스를 이용할 수 없습니다.", e);
        }
        return result;
    }

    private SubwayFindSttnListServiceItem decodeCategory(SubwayFindSttnListServiceItem item) {


        return item;
    }
}
