package kr.ac.kmu.Capstone.OpenApi.VO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusRouteArvlServiceItem {
    //버스도착정보
    //정류소별특정노선버스 도착예정정보 목록조회

    // 정류소ID
    @JsonProperty("nodeid")
    private String nodeid;

    // 정류소명
    @JsonProperty("nodenm")
    private String nodenm;

    // 노선ID
    @JsonProperty("routeid")
    private String routeid;

    // 노선번호
    @JsonProperty("routeno")
    private String routeno;

    // 노선유형
    @JsonProperty("routetp")
    private String routetp;

    //도착예정버스 남은 정류장 수
    @JsonProperty("arrprevstationcnt")
    private int arrprevstationcnt;

    // 도착예정버스 차량유형
    @JsonProperty("vehicletp")
    private String vehicletp;

    //도착예정버스 도착예상시간
    @JsonProperty("arrtime")
    private int arrtime;
}
