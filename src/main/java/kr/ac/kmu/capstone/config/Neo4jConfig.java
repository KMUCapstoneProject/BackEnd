package kr.ac.kmu.Capstone.config;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Neo4jConfig {

    @Bean
    public Driver neo4jDriver() {
        try {
            return GraphDatabase.driver("neo4j://friedkimchi.kdedevelop.com:7687", AuthTokens.basic("neo4j","friedkimchi"));
        } catch (Exception e) {
            // 연결 실패 시 예외 처리
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize Neo4j driver");
        }
    }
}