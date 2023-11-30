package kr.ac.kmu.Capstone.repository;

import kr.ac.kmu.Capstone.entity.User;
import kr.ac.kmu.Capstone.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Message> findAllByReceiver(User user);
    List<Message> findAllBySender(User user);

}