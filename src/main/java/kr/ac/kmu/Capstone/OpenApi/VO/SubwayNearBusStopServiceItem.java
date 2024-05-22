package kr.ac.kmu.Capstone.OpenApi.VO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubwayNearBusStopServiceItem {
    //지하철역 출구별 버스노선 목록 조회

    //출구번호
    @JsonProperty("exitNo")
    private String exitNo;

    //버스번호
    @JsonProperty("busRouteNo")
    private String busRouteNo;
}
