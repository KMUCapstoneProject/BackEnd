package kr.ac.kmu.capstone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.repository.config.EnableReactiveNeo4jRepositories;

@SpringBootApplication
@EnableReactiveNeo4jRepositories
public class CapstoneApplication {
	public static void main(String[] args) {
		SpringApplication.run(CapstoneApplication.class, args);
	}

	
}
