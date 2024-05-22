package kr.ac.kmu.Capstone.OpenApi.VO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusApiServiceItem {
    //버스도착정보
    //정류소별도착예정정보 목록 조회

    // 도착예정버스 남은 정류장 수
    @JsonProperty("arrprevstationcnt")
    private String arrprevstationcnt;

    // 도착예정버스 도착예상시간
    @JsonProperty("arrtime")
    private String arrtime;

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

    // 도착예정버스 차량유형
    @JsonProperty("vehicletp")
    private String vehicletp;

}
