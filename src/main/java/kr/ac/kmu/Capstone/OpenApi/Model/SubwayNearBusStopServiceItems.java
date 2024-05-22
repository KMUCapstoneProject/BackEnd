package kr.ac.kmu.Capstone.OpenApi.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.kmu.Capstone.OpenApi.VO.SubwayNearBusStopServiceItem;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
public class SubwayNearBusStopServiceItems {
    //지하철역 출구별 버스노선 목록 조회
    @JsonProperty("item")
    private List<SubwayNearBusStopServiceItem> subwayServiceItem;

    @JsonCreator
    public SubwayNearBusStopServiceItems(@JsonProperty("response") JsonNode node) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode itemNode = node.findValue("item");
        this.subwayServiceItem = Arrays.stream(objectMapper.treeToValue(itemNode, SubwayNearBusStopServiceItem[].class)).toList();
    }
}
