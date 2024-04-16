package kr.ac.kmu.Capstone.image;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

@Component
@ConfigurationProperties(prefix="file")
public class FileUploadProperties {
    @Value("file.upload-dir")
    private String uploadDir;

    @Value("file.excel-upload-dir")
    private String excelUploadDir;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
    public String getExcelUploadDir() {
        return excelUploadDir;
    }
    public void setExcelUploadDir(String excelUploadDir) {
        this.excelUploadDir = excelUploadDir;
    }
}
