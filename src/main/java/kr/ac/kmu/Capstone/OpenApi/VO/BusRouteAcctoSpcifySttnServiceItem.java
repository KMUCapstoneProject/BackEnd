package kr.ac.kmu.Capstone.OpenApi.VO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusRouteAcctoSpcifySttnServiceItem {
    //버스위치정보
    //노선별특정정류소접근 버스위치정보조회

    // 노선번호
    @JsonProperty("routenm")
    private String routenm;

    // 정류소명
    @JsonProperty("nodenm")
    private String nodenm;

    // 맵매칭 Y좌표
    @JsonProperty("gpslati")
    private String gpslati;

    // 맵매칭 X좌표
    @JsonProperty("gpslong")
    private String gpslong;

    // 노선유형
    @JsonProperty("routetp")
    private String routetp;
}
