package kr.ac.kmu.Capstone.repository;

import kr.ac.kmu.Capstone.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    //Optional<RefreshToken> findByKey(String key);
}
