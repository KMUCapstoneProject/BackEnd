package kr.ac.kmu.Capstone.repository;

import kr.ac.kmu.Capstone.entity.UserLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLocRepository extends JpaRepository<UserLocation, Long> {
}
