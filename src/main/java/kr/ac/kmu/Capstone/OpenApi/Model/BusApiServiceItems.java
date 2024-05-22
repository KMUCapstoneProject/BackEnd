package kr.ac.kmu.Capstone.OpenApi.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.kmu.Capstone.OpenApi.ParsingDecode.BusApiService;
import kr.ac.kmu.Capstone.OpenApi.VO.BusApiServiceItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
public class BusApiServiceItems {
    @JsonProperty("item")
    private List<BusApiServiceItem> BusApiServiceItem;

    private static final Logger logger = LoggerFactory.getLogger(BusApiService.class);

    @JsonCreator
    public BusApiServiceItems(@JsonProperty("response") JsonNode node) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode itemNode = node.findValue("item");
        if (itemNode == null || !itemNode.isArray() || itemNode.size() == 0) {
            this.BusApiServiceItem = Collections.emptyList();
        } else {
            this.BusApiServiceItem = Arrays.asList(objectMapper.treeToValue(itemNode, BusApiServiceItem[].class));
        }
    }
}
