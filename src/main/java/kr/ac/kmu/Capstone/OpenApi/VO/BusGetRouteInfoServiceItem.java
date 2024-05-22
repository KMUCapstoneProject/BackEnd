package kr.ac.kmu.Capstone.OpenApi.VO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusGetRouteInfoServiceItem {
    //버스노선정보
    //노선정보항목 조회
    // 종점
    @JsonProperty("endnodenm")
    private String endnodenm;

    // 막차시간(2400기준)
    @JsonProperty("endvehicletime")
    private int  endvehicletime;

    // 배차간격(토요일)(분)
    @JsonProperty("intervalsattime")
    private int  intervalsattime;

    // 배차간격(일요일)(분)
    @JsonProperty("intervalsuntime")
    private int  intervalsuntime;

    // 배차간격(평일)(분)
    @JsonProperty("intervaltime")
    private int  intervaltime;

    // 노선ID
    @JsonProperty("routeid")
    private String routeid;

    // 노선번호
    @JsonProperty("routeno")
    private int routeno;

    // 노선유형
    @JsonProperty("routetp")
    private String routetp;

    // 기점
    @JsonProperty("startnodenm")
    private String startnodenm;

    // 첫차시간
    @JsonProperty("startvehicletime")
    private String  startvehicletime;
}
