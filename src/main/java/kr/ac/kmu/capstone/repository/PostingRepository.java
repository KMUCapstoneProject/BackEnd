package kr.ac.kmu.Capstone.repository;

import kr.ac.kmu.Capstone.entity.Category;
import kr.ac.kmu.Capstone.entity.Posting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostingRepository extends JpaRepository<Posting, Long> {

    List<Posting> findByCategory(Category category);

    List<Posting> findAll();

    List<Posting> findByTitleContaining(String title);
    List<Posting> findByTitleContainingAndCategory(String title, Category category);
}
