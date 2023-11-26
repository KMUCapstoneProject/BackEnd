package kr.ac.kmu.Capstone.repository;

import kr.ac.kmu.Capstone.entity.Category;
import kr.ac.kmu.Capstone.entity.Posting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostingRepository extends JpaRepository<Posting, Long> {

    //List<Posting> findByCategory(Category category);
    List<Posting> findByCategoryAndStatus(Category category, int status);
    List<Posting> findByStatus(int status);
    List<Posting> findAll();
    List<Posting> findByTitleContainingAndStatus(String title, int status);
    List<Posting> findByTitleContainingAndCategoryAndStatus(String title, Category category, int status);
    Optional<Posting> findByPostIdAndStatus(Long postId, int status);

}
