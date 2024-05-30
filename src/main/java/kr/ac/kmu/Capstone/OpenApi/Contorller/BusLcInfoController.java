package kr.ac.kmu.Capstone.OpenApi.Contorller;


import kr.ac.kmu.Capstone.OpenApi.Model.BusRouteAcctoSpcifySttnServiceItems;
import kr.ac.kmu.Capstone.OpenApi.Model.BusRouteArvlServiceItems;
import kr.ac.kmu.Capstone.OpenApi.OpenApiInfo.AreaCode;
import kr.ac.kmu.Capstone.OpenApi.OpenApiInfo.OpenApiCallName;
import kr.ac.kmu.Capstone.OpenApi.ParsingDecode.BusApiService;
import kr.ac.kmu.Capstone.OpenApi.ParsingDecode.BusRouteAcctoSpcifySttnService;
import kr.ac.kmu.Capstone.OpenApi.ParsingDecode.BusRouteArvlService;
import kr.ac.kmu.Capstone.OpenApi.VO.BusApiServiceItem;
import kr.ac.kmu.Capstone.dto.OpenApi.BusArrivalInfoStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import static kr.ac.kmu.Capstone.OpenApi.OpenApiInfo.OpenApiServiceKey.SERVICE_KEY;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-service/api")
public class BusLcInfoController {
    private final BusRouteAcctoSpcifySttnService BusRouteAcctoSpcifySttnService;
    private final BusRouteArvlService busRouteArvlService;
    String cityCode = Integer.toString(AreaCode.대구광역시);

    @Autowired
    private BusArrivalInfoStorage storage;

    @Autowired
    private BusApiService busApiService;

    @GetMapping("/BusLocationByArrivalInfo")
    public ResponseEntity<BusRouteAcctoSpcifySttnServiceItems> getBusLocationByArrivalInfo(
            @RequestParam(value="routeId") String routeId
    ) {
        // BusArrivalInfoStorage에서 해당 routeId에 대한 버스 도착 정보를 가져옵니다.
        List<BusApiServiceItem> busArrivalInfoList = storage.getBusArrivalInfoList();

        // 해당 routeId와 일치하는 버스 도착 정보를 찾습니다.
        List<BusApiServiceItem> matchingBusInfo = busArrivalInfoList.stream()
                .filter(item -> item.getRouteid().equals(routeId))
                .collect(Collectors.toList());

        // 해당 routeId에 대한 버스 도착 정보가 없는 경우
        if (matchingBusInfo.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        // 해당 routeId에 대한 버스 도착 정보가 있는 경우
        // 노선별 버스 위치 정보 조회 API를 호출하여 해당 노선의 버스 위치 정보를 가져옵니다.
        ResponseEntity<BusRouteAcctoSpcifySttnServiceItems> responseEntity = callBusRouteAcctoBusLcListApi("1", "10", routeId);
        BusRouteAcctoSpcifySttnServiceItems busLocationInfo = responseEntity.getBody();

        return new ResponseEntity<>(busLocationInfo, HttpStatus.OK);
    }

    @GetMapping("/RouteBusLcList")//노선별버스위치 목록조회
    public ResponseEntity<BusRouteAcctoSpcifySttnServiceItems> callBusRouteAcctoBusLcListApi(
            @RequestParam(value="pageNo") String pageNo,
            @RequestParam(value="numOfRows") String numOfRows,
            @RequestParam(value="routeId") String routeId

    ){
        HttpURLConnection urlConnection = null;
        InputStream stream = null;
        String result = null;

        String urlStr = OpenApiCallName.RouteAcctoBusENDPOINT +
                "?serviceKey=" + SERVICE_KEY +
                "&numOfRows=" + numOfRows +
                "&pageNo=" + pageNo +
                "&_type=" + "json" +
                "&cityCode=" + cityCode +
                "&routeId=" + routeId;

        try {
            URL url = new URL(urlStr);

            urlConnection = (HttpURLConnection) url.openConnection();
            stream = getNetworkConnection(urlConnection);
            result = readStreamToString(stream);

            if (stream != null) stream.close();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        BusRouteAcctoSpcifySttnServiceItems response = BusRouteAcctoSpcifySttnService.parsingJsonObject(result); //
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/RouteBusLcInfo")//노선별특정정류소접근 버스위치정보조회
    public ResponseEntity<BusRouteArvlServiceItems> callBusRouteAcctoSpcifySttnAccesBusLcInfoApi(
            @RequestParam(value="pageNo") String pageNo,
            @RequestParam(value="numOfRows") String numOfRows,
            @RequestParam(value="routeId") String routeId,
            @RequestParam(value="nodeId") String nodeId

    ){
        HttpURLConnection urlConnection = null;
        InputStream stream = null;
        String result = null;

        String urlStr = OpenApiCallName.RouteAcctoSpcifySttnENDPOINT +
                "?serviceKey=" + SERVICE_KEY +
                "&numOfRows=" + numOfRows +
                "&pageNo=" + pageNo +
                "&_type=" + "json" +
                "&cityCode=" + cityCode +
                "&nodeId=" + nodeId +
                "&routeId=" + routeId;

        try {
            URL url = new URL(urlStr);

            urlConnection = (HttpURLConnection) url.openConnection();
            stream = getNetworkConnection(urlConnection);
            result = readStreamToString(stream);

            if (stream != null) stream.close();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        BusRouteArvlServiceItems response = busRouteArvlService.parsingJsonObject(result); //
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /* URLConnection 을 전달받아 연결정보 설정 후 연결, 연결 후 수신한 InputStream 반환 */

    private InputStream getNetworkConnection(HttpURLConnection urlConnection) throws IOException {
        urlConnection.setConnectTimeout(3000);
        urlConnection.setReadTimeout(3000);
        urlConnection.setRequestMethod("GET");
        urlConnection.setDoInput(true);

        if(urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP error code : " + urlConnection.getResponseCode());
        }

        return urlConnection.getInputStream();
    }

    /* InputStream을 전달받아 문자열로 변환 후 반환 */

    private String readStreamToString(InputStream stream) throws IOException{
        StringBuilder result = new StringBuilder();

        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));

        String readLine;
        while((readLine = br.readLine()) != null) {
            result.append(readLine + "\n\r");
        }

        br.close();

        return result.toString();
    }
}
