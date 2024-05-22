package kr.ac.kmu.Capstone.repository;


import kr.ac.kmu.Capstone.entity.SubwayTimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubwayRepository extends JpaRepository<SubwayTimeEntity, Long> {
    @Query("SELECT COUNT(s) > 0 FROM SubwayTimeEntity s WHERE s.dailyTypeCode = :dailyTypeCode AND s.upDownTypeCode = :upDownTypeCode AND s.arrTime = :arrTime AND s.depTime = :depTime")
    boolean existsByUniqueKeys(@Param("dailyTypeCode") String dailyTypeCode,
                               @Param("upDownTypeCode") String upDownTypeCode,
                               @Param("arrTime") String arrTime,
                               @Param("depTime") String depTime);
    @Query("SELECT s FROM SubwayTimeEntity s WHERE s.subwayStationNm = :subwayStationNm AND s.dailyTypeCode = :dailyTypeCode AND s.upDownTypeCode = :up_down_type_code")
    List<SubwayTimeEntity> findSchedules(@Param("subwayStationNm") String subwayStationNm,
                                         @Param("dailyTypeCode") String dailyTypeCode,
                                         @Param("up_down_type_code") String upDownTypeCode);
}
