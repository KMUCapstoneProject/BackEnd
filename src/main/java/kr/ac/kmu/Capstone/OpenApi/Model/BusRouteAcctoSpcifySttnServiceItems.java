package kr.ac.kmu.Capstone.OpenApi.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.kmu.Capstone.OpenApi.ParsingDecode.BusRouteAcctoSpcifySttnService;
import kr.ac.kmu.Capstone.OpenApi.VO.BusRouteAcctoSpcifySttnServiceItem;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
public class BusRouteAcctoSpcifySttnServiceItems {
    //버스위치정보
    //노선별특정정류소접근 버스위치정보조회
    @JsonProperty("item")
    private List<BusRouteAcctoSpcifySttnServiceItem> BusRouteAcctoSpcifySttnServiceItem;

    @JsonCreator
    public BusRouteAcctoSpcifySttnServiceItems(@JsonProperty("response") JsonNode node) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode itemNode = node.findValue("item");
        this.BusRouteAcctoSpcifySttnServiceItem = Arrays.stream(objectMapper.treeToValue(itemNode, BusRouteAcctoSpcifySttnServiceItem[].class)).toList();
    }
}
