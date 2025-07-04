package lol.khakikukhi.ratplushies.infrastructure.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorage {
    void store(String filename, byte[] imageData) throws IOException;
    byte[] validateAndRead(MultipartFile file) throws IOException;
    String getValidExtension(String originalFilename);
}
