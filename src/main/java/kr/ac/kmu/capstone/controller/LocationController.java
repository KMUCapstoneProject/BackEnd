package kr.ac.kmu.Capstone.controller;

import kr.ac.kmu.Capstone.dto.location.LocationDto;
import kr.ac.kmu.Capstone.service.LocationService;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/location")
public class LocationController {

    @Autowired
    private LocationService locationService;


    @GetMapping("/find")
    public ResponseEntity<String> findPath(@RequestParam String start_name, @RequestParam String end_name) {
        //start_name = 동산도서관, end_name = 영암관

        //System.out.println("1"); // << 진행 위치 겸 테스트용으로 1 찍은거
        Path resultPath = locationService.findDijkstraPath(start_name, end_name);
        String test = convertPathToText(resultPath);
        //System.out.println("2"); // // << 진행 위치 겸 테스트용으로 2 찍은거
        if(resultPath != null) {
            System.out.println(test);
            return new ResponseEntity<>(test, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/find_a")
    public ResponseEntity<String> findPath_A(@RequestParam String start_name, @RequestParam String end_name) {
        //start_name = 동산도서관, end_name = 영암관
        //System.out.println("1"); // << 진행 위치 겸 테스트용으로 1 찍은거

        String start_name_A = start_name + "_A";
        String end_name_A = end_name + "_A";

        Path resultPath = locationService.findDijkstraPath(start_name_A, end_name_A);
        String test = convertPathToText(resultPath);
        //System.out.println("2"); // // << 진행 위치 겸 테스트용으로 2 찍은거
        if(resultPath != null) {
            System.out.println(test);
            return new ResponseEntity<>(test, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/findMe")
    public ResponseEntity<String> findMyPath(
            @RequestParam String start_position,
            @RequestParam double start_pos_latitude,
            @RequestParam double start_pos_longitude,
            @RequestParam String end_name) {
        //start_name = 내 위치, end_name = 영암관

        LocationDto locationDto = new LocationDto();
        locationDto.setName(start_position);
        locationDto.setLatitude(start_pos_latitude);
        locationDto.setLongitude(start_pos_longitude);


        Path resultPath = locationService.findMyDijkstraPath(locationDto, end_name);
        String test = convertPathToText(resultPath);
        //System.out.println("2"); // // << 진행 위치 겸 테스트용으로 2 찍은거
        if(resultPath != null) {
            System.out.println(test);
            return new ResponseEntity<>(test, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/findMe_A")
    public ResponseEntity<String> findMyPath_A(
            @RequestParam String start_position,
            @RequestParam double start_pos_latitude,
            @RequestParam double start_pos_longitude,
            @RequestParam String end_name) {
        //start_name = 내 위치, end_name = 영암관

        LocationDto locationDto = new LocationDto();
        locationDto.setName(start_position + "_A");
        locationDto.setLatitude(start_pos_latitude);
        locationDto.setLongitude(start_pos_longitude);

        String end_name_A = end_name + "_A";
        Path resultPath = locationService.findMyDijkstraPath_A(locationDto, end_name_A);
        String test = convertPathToText(resultPath);
        if(resultPath != null) {
            return new ResponseEntity<>(test, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public static String convertPathToText(Path path) {
        StringBuilder result = new StringBuilder();

        Iterable<Node> nodes = path.nodes();
        for (Node node : nodes) {
            result.append(convertNodeToText(node)).append("/");
        }

        // Remove the trailing comma and space
        if (result.length() > 0) {
            result.setLength(result.length() - 1);
        }

        return result.toString();
    }

    private static String convertNodeToText(Node node) {

        double latitude = node.get("latitude").asDouble();
        double longitude = node.get("longitude").asDouble();

        return String.format("%f,%f", latitude, longitude);
    }
}