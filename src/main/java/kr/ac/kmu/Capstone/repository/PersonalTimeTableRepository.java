package kr.ac.kmu.Capstone.repository;

import kr.ac.kmu.Capstone.entity.PersonalTimeTable;
import kr.ac.kmu.Capstone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonalTimeTableRepository extends JpaRepository<PersonalTimeTable, Long> {

    List<PersonalTimeTable> findByUser(User user);
}
