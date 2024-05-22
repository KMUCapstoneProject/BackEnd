package kr.ac.kmu.Capstone.OpenApi.VO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusNodegetRouteInfoIemServiceItem {
    //버스정류소정보
    //좌표기반근접정류소 목록조회

    // 정류소 Y좌표
    @JsonProperty("gpslati")
    private String gpslati;

    // 정류소 X좌표
    @JsonProperty("gpslong")
    private String gpslong;

    // 정류소ID
    @JsonProperty("nodeid")
    private String nodeid;

    // 정류소명
    @JsonProperty("nodenm")
    private String nodenm;
}
