package kr.ac.kmu.Capstone.repository;

import kr.ac.kmu.Capstone.entity.TimeTable;
import kr.ac.kmu.Capstone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {

    List<TimeTable> findByUser(User user);
}
