package kr.ac.kmu.Capstone.OpenApi.VO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusRouteAcctoBusServiceItem {
    //버스위치정보
    //노선별버스위치 목록조회

    // 노선번호
    @JsonProperty("routenm")
    private String routenm;

    // 맵매칭 Y좌표
    @JsonProperty("gpslati")
    private String gpslati;

    // 맵매칭 X좌표
    @JsonProperty("gpslong")
    private String gpslong;

    // 정류소 순서
    @JsonProperty("nodeord")
    private String nodeord;

    // 정류소명
    @JsonProperty("nodenm")
    private String nodenm;

    // 정류소ID
    @JsonProperty("nodeid")
    private String nodeid;

    // 노선유형
    @JsonProperty("routetp")
    private String routetp;

    // 차량번호
    @JsonProperty("vehicleno")
    private String vehicleno;
}
