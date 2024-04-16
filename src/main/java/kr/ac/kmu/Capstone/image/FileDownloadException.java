package kr.ac.kmu.Capstone.image;

public class FileDownloadException extends RuntimeException{
    public FileDownloadException(String message) {
        super(message);
    }

    public FileDownloadException(String message, Throwable cause) {
        super(message, cause);
    }
}
