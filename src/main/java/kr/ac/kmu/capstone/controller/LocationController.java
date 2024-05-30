package kr.ac.kmu.capstone.controller;

import kr.ac.kmu.capstone.dto.location.PointDto;
import kr.ac.kmu.capstone.dto.location.StatusDto;
import kr.ac.kmu.capstone.entity.Point;
import kr.ac.kmu.capstone.entity.Status;
import kr.ac.kmu.capstone.service.LocationService;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/location-service/location")
public class LocationController {

    @Autowired
    private LocationService locationService;


    @GetMapping("/find")
    public ResponseEntity<String> getFindPath(
            @RequestParam String start_name,
            @RequestParam String end_name) {

        Path resultPath = locationService.findDijkstraPath(start_name, end_name);
        String test = convertPathToText(resultPath);

        if(resultPath != null) {
            System.out.println(test);
            return new ResponseEntity<>(test, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/find_A")
    public ResponseEntity<String> getFindPath_A(
            @RequestParam String start_name,
            @RequestParam String end_name) {

        String start_name_A = start_name + "_A";
        String end_name_A = end_name + "_A";

        Path resultPath = locationService.findDijkstraPath(start_name_A, end_name_A);
        String test = convertPathToText(resultPath);

        if(resultPath != null) {
            System.out.println(test);
            return new ResponseEntity<>(test, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/findMe")
    public ResponseEntity<String> getFindMyPath(
            @RequestParam String start_position,
            @RequestParam double start_pos_latitude,
            @RequestParam double start_pos_longitude,
            @RequestParam String end_name) {

        PointDto pointDto = new PointDto();
        pointDto.setName(start_position);
        pointDto.setLatitude(start_pos_latitude);
        pointDto.setLongitude(start_pos_longitude);


        Path resultPath = locationService.findMyDijkstraPath(pointDto, end_name);
        String test = convertPathToText(resultPath);

        if(resultPath != null) {
            System.out.println(test);
            return new ResponseEntity<>(test, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/findMe_A")
    public ResponseEntity<String> getFindMyPath_A(
            @RequestParam String start_position,
            @RequestParam double start_pos_latitude,
            @RequestParam double start_pos_longitude,
            @RequestParam String end_name) {

        PointDto pointDto = new PointDto();
        pointDto.setName(start_position + "_A");
        pointDto.setLatitude(start_pos_latitude);
        pointDto.setLongitude(start_pos_longitude);

        String end_name_A = end_name + "_A";
        Path resultPath = locationService.findMyDijkstraPath_A(pointDto, end_name_A);
        String test = convertPathToText(resultPath);
        if(resultPath != null) {
            return new ResponseEntity<>(test, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/loadAll_Node")
    public ResponseEntity<List<PointDto>> getLoadPoint() {

        List<PointDto> locations = locationService.loadAllPoint();
        return new ResponseEntity<>(locations, HttpStatus.OK);
    }
    @GetMapping("/loadAll_Status")
    public ResponseEntity<List<StatusDto>> getLoadStatus() {
        List<StatusDto> relationships = locationService.loadAllStatus();


        return new ResponseEntity<>(relationships, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Void> postCreateNode (
            @RequestParam String name,
            @RequestParam double node_latitude,
            @RequestParam double node_longitude) {

        PointDto pointDto = new PointDto();
        pointDto.setName(name);
        pointDto.setLatitude(node_latitude);
        pointDto.setLongitude(node_longitude);

        locationService.createNode(pointDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/create_status")
    public ResponseEntity<Void> postCreateStatus(
            @RequestParam String start_name,
            @RequestParam String end_name,
            @RequestParam String value,
            @RequestParam double weight
    ) {

        StatusDto statusDto = new StatusDto();
        statusDto.setStart_name(start_name);
        statusDto.setEnd_name(end_name);
        statusDto.setValue(value);
        statusDto.setWeight(weight);

        locationService.createStatus(statusDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> postDeleteNode (
            @RequestParam String name) {

        locationService.deleteNode(name);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/delete_Status")
    public ResponseEntity<Void> postDeleteStatus (
            @RequestParam String start_name,
            @RequestParam String end_name) {

        locationService.deleteStatus(start_name, end_name);
        return new ResponseEntity<>(HttpStatus.OK);
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

        return String.format("%.5f,%.5f", latitude, longitude);
    }
}