package lol.khakikukhi.ratplushies.application.services;

import jakarta.persistence.EntityNotFoundException;
import lol.khakikukhi.ratplushies.domain.entities.Owner;
import lol.khakikukhi.ratplushies.domain.entities.Rat;
import lol.khakikukhi.ratplushies.infrastructure.repositories.OwnerRepository;
import lol.khakikukhi.ratplushies.infrastructure.repositories.RatRepository;
import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class ProfilePicturesService {

    @Value("${upload.dir}")
    private String uploadDir;

    @Value("${upload.max-width}")  private int maxWidth;
    @Value("${upload.max-height}") private int maxHeight;

    private final OwnerRepository ownerRepository;
    private final RatRepository ratRepository;

    public void uploadProfilePictureOwner(String userId, MultipartFile file) throws IOException {
        Owner owner = ownerRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Owner not found"));
        byte[] imageData = fileCheck(file);
        String filename = owner.getId() + getExtension(file.getOriginalFilename());
        saveFile(filename, imageData);
        owner.setProfilePicture(filename);
        ownerRepository.save(owner);
    }

    public void uploadProfilePictureRat(String userId, String ratId, MultipartFile file) throws IOException {
        Owner owner = ownerRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Owner not found!"));
        Rat rat = ratRepository.findById(ratId).orElseThrow(() -> new EntityNotFoundException("Rat not found!"));
        if (owner.ownsRat(rat)) {
            byte[] imageData = fileCheck(file);
            String filename = rat.getId() + getExtension(file.getOriginalFilename());
            saveFile(filename, imageData);
            rat.setProfilePicture(filename);
            ratRepository.save(rat);
        } else {
            throw new IllegalArgumentException("Rat not owned by this owner!");
        }
    }

    private byte[] fileCheck(MultipartFile file) throws IOException {
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


    private void saveFile(String filename, byte[] data) throws IOException {
        System.out.println( uploadDir.toString() );
        Path filePath = Paths.get(uploadDir, filename);
        Files.write(filePath, data);
    }

    private String getExtension(String originalFilename) {
        String ext = Optional.ofNullable(originalFilename)
                .filter(n -> n.contains("."))
                .map(n -> n.substring(n.lastIndexOf(".")).toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Couldn’t determine extension"));

        if (!List.of(".jpg",".jpeg",".png",".webp").contains(ext)) {
            throw new IllegalArgumentException("Unsupported extension");
        }
        return ext;
    }

}
