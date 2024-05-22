package kr.ac.kmu.Capstone.OpenApi.Contorller;


import kr.ac.kmu.Capstone.OpenApi.Model.SubwayFindSttnListServiceItems;
import kr.ac.kmu.Capstone.OpenApi.Model.SubwayNearBusStopServiceItems;
import kr.ac.kmu.Capstone.OpenApi.Model.SubwayServiceItems;
import kr.ac.kmu.Capstone.OpenApi.OpenApiInfo.OpenApiCallName;
import kr.ac.kmu.Capstone.OpenApi.ParsingDecode.SubwayFindSttnListService;
import kr.ac.kmu.Capstone.OpenApi.ParsingDecode.SubwayNearBusStopService;
import kr.ac.kmu.Capstone.OpenApi.ParsingDecode.SubwayTimeApiService;
import kr.ac.kmu.Capstone.OpenApi.Utils.DateUtils;
import kr.ac.kmu.Capstone.dto.OpenApi.SubwayScheduleResponseDTO;
import lombok.RequiredArgsConstructor;
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
import java.util.List;

import static kr.ac.kmu.Capstone.OpenApi.OpenApiInfo.OpenApiServiceKey.SERVICE_KEY;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SubwayController {
    private final SubwayTimeApiService subwayTimeApiService;
    private final SubwayNearBusStopService subwayNearBusStopService;
    private final SubwayFindSttnListService subwayFindSttnListService;

    @Autowired
    private SubwayTimeApiService subwayTimeApiServices;

    @GetMapping("/subway-schedules")
    public ResponseEntity<List<String>> getSubwaySchedule() {
        List<String> scheduleList = subwayTimeApiServices.getSubwayScheduleList();
        if (scheduleList != null && !scheduleList.isEmpty()) {
            return ResponseEntity.ok(scheduleList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Scheduled(fixedRate = 60000)
    public void scheduleGetNextSchedule() {
        subwayTimeApiService.getSubwayScheduleList().clear();

        String[][] subwayStationPairs = {
                {"계명대", "강창"},
                {"강창", "계명대"},
                {"계명대", "성서산업단지"},
                {"강창", "대실"}
        };

        for (String[] pair : subwayStationPairs) {
            String subwayStationNm = pair[0];
            String directionStationNm = pair[1];
            subwayTimeApiService.logSubwaySchedule(subwayStationNm, directionStationNm);
        }
    }

    @GetMapping("/subwaySchedulegetTime")
    public ResponseEntity<SubwayScheduleResponseDTO> getNextSchedule(
            @RequestParam String subwayStationNm,
            @RequestParam String directionStationNm) {

        String dailyTypeCode = DateUtils.getDailyTypeCode();

        // upDownTypeCode를 판단하기 위한 로직 추가
        String upDownTypeCode = subwayTimeApiService.determineUpDownType(subwayStationNm, directionStationNm);

        // getTravelTime 메서드를 호출하여 SubwayScheduleResponseDTO를 반환 받음
        SubwayScheduleResponseDTO response = subwayTimeApiService.getTravelTime(subwayStationNm, directionStationNm, dailyTypeCode, upDownTypeCode);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/SubwaySchedule")//지하철역별 시간표 목록조회
    public ResponseEntity<SubwayServiceItems> callForecastApi(
            //인풋값 바꾸고
            @RequestParam(value="pageNo") String pageNo,
            @RequestParam(value="numOfRows") String numOfRows,
            @RequestParam(value="subwayStationId") String subwayStationId,
            @RequestParam(value="dailyTypeCode") String dailyTypeCode,
            @RequestParam(value="upDownTypeCode") String upDownTypeCode

    ){
        HttpURLConnection urlConnection = null;
        InputStream stream = null;
        String result = null;

        String urlStr = OpenApiCallName.SubwayTimeENDPOINT +
                "?serviceKey=" + SERVICE_KEY +
                "&numOfRows=" + numOfRows +
                "&pageNo=" + pageNo +
                "&_type=" + "json" +
                "&subwayStationId=" + subwayStationId +
                "&dailyTypeCode=" + dailyTypeCode +
                "&upDownTypeCode=" + upDownTypeCode;

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

        SubwayServiceItems response = subwayTimeApiService.parsingJsonObject(result);
        subwayTimeApiService.saveSubwayTimeItems(response.getSubwayServiceItem());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/FindSubway")//키워드기반 지하철역 목록 조회
    public ResponseEntity<SubwayFindSttnListServiceItems> callFndSubwayApi(
            @RequestParam(value="pageNo") String pageNo,
            @RequestParam(value="numOfRows") String numOfRows,
            @RequestParam(value="subwayStationName") String subwayStationName

    ){
        HttpURLConnection urlConnection = null;
        InputStream stream = null;
        String result = null;

        String urlStr = OpenApiCallName.KeyWordENDPOINT +
                "?serviceKey=" + SERVICE_KEY +
                "&numOfRows=" + numOfRows +
                "&pageNo=" + pageNo +
                "&_type=" + "json" +
                "&subwayStationName=" + subwayStationName;

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

        SubwayFindSttnListServiceItems response = subwayFindSttnListService.parsingJsonObject(result);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/FindSubwayBusRoute")//지하철역출구별 버스노선 목록 조회
    public ResponseEntity<SubwayNearBusStopServiceItems> callSubwayBusRouteApi(
            //인풋값 바꾸고
            @RequestParam(value="pageNo") String pageNo,
            @RequestParam(value="numOfRows") String numOfRows,
            @RequestParam(value="subwayStationId") String subwayStationId

    ){
        HttpURLConnection urlConnection = null;
        InputStream stream = null;
        String result = null;

        String urlStr = OpenApiCallName.SubwayBusENDPOINT +
                "?serviceKey=" + SERVICE_KEY +
                "&numOfRows=" + numOfRows +
                "&pageNo=" + pageNo +
                "&_type=" + "json" +
                "&subwayStationId=" + subwayStationId;

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

        SubwayNearBusStopServiceItems response = subwayNearBusStopService.parsingJsonObject(result); //수정 필요
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
