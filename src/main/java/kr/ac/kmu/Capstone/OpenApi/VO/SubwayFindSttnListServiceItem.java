package kr.ac.kmu.Capstone.OpenApi.VO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubwayFindSttnListServiceItem {
    //키워드기반 지하철역 목록 조회

    //지하철역ID
    @JsonProperty("subwayStationId")
    private String subwayStationId;

    //지하철역명
    @JsonProperty("subwayStationName")
    private String subwayStationName;

    //노선명
    @JsonProperty("subwayRouteName")
    private String subwayRouteName;
}
