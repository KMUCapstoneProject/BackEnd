package kr.ac.kmu.Capstone.OpenApi.Contorller;

import kr.ac.kmu.Capstone.OpenApi.Model.BusApiServiceItems;
import kr.ac.kmu.Capstone.OpenApi.Model.BusRouteAcctoSpcifySttnServiceItems;
import kr.ac.kmu.Capstone.OpenApi.OpenApiInfo.AreaCode;
import kr.ac.kmu.Capstone.OpenApi.OpenApiInfo.OpenApiCallName;
import kr.ac.kmu.Capstone.OpenApi.ParsingDecode.BusApiService;
import kr.ac.kmu.Capstone.OpenApi.ParsingDecode.BusRouteAcctoSpcifySttnService;
import kr.ac.kmu.Capstone.OpenApi.VO.BusApiServiceItem;
import kr.ac.kmu.Capstone.dto.OpenApi.BusArrivalInfoStorage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static kr.ac.kmu.Capstone.OpenApi.OpenApiInfo.OpenApiServiceKey.SERVICE_KEY;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-service/api")
public class BusContorller {
    private final BusApiService busApiService;
    private final BusRouteAcctoSpcifySttnService busRouteAcctoSpcifySttnService;
    private static final Logger logger = LoggerFactory.getLogger(BusContorller.class);
    String cityCode = Integer.toString(AreaCode.대구광역시);

    @Autowired
    private BusArrivalInfoStorage storage;

    private static final Map<String, String> busRoutes = new HashMap<>();

    static {
        busRoutes.put("DGB4070002000", "성서2(대천-대평-대천)");
        busRoutes.put("DGB4070002003", "성서2(대천-하산스무지-대천)");
        busRoutes.put("DGB4070002006", "성서2(대천-강정-육신사-강정-대천)");
        busRoutes.put("DGB4070002016", "성서2(대천-대평-서문시장)");
        busRoutes.put("DGB4070002018", "성서2(서문시장-대평-대천)");
        busRoutes.put("DGB4070002021", "성서2(대천-하산스무지(육신사)-대천)");
        busRoutes.put("DGB4070002026", "성서2(대천-기곡(육신사,도채)-대천)");
        busRoutes.put("DGB4070002027", "성서2(대천-강정-기곡(육신사,도채)-강정-대천)");
        busRoutes.put("DGB4070002033", "성서2(서문시장-하산스무지-대천)");
        busRoutes.put("DGB4070002034", "성서2(서문시장-하산스무지-서문시장)");
        busRoutes.put("DGB4070002050", "성서2(대천-문산-하산스무지-대천)");
        busRoutes.put("DGB4070002060", "성서2(대천-하산스무지-문산-서문시장)");
        busRoutes.put("DGB4070002023", "성서2(대천-하산스무지(대구보훈요양원)-대천)");
        busRoutes.put("DGB4070002022", "성서2(대천-기곡(대평,상당,터실)-대천)");
        busRoutes.put("DGB3000523000", "523(원대오거리 방면)");
        busRoutes.put("DGB3000523001", "523(동산네거리 방면)");
        busRoutes.put("DGB3000527000", "527");
        busRoutes.put("DGB3000564000", "564");
        busRoutes.put("DGB3000655000", "655");
        busRoutes.put("DGB1000001000", "급행1");
        busRoutes.put("DGB4070001000", "성서1");
        busRoutes.put("DGB4030005000", "달서5");
        busRoutes.put("DGB3000503000", "503");
        busRoutes.put("DGB3000425000", "425");
        busRoutes.put("DGB3001725000", "7250");
        busRoutes.put("DGB4030003001", "달서3");
        busRoutes.put("DGB1000007000", "급행7");
        busRoutes.put("DGB1000005000", "급행5");
        busRoutes.put("DGB4070001100", "성서1-1");
        busRoutes.put("DGB3000405000", "405");
        busRoutes.put("DGB3000524000", "524");
        busRoutes.put("DGB4030001000", "달서1");
    }


    @GetMapping("/bus-arrival-info")
    public List<BusApiServiceItem> getBusArrivalInfo() {
       return storage.getBusArrivalInfoList();
    }

    private final String[] nodeIds = {"DGB7041063100", "DGB7041061300", "DGB7041001200","DGB7041001100","DGB7041033700",
            "DGB7041001000","DGB7041000800","DGB7041000500","DGB7041000900","DGB7041000300","DGB7041000400"}; // 정류장 ID 배열
    @Scheduled(fixedDelay = 30000) // 10초마다 실행
    public void fetchBusArrivalInfo() {
        List<BusApiServiceItem> allBusArrivalInfo = new ArrayList<>();
        for (String nodeId : nodeIds) {
            ResponseEntity<BusApiServiceItems> responseEntity = callBusSttnAcctoArvlPrearngeInfoListApi("1", "10", nodeId);
            BusApiServiceItems busApiServiceItems = responseEntity.getBody();
            List<BusApiServiceItem> sortedItems = busApiService.sortItemsByArrivalTime(busApiServiceItems.getBusApiServiceItem());

            // 각 항목에 노선 정보를 추가합니다.
            for (BusApiServiceItem item : sortedItems) {
                String routeId = item.getRouteid();
                if (busRoutes.containsKey(routeId)) {
                    item.setRouteInfo(busRoutes.get(routeId));
                }
            }

            allBusArrivalInfo.addAll(sortedItems);

            logger.info("Received bus arrival info: {}", sortedItems);
        }
        storage.setBusArrivalInfoList(allBusArrivalInfo);
    }

    @GetMapping("/BusSttnInfo")//정류소별도착예정정보 목록 조회
    public ResponseEntity<BusApiServiceItems> callBusSttnAcctoArvlPrearngeInfoListApi(
            @RequestParam(value="pageNo") String pageNo,
            @RequestParam(value="numOfRows") String numOfRows,
            @RequestParam(value="nodeId") String nodeId

    ){
        HttpURLConnection urlConnection = null;
        InputStream stream = null;
        String result = null;

        String urlStr = OpenApiCallName.ARRENDPOINT +
                "?serviceKey=" + SERVICE_KEY +
                "&numOfRows=" + numOfRows +
                "&pageNo=" + pageNo +
                "&_type=" + "json" +
                "&cityCode=" + cityCode +
                "&nodeId=" + nodeId;

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

        BusApiServiceItems response = busApiService.parsingJsonObject(result);
        if (response.getBusApiServiceItem().isEmpty()) {
            return new ResponseEntity<>(new BusApiServiceItems(new ArrayList<>()), HttpStatus.NO_CONTENT);
        }
        // 도착 시간에 따라 정렬
        List<BusApiServiceItem> sortedItems = busApiService.sortItemsByArrivalTime(response.getBusApiServiceItem());
        response.setBusApiServiceItem(sortedItems);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/RouteBusArvlInfo")//정류소별특정노선버스 도착예정정보 목록조회
    public ResponseEntity<BusRouteAcctoSpcifySttnServiceItems> callBusSttnAcctoSpcifyRouteBusArvlPrearngeInfoApi(
            @RequestParam(value="pageNo") String pageNo,
            @RequestParam(value="numOfRows") String numOfRows,
            @RequestParam(value="nodeId") String nodeId,
            @RequestParam(value="routeId") String routeId

    ){
        HttpURLConnection urlConnection = null;
        InputStream stream = null;
        String result = null;

        String urlStr = OpenApiCallName.NODEARRENDPOINT +
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

        BusRouteAcctoSpcifySttnServiceItems response = busRouteAcctoSpcifySttnService.parsingJsonObject(result); //수정 필요
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
