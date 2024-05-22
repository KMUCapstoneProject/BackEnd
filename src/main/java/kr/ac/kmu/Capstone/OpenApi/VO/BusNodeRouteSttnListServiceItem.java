package kr.ac.kmu.Capstone.OpenApi.VO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusNodeRouteSttnListServiceItem {
    //버스정류소정보
    //정류소별경유노선조회

    // 노선ID
    @JsonProperty("routeid")
    private String routeid;

    // 노선번호
    @JsonProperty("routeno")
    private String routeno;

    // 노선유형
    @JsonProperty("routetp")
    private String routetp;

    // 정류소번호
    @JsonProperty("nodeno")
    private String nodeno;

    // 종점
    @JsonProperty("endnodenm")
    private int endnodenm;

    // 기점
    @JsonProperty("startnodenm")
    private String startnodenm;
}
