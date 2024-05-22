package kr.ac.kmu.Capstone.OpenApi.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.kmu.Capstone.OpenApi.VO.SubwayFindSttnListServiceItem;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
public class SubwayFindSttnListServiceItems {
    //키워드기반 지하철역 목록 조회
    @JsonProperty("item")
    private List<SubwayFindSttnListServiceItem> subwayServiceItem;

    @JsonCreator
    public SubwayFindSttnListServiceItems(@JsonProperty("response") JsonNode node) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode itemNode = node.findValue("item");
        this.subwayServiceItem = Arrays.stream(objectMapper.treeToValue(itemNode, SubwayFindSttnListServiceItem[].class)).toList();
    }
}
