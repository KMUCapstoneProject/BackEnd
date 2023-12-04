package kr.ac.kmu.Capstone.image;

import kr.ac.kmu.Capstone.entity.Files;
import kr.ac.kmu.Capstone.repository.PostingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileUploadDownloadService {

    private final Path fileLocation;
    private final FileRepository fileRepository;
    private PostingRepository postingRepository;

    @Autowired
    private FileUploadProperties fileUploadProperties;

    @Autowired
    public FileUploadDownloadService(FileUploadProperties prop, FileRepository fileRepository) {
        this.fileLocation = Paths.get(prop.getUploadDir())
                .toAbsolutePath().normalize();
        this.fileRepository = fileRepository; // 주입 받은 Repository 할당

        try {
            java.nio.file.Files.createDirectories(this.fileLocation);
        } catch (Exception e) {
            throw new FileUploadException("파일을 업로드할 디렉토리를 생성하지 못했습니다.", e);
        }
    }

    public Files storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (fileName.contains(".."))
                throw new FileUploadException("파일명에 부적합 문자가 포함되어 있습니다. " + fileName);

            String fileExtension = getFileExtension(fileName);
            String fileUploadDir;

            if (isImageFile(fileExtension)) {
                fileUploadDir = fileUploadProperties.getUploadDir();
            } else if (isExcelFile(fileExtension)) {
                fileUploadDir = fileUploadProperties.getExcelUploadDir();
            } else {
                throw new FileUploadException("유효하지 않은 파일 유형입니다.");
            }

            Path targetLocation = this.fileLocation.resolve(fileName);
            java.nio.file.Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // 파일 다운로드 URL 생성
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/downloadFile/")
                    .path(fileName)
                    .toUriString();

            Long maxPostId = postingRepository.findMaxPostId();

            Files saveFiles = new Files(fileName, fileDownloadUri, file.getContentType(), file.getSize(), maxPostId);
            fileRepository.save(saveFiles);

            return saveFiles;
        } catch (Exception e) {
            throw new FileUploadException("[" + fileName + "] 파일 업로드에 실패하였습니다. 다시 시도하십시오.", e);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if(resource.exists()) {
                return resource;
            }else {
                throw new FileDownloadException(fileName + " 파일을 찾을 수 없습니다.");
            }
        }catch(MalformedURLException e) {
            throw new FileDownloadException(fileName + " 파일을 찾을 수 없습니다.", e);
        }
    }
    private String getFileExtension(String fileName) {
        return StringUtils.getFilenameExtension(fileName);
    }

    // 이미지 파일 여부를 확인하는 메서드
    private boolean isImageFile(String fileExtension) {
        return fileExtension.equalsIgnoreCase("jpg") ||
                fileExtension.equalsIgnoreCase("jpeg") ||
                fileExtension.equalsIgnoreCase("png") ||
                fileExtension.equalsIgnoreCase("gif");
    }

    // 엑셀 파일 여부를 확인하는 메서드
    private boolean isExcelFile(String fileExtension) {
        return fileExtension.equalsIgnoreCase("xls") ||
                fileExtension.equalsIgnoreCase("xlsx");
    }
}
