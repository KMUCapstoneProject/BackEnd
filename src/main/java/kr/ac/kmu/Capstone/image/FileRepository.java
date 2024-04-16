package kr.ac.kmu.Capstone.image;


import kr.ac.kmu.Capstone.entity.Files;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<Files, String> {

    List<Files> findByPostId(Long postId);
}
