package kr.ac.kmu.capstone.service;

import kr.ac.kmu.capstone.dto.location.PointDto;
import kr.ac.kmu.capstone.dto.location.StatusDto;
import kr.ac.kmu.capstone.entity.Point;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.exceptions.ServiceUnavailableException;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationService {

    private final Driver driver; // neo4j Driver


    @Autowired
    public LocationService(Driver driver) {
        this.driver = driver;
    }

    public Path findDijkstraPath(String start_name, String end_name) {
        try(Session session = driver.session()) {
            String query = "MATCH(start:point {name : \'" + start_name + "\'}), (end:point {name : \'" + end_name + "\'})" +
                    " CALL apoc.algo.dijkstra(start,end,'status','weight') YIELD path,weight " +
                    "RETURN path, weight";

            Result result = session.run(query);
            System.out.println(result); // org.neo4j.driver.internal.InternalResult@6c3f7ad5
            //결과 처리
            if(result.hasNext()) {
                Record record = result.next();
                Path path = record.get("path").asPath(); // << 경로
                System.out.println(path);
                return path;
            } else {
                return null;
            }
        }
    }


    public Path findMyDijkstraPath(PointDto pointDto, String end_name) {
        try (Session session = driver.session()) {
            // 1. 우선 나와 가장 거리가 가까운 점을 찾아온다. >> 성공
            // 2. 가장 가까운 점과 종점을 dijkstra 해서 거리를 구한다. >> 성공
            // 3. 출력 해준다 >> 성공

            String findNodeQuery = "MATCH (n)\n" +
                    "WITH n, point({latitude: n.latitude, longitude: n.longitude}) AS nodeLocation,\n" +
                    "point({latitude: " + pointDto.getLatitude() + ", longitude: " + pointDto.getLongitude() + "}) AS newLocation\n" +
                    "RETURN n, point.distance(nodeLocation, newLocation) AS distance\n" +
                    "ORDER BY distance\n" +
                    "LIMIT 1;"; // << 나와 가장 가까운 점 1개

            Result findNodeResult = session.run(findNodeQuery);

            if (findNodeResult.hasNext()) {
                Record record = findNodeResult.next();
                System.out.println(record);
                Node node = record.get("n").asNode();

                String nodeName = node.get("name").asString();
                double nodeLatitude = node.get("latitude").asDouble();
                double nodeLongitude = node.get("longitude").asDouble();

                Point point = new Point(nodeName, nodeLatitude, nodeLongitude);

                String query = "MATCH(start:point {name : \'" + point.getName() + "\'}), (end:point {name : \'" + end_name + "\'})" +
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

    //계단 없는 버전
    public Path findMyDijkstraPath_A(PointDto pointDto, String end_name) {
        try (Session session = driver.session()) {
            // 1. 우선 나와 가장 거리가 가까운 점을 찾아온다. >> 성공
            // 2. 가장 가까운 점과 종점을 dijkstra 해서 거리를 구한다.
            // 3. 출력 해준다

            String findNodeQuery = "MATCH (n)\n" +
                    "WITH n, point({latitude: n.latitude, longitude: n.longitude}) AS nodeLocation,\n" +
                    "point({latitude: " + pointDto.getLatitude() + ", longitude: " + pointDto.getLongitude() + "}) AS newLocation\n" +
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

                Point point = new Point(nodeName, nodeLatitude, nodeLongitude);

                String query = "MATCH(start:point {name : \'" + point.getName() + "\'}), (end:point {name : \'" + end_name + "\'})" +
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

    public List<PointDto> loadAllPoint() {
        List<PointDto> locations = new ArrayList<>();
        String query = "Match (n) WHERE NOT n.name ENDS WITH '_A' return n";

        try (Session session = driver.session()) {
            Result result = session.run(query);
            //System.out.println(result);

            while (result.hasNext()) {
                Record record = result.next();
                PointDto pointdto = new PointDto();
                pointdto.setName(record.get("n").get("name").asString());
                pointdto.setLatitude(record.get("n").get("latitude").asDouble());
                pointdto.setLongitude(record.get("n").get("longitude").asDouble());

                locations.add(pointdto);
            }
        }catch (ServiceUnavailableException e) {
                e.printStackTrace();
        }
        return locations;
    }

    public List<StatusDto> loadAllStatus() {
        List<StatusDto> relationships = new ArrayList<>();
        String query = "MATCH (start)-[r:status]->(end) WHERE NOT start.name ENDS WITH '_A' " +
                       "RETURN start.name AS startNode, end.name AS endNode, r.value AS value, r.weight AS weight";

        try (Session session = driver.session()) {
            Result result = session.run(query);
            while (result.hasNext()) {
                Record record = result.next();
                StatusDto statusdto = new StatusDto();
                statusdto.setValue(record.get("value").asString());
                statusdto.setWeight(record.get("weight").asDouble());
                statusdto.setStart_name(record.get("startNode").asString());
                statusdto.setEnd_name(record.get("endNode").asString());
                relationships.add(statusdto);
            }
        }
        return relationships;
    }

    @Transactional
    public void createNode(PointDto pointDto) {
        try (Session session = driver.session()) {
            //create(n:point {name : '본관', latitude : 35.85579, longitude : 128.48889})
            String query1 = "create(n:point {name : '" + pointDto.getName() +
                    "' ,latitude : " + pointDto.getLatitude() + ", longitude : " + pointDto.getLongitude() + "})";
            session.run(query1);

            String query2 = "create(n:point {name : '" + pointDto.getName() +
                    "_A' ,latitude : " + pointDto.getLatitude() + ", longitude : " + pointDto.getLongitude() + "})";
            session.run(query2);

            System.out.println("success");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting node", e);
        }
    }

    @Transactional
    public void createStatus(StatusDto statusDto) {
        try (Session session = driver.session()) {
            //MATCH (a:point {name: '본관_A'}), (b:point {name: '본관12_A'})
            //   CREATE (a)-[r:status {value: 'road', weight: 100}]->(b);
            String query1 = "MATCH (a:point {name: '"+ statusDto.getStart_name() + "'}), (b:point {name: '" + statusDto.getEnd_name() + "'}) "+
                    "CREATE (a)-[r:status {value: '"+ statusDto.getValue() + "', weight: "+ statusDto.getWeight() +"}]->(b);" ;

            String query2 = "MATCH (a:point {name: '"+ statusDto.getEnd_name() + "'}), (b:point {name: '" + statusDto.getStart_name() + "'}) "+
                    "CREATE (a)-[r:status {value: '"+ statusDto.getValue() + "', weight: "+ statusDto.getWeight() +"}]->(b);" ;
            //System.out.println(pointDto.getName());
            session.run(query1);
            session.run(query2);
            if(!statusDto.getValue().equals("stair")) {
                String query3 = "MATCH (a:point {name: '"+ statusDto.getStart_name() + "_A'}), (b:point {name: '" + statusDto.getEnd_name() + "_A'}) "+
                        "CREATE (a)-[r:status {value: '"+ statusDto.getValue() + "', weight: "+ statusDto.getWeight() +"}]->(b);" ;

                String query4 = "MATCH (a:point {name: '"+ statusDto.getEnd_name() + "_A'}), (b:point {name: '" + statusDto.getStart_name() + "_A'}) "+
                        "CREATE (a)-[r:status {value: '"+ statusDto.getValue() + "', weight: "+ statusDto.getWeight() +"}]->(b);" ;
                //System.out.println(pointDto.getName());
                session.run(query3);
                session.run(query4);
            }
            System.out.println("success");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting node", e);
        }
    }

    @Transactional
    public void createStatusSolo(StatusDto statusDto) {
        try (Session session = driver.session()) {
            //MATCH (a:point {name: '본관_A'}), (b:point {name: '본관12_A'})
            //   CREATE (a)-[r:status {value: 'road', weight: 100}]->(b);
            String query1 = "MATCH (a:point {name: '"+ statusDto.getStart_name() + "'}), (b:point {name: '" + statusDto.getEnd_name() + "'}) "+
                    "CREATE (a)-[r:status {value: '"+ statusDto.getValue() + "', weight: "+ statusDto.getWeight() +"}]->(b);" ;

            System.out.println(query1);
            session.run(query1);
            if(!statusDto.getValue().equals("stair")) {
                String query3 = "MATCH (a:point {name: '"+ statusDto.getStart_name() + "_A'}), (b:point {name: '" + statusDto.getEnd_name() + "_A'}) "+
                        "CREATE (a)-[r:status {value: '"+ statusDto.getValue() + "', weight: "+ statusDto.getWeight() +"}]->(b);" ;

                System.out.println(query3);
                session.run(query3);
            }
            System.out.println("success");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting node", e);
        }
    }

    @Transactional
    public void deleteNode(String name) {
        try (Session session = driver.session()) {
            // MATCH (n) WHERE n.name = 'test3' DELETE n
            String query1 = "MATCH (n) WHERE n.name = '" + name + "' DELETE n";
            String query2 = "MATCH (n) WHERE n.name = '" + name + "_A' DELETE n";

            session.run(query1);
            session.run(query2);
            System.out.println("success");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting node", e);
        }
    }

    @Transactional
    public void deleteStatus(String start_name, String end_name) {
        // MATCH (n1 {name: 'test1'})-[r:status]-(n2 {name: 'test2'}) DELETE r
        try (Session session = driver.session()) {
            String query1 = "MATCH (n1 {name: '" + start_name + "'})-[r:status]-(n2 {name: '" + end_name + "'}) DELETE r";
            String query2 = "MATCH (n1 {name: '" + start_name + "_A'})-[r:status]-(n2 {name: '" + end_name + "_A'}) DELETE r";
            session.run(query1);
            session.run(query2);
            System.out.println("success");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting node", e);
        }
    }
}