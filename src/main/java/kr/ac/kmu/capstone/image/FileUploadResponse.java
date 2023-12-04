package kr.ac.kmu.Capstone.image;

import lombok.Getter;

@Getter
public class FileUploadResponse {

    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private Long size;
    private Long postId;

    public FileUploadResponse(String fileName, String fileDownloadUri, String fileType, Long size, Long postId) {
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.size = size;
        this.postId = postId;
    }

}
