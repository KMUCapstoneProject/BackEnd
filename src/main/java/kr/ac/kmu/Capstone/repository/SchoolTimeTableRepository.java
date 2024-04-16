package kr.ac.kmu.Capstone.repository;

import kr.ac.kmu.Capstone.entity.SchoolTimeTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SchoolTimeTableRepository extends JpaRepository<SchoolTimeTable, Long> {
    List<SchoolTimeTable> findByBuilding(String building);

    Optional<SchoolTimeTable> findByTimetableId(Long timetableId);

    // 과목명 검색
    List<SchoolTimeTable> findByLectureNameContaining(String lectureName);

    // 강좌번호 검색
    List<SchoolTimeTable> findByLectureNumContaining(String lectureNum);
}
