package lol.khakikukhi.ratplushies.infrastructure.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service("localImageStorage")
public class LocalImageStorageProvider implements FileStorage{
    @Value("${upload.dir}") private String uploadDir;
    @Value("${upload.max-width}") private int maxWidth;
    @Value("${upload.max-height}") private int maxHeight;

    @Override
    public byte[] validateAndRead(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Empty file");
        }

        String contentType = file.getContentType();
        if (!List.of("image/jpeg", "image/png", "image/webp").contains(contentType)) {
            throw new IllegalArgumentException("Only JPEG or PNG allowed");
        }

        byte[] data = file.getBytes(); // buffer once

        BufferedImage image = ImageIO.read(new ByteArrayInputStream(data));
        if (image == null) {
            throw new IllegalArgumentException("File is not a valid image");
        }

        if (image.getWidth() > maxWidth || image.getHeight() > maxHeight) {
            throw new IllegalArgumentException(String.format("Image too large. Max %dx%d pixels.", maxWidth, maxHeight));
        }

        return data;
    }

    @Override
    public void store(String filename, byte[] data) throws IOException {
        if (filename.contains("..")) {
            throw new IllegalArgumentException("Invalid filename");
        }

        System.out.println(uploadDir);
        Path filePath = Paths.get(uploadDir, filename);
        Files.write(filePath, data);
    }

    @Override
    public String getValidExtension(String filename) {
        String ext = Optional.ofNullable(filename)
                .filter(n -> n.contains("."))
                .map(n -> n.substring(n.lastIndexOf(".")).toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Couldn’t determine extension"));

        if (!List.of(".jpg",".jpeg",".png",".webp").contains(ext)) {
            throw new IllegalArgumentException("Unsupported extension");
        }
        return ext;
    }
}
