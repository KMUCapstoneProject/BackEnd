package kr.ac.kmu.Capstone.OpenApi.VO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusGetRouteAcctoThrghSttnServiceItem {
    //버스노선정보ㅇ
    //노선별경유정류소목록조회

    // 노선ID
    @JsonProperty("routeid")
    private String routeid;

    // 정류소ID
    @JsonProperty("nodeid")
    private String nodeid;

    // 정류소명
    @JsonProperty("nodenm")
    private String nodenm;

    // 정류소번호
    @JsonProperty("nodeno")
    private String nodeno;

    // 정류소순번
    @JsonProperty("nodeord")
    private int nodeord;

    // 정류소 Y좌표
    @JsonProperty("gpslati")
    private String gpslati;

    // 정류소 X좌표
    @JsonProperty("gpslong")
    private String gpslong;

    // 상하행구분코드
    @JsonProperty("updowncd")
    private String updowncd;

}
