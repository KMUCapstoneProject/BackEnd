package kr.ac.kmu.Capstone.service;

import kr.ac.kmu.Capstone.dto.location.LocationDto;
import kr.ac.kmu.Capstone.entity.Location;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

    private final Driver driver; // neo4j Driver

    @Autowired
    public LocationService( Driver driver) {
        this.driver = driver;
    }

    public Path findDijkstraPath(String start_name, String end_name) {
        try(Session session = driver.session()) {
            String query = "MATCH(start:point {name : \'" + start_name + "\'}), (end:point {name : \'" + end_name + "\'})" +
                    " CALL apoc.algo.dijkstra(start,end,'status','weight') YIELD path,weight " +
                    "RETURN path, weight";

            Result result = session.run(query);
            //결과 처리
            if(result.hasNext()) {
                Record record = result.next();
                Path path = record.get("path").asPath();
                System.out.println("success");
                return path;
            } else {
                return null;
            }
        }
    }


    public Path findMyDijkstraPath(LocationDto locationDto, String end_name) {
        try (Session session = driver.session()) {
            // 1. 우선 나와 가장 거리가 가까운 점을 찾아온다. >> 성공
            // 2. 가장 가까운 점과 종점을 dijkstra 해서 거리를 구한다.
            // 3. 출력 해준다

            String findNodeQuery = "MATCH (n)\n" +
                    "WITH n, point({latitude: n.latitude, longitude: n.longitude}) AS nodeLocation,\n" +
                    "point({latitude: " + locationDto.getLatitude() + ", longitude: " + locationDto.getLongitude() + "}) AS newLocation\n" +
                    "RETURN n, point.distance(nodeLocation, newLocation) AS distance\n" +
                    "ORDER BY distance\n" +
                    "LIMIT 1;";

            Result findNodeResult = session.run(findNodeQuery);

            if (findNodeResult.hasNext()) {
                Record record = findNodeResult.next();
                Node node = record.get("n").asNode();

                String nodeName = node.get("name").asString();
                double nodeLatitude = node.get("latitude").asDouble();
                double nodeLongitude = node.get("longitude").asDouble();

                Location location = new Location(nodeName, nodeLatitude, nodeLongitude);

                String query = "MATCH(start:point {name : \'" + location.getName() + "\'}), (end:point {name : \'" + end_name + "\'})" +
                        " CALL apoc.algo.dijkstra(start,end,'status','weight') YIELD path,weight " +
                        "RETURN path, weight";

                Result dijkstraresult = session.run(query);
                if (dijkstraresult.hasNext()) {
                    Record dijkstraRecord = dijkstraresult.next();
                    Path path = dijkstraRecord.get("path").asPath();
                    return path;
                } else {
                }
            }
        }
        return null;
    }

    public Path findMyDijkstraPath_A(LocationDto locationDto, String end_name) {
        try (Session session = driver.session()) {
            // 1. 우선 나와 가장 거리가 가까운 점을 찾아온다. >> 성공
            // 2. 가장 가까운 점과 종점을 dijkstra 해서 거리를 구한다.
            // 3. 출력 해준다

            String findNodeQuery = "MATCH (n)\n" +
                    "WITH n, point({latitude: n.latitude, longitude: n.longitude}) AS nodeLocation,\n" +
                    "point({latitude: " + locationDto.getLatitude() + ", longitude: " + locationDto.getLongitude() + "}) AS newLocation\n" +
                    "RETURN n, point.distance(nodeLocation, newLocation) AS distance\n" +
                    "ORDER BY distance\n" +
                    "LIMIT 1;";

            Result findNodeResult = session.run(findNodeQuery);

            if (findNodeResult.hasNext()) {
                Record record = findNodeResult.next();
                Node node = record.get("n").asNode();

                String nodeName = node.get("name").asString() + "_A";
                double nodeLatitude = node.get("latitude").asDouble();
                double nodeLongitude = node.get("longitude").asDouble();

                Location location = new Location(nodeName, nodeLatitude, nodeLongitude);

                String query = "MATCH(start:point {name : \'" + location.getName() + "\'}), (end:point {name : \'" + end_name + "\'})" +
                        " CALL apoc.algo.dijkstra(start,end,'status','weight') YIELD path,weight " +
                        "RETURN path, weight";

                Result dijkstraresult = session.run(query);
                if (dijkstraresult.hasNext()) {
                    Record dijkstraRecord = dijkstraresult.next();
                    Path path = dijkstraRecord.get("path").asPath();
                    return path;
                } else {
                }
            }
        }
        return null;
    }
}