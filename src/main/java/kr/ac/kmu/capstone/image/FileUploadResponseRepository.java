package kr.ac.kmu.Capstone.image;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileUploadResponseRepository extends JpaRepository<FileUploadResponse, String> {
}
