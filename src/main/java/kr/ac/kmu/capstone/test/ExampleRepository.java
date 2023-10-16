package kr.ac.kmu.capstone.test;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExampleRepository extends JpaRepository<Example, Integer> {
    Example findById(int id);
}
