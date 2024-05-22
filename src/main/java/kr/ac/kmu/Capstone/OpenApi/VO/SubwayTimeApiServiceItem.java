package kr.ac.kmu.Capstone.OpenApi.VO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubwayTimeApiServiceItem {
    //지하철역별 시간표 목록조회
    //도착시간
    @JsonProperty("arrTime")
    private String arrTime;

    //요일구분코드
    @JsonProperty("dailyTypeCode")
    private String dailyTypeCode;

    //출발시간
    @JsonProperty("depTime")
    private String depTime;

    //종점지하철역ID
    @JsonProperty("endSubwayStationId")
    private String endSubwayStationId;

    //종점지하철역명
    @JsonProperty("endSubwayStationNm")
    private String endSubwayStationNm;

    //지하철 노선Id
    @JsonProperty("subwayRouteId")
    private String subwayRouteId;

    //지하철역Id(지하철시간표api)
    @JsonProperty("subwayStationId")
    private String subwayStationId;

    //지하철역명(지하철시간표api)
    @JsonProperty("subwayStationNm")
    private String subwayStationNm;

    //상하행구분코드
    @JsonProperty("upDownTypeCode")
    private String upDownTypeCode;
}
