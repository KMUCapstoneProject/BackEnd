package kr.ac.kmu.Capstone.OpenApi.OpenApiInfo;

public class OpenApiCallName {
    //버스도착정보
    //정류소별 도착예정정보 목록 조회
    public static final String ARRENDPOINT = "http://apis.data.go.kr/1613000/ArvlInfoInqireService/getSttnAcctoArvlPrearngeInfoList";

    //정류소별 특정노선버스 도착예정정보 목록조회
    public static final String NODEARRENDPOINT = "http://apis.data.go.kr/1613000/ArvlInfoInqireService/getSttnAcctoSpcifyRouteBusArvlPrearngeInfoList";


    //버스노선정보
    //노선정보항목 조회
    public static final String NodeItemENDPOINT = "http://apis.data.go.kr/1613000/BusRouteInfoInqireService/getRouteInfoIem";

    //노선번호목록 조회
    public static final String NodeListNDPOINT = "http://apis.data.go.kr/1613000/BusRouteInfoInqireService/getRouteNoList";

    //노선별 경유 정류소 목록조회
    public static final String NodeStopNDPOINT = "http://apis.data.go.kr/1613000/BusRouteInfoInqireService/getRouteAcctoThrghSttnList";


    //버스정류소정보
    //좌표기반 근접정류소 목록조회
    public static final String XYENDPOINT = "http://apis.data.go.kr/1613000/BusSttnInfoInqireService/getCrdntPrxmtSttnList";

    //정류소번호 목록조회
    public static final String ENDPOINT = "http://apis.data.go.kr/1613000/BusSttnInfoInqireService/getSttnNoList";

    //정류소별 경유노선 목록조회
    public static final String STOPNODEENDPOINT = "http://apis.data.go.kr/1613000/BusSttnInfoInqireService/getSttnThrghRouteList";

    //버스위치정보
    //노선별 버스위치목록조회
    public static final String RouteAcctoBusENDPOINT = "http://apis.data.go.kr/1613000/BusLcInfoInqireService/getRouteAcctoBusLcList";

    //노선별 정류소접근 버스위치정보
    public static final String RouteAcctoSpcifySttnENDPOINT = "http://apis.data.go.kr/1613000/BusLcInfoInqireService/getRouteAcctoSpcifySttnAccesBusLcInfo";



    //지하철
    //키워드기반 지하철역 목록조회
    public static final String KeyWordENDPOINT = "http://apis.data.go.kr/1613000/SubwayInfoService/getKwrdFndSubwaySttnList";

    //지하철역출구별 버스노선 목록조회
    public static final String SubwayBusENDPOINT = "http://apis.data.go.kr/1613000/SubwayInfoService/getSubwaySttnExitAcctoBusRouteList";

    //지하철역별 시간표 목록조회
    public static final String SubwayTimeENDPOINT = "http://apis.data.go.kr/1613000/SubwayInfoService/getSubwaySttnAcctoSchdulList";
}
