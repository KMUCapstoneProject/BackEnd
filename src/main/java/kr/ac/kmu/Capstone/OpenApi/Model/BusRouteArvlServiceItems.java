package kr.ac.kmu.Capstone.OpenApi.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.kmu.Capstone.OpenApi.VO.BusRouteArvlServiceItem;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
public class BusRouteArvlServiceItems {
    //버스도착정보
    //정류소별특정노선버스 도착예정정보 목록조회
    @JsonProperty("item")
    private List<kr.ac.kmu.Capstone.OpenApi.VO.BusRouteArvlServiceItem> BusRouteArvlServiceItem;

    @JsonCreator
    public BusRouteArvlServiceItems(@JsonProperty("response") JsonNode node) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode itemNode = node.findValue("item");
        this.BusRouteArvlServiceItem = Arrays.stream(objectMapper.treeToValue(itemNode, BusRouteArvlServiceItem[].class)).toList();
    }
}
