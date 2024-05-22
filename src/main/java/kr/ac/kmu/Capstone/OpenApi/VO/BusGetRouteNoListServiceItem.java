package kr.ac.kmu.Capstone.OpenApi.VO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusGetRouteNoListServiceItem {
    //버스노선정보ㅇ
    //노선번호목록 조회

    // 노선ID
    @JsonProperty("routeid")
    private String routeid;

    // 노선번호
    @JsonProperty("routeno")
    private String routeno;

    // 노선유형
    @JsonProperty("routetp")
    private String routetp;

    // 종점
    @JsonProperty("endnodenm")
    private String endnodenm;

    // 기점
    @JsonProperty("startnodenm")
    private String startnodenm;

    // 막차시간(2400기준)
    @JsonProperty("endvehicletime")
    private int endvehicletime;

    // 첫차시간
    @JsonProperty("startvehicletime")
    private int startvehicletime;
}
