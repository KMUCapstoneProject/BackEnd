package kr.ac.kmu.Capstone.OpenApi.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.kmu.Capstone.OpenApi.VO.BusNodegetRouteInfoIemServiceItem;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
public class BusNodegetRouteInfoIemServiceItems {
    //버스노선정보
    //노선정보항목조회
    @JsonProperty("item")
    private List<BusNodegetRouteInfoIemServiceItem> BusApiServiceItem;

    @JsonCreator
    public BusNodegetRouteInfoIemServiceItems(@JsonProperty("response") JsonNode node) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode itemNode = node.findValue("item");
        this.BusApiServiceItem = Arrays.stream(objectMapper.treeToValue(itemNode, BusNodegetRouteInfoIemServiceItem[].class)).toList();
    }
}
