package kr.ac.kmu.Capstone.OpenApi.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.kmu.Capstone.OpenApi.VO.BusRouteAcctoBusServiceItem;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
public class BusRouteAcctoBusServiceItems {
    //버스위치정보
    //노선별버스위치 목록조회
    @JsonProperty("item")
    private List<BusRouteAcctoBusServiceItem> BusApiServiceItem;

    @JsonCreator
    public BusRouteAcctoBusServiceItems(@JsonProperty("response") JsonNode node) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode itemNode = node.findValue("item");
        this.BusApiServiceItem = Arrays.stream(objectMapper.treeToValue(itemNode, BusRouteAcctoBusServiceItem[].class)).toList();
    }
}
