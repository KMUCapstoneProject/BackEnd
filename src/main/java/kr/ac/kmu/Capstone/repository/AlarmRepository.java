package kr.ac.kmu.Capstone.repository;


import kr.ac.kmu.Capstone.dto.notification.AlarmDto;
import kr.ac.kmu.Capstone.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO capstone_test.notification (email, device_token, alarm_check) VALUES (:email, :device_token, :alarm_check)", nativeQuery = true)
    void insert(@Param("email") String email,
                @Param("device_token") String deviceToken,
                @Param("alarm_check") boolean isAlarmChecked);

    @Modifying
    @Transactional
    @Query(value = "UPDATE capstone_test.notification SET alarm_check = :alarm_check WHERE email = :email", nativeQuery = true)
    boolean update(@Param("email") String email,
                   @Param("alarm_check") boolean isAlarmChecked);


    @Query(value = "SELECT device_token FROM capstone_test.notification WHERE email = :email", nativeQuery = true)
    String selectDeviceTokenByEmail(@Param("email") String email);

    @Query(value = "SELECT alarm_check FROM capstone_test.notification WHERE email = :email", nativeQuery = true)
    boolean selectAlarmCheckedByEmail(@Param("email") String email);


    @Query(value = "SELECT email FROM capstone_test.notification WHERE alarm_check = true", nativeQuery = true)
    List<String> selectEmailByAlarmCheck();

}