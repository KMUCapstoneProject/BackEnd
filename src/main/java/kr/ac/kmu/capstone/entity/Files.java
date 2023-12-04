package kr.ac.kmu.Capstone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "files")
public class Files {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="file_id")
    private Long fileId;

    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private Long size;

    /*@ManyToOne(targetEntity = Posting.class, fetch = FetchType.EAGER) //Many = File, One = posting, 한 게시글에 여러 개 파일
    @JoinColumn(name = "post_id")*/
    private Long postId;

    public Files(String fileName, String fileDownloadUri, String fileType, Long size, Long postId) {
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.size = size;
        this.postId = postId;
    }
}
