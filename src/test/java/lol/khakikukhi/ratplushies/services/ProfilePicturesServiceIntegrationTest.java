package lol.khakikukhi.ratplushies.services;

import jakarta.persistence.EntityNotFoundException;
import lol.khakikukhi.ratplushies.entities.Owner;
import lol.khakikukhi.ratplushies.entities.Rat;
import lol.khakikukhi.ratplushies.repositories.OwnerRepository;
import lol.khakikukhi.ratplushies.repositories.RatRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProfilePicturesServiceIntegrationTest {

    @Autowired
    private ProfilePicturesService service;

    @Autowired
    private OwnerRepository ownerRepo;

    @Autowired
    private RatRepository ratRepo;

    @TempDir
    static Path uploadDir;

    @DynamicPropertySource
    static void registerDynamicProps(DynamicPropertyRegistry registry) {
        registry.add("upload.dir", () -> uploadDir.toString());
        registry.add("upload.max-width", () -> 1000);
        registry.add("upload.max-height", () -> 1000);
    }

    @Test
    void uploadOwnerProfilePicture_happyPath() throws IOException {
        // given
        Owner owner = new Owner();
        owner.setUsername("alice");
        owner.setPasswordHash("secret");
        ownerRepo.save(owner);

        // build a tiny 10×10 PNG in memory
        BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "png", baos);

        var file = new MockMultipartFile(
                "file",
                "pic.png",
                "image/png",
                baos.toByteArray()
        );

        // when
        service.uploadProfilePictureOwner(owner.getId(), file);

        // then
        Owner updated = ownerRepo.findById(owner.getId()).orElseThrow();
        String expected = owner.getId() + ".png";
        assertEquals(expected, updated.getProfilePicture());
        assertTrue(Files.exists(Path.of(uploadDir.toString(), expected)));
    }

    @Test
    void uploadRatProfilePicture_happyPath() throws IOException {
        // given
        Rat rat = new Rat();
        rat.setName("Remy");
        ratRepo.save(rat);

        Owner owner = new Owner();
        owner.setUsername("bob");
        owner.setPasswordHash("pw");
        owner.addRat(rat);
        ownerRepo.save(owner);

        // generate a random hex colour
        int r = (int) (Math.random() * 256);
        int g = (int) (Math.random() * 256);
        int b = (int) (Math.random() * 256);
        int rgb = (r << 16) | (g << 8) | b;

        // build 8x8 image with that colour
        BufferedImage img = new BufferedImage(8, 8, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                img.setRGB(x, y, rgb);
            }
        }

        // save to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "jpg", baos);

        var file = new MockMultipartFile(
                "file",
                "remy.jpg",
                "image/jpeg",
                baos.toByteArray()
        );

        // when
        service.uploadProfilePictureRat(owner.getId(), rat.getId(), file);

        // then
        Rat updated = ratRepo.findById(rat.getId()).orElseThrow();
        String expectedFilename = updated.getId() + ".jpg";
        Path savedPath = uploadDir.resolve(expectedFilename);

        assertEquals(expectedFilename, updated.getProfilePicture());
        assertTrue(Files.exists(savedPath));

        // read saved image
        BufferedImage savedImage = ImageIO.read(Files.newInputStream(savedPath));
        int savedRgb = savedImage.getRGB(0, 0) & 0xFFFFFF;

        int tolerance = 10;
        int diff = colorDiff(rgb, savedRgb);
        assertTrue(diff < tolerance, "Saved image colour should be within tolerance");
    }


    @Test
    void emptyFile_throwsIllegalArgument() {
        Owner owner = new Owner();
        owner.setUsername("bob");
        owner.setPasswordHash("pw");
        ownerRepo.save(owner);

        var emptyFile = new MockMultipartFile("file", "empty.png", "image/png", new byte[0]);
        var ex = assertThrows(IllegalArgumentException.class,
                () -> service.uploadProfilePictureOwner(owner.getId(), emptyFile)
        );
        assertEquals("Empty file", ex.getMessage());
    }

    @Test
    void invalidContentType_throwsIllegalArgument() {
        Owner owner = new Owner();
        owner.setUsername("charlie");
        owner.setPasswordHash("pw");
        ownerRepo.save(owner);

        var txtFile = new MockMultipartFile("file", "foo.txt", "text/plain", "hello".getBytes());
        assertThrows(IllegalArgumentException.class,
                () -> service.uploadProfilePictureOwner(owner.getId(), txtFile)
        );
    }

    @Test
    void tooLargeImage_throwsIllegalArgument() throws IOException {
        Owner owner = new Owner();
        owner.setUsername("dan");
        owner.setPasswordHash("pw");
        ownerRepo.save(owner);

        // 2000×200 px image
        BufferedImage bigImg = new BufferedImage(2000, 200, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bigImg, "png", baos);

        var file = new MockMultipartFile(
                "file",
                "big.png",
                "image/png",
                baos.toByteArray()
        );

        var ex = assertThrows(IllegalArgumentException.class,
                () -> service.uploadProfilePictureOwner(owner.getId(), file)
        );
        assertTrue(ex.getMessage().contains("Image too large"));
    }

    @Test
    void nonExistentOwner_throwsEntityNotFound() {
        var file = new MockMultipartFile("file", "pic.png", "image/png", new byte[]{1,2,3});
        assertThrows(EntityNotFoundException.class,
                () -> service.uploadProfilePictureOwner("USR_notreal", file)
        );
    }

    private int colorDiff(int rgb1, int rgb2) {
        int r1 = (rgb1 >> 16) & 0xFF;
        int g1 = (rgb1 >> 8) & 0xFF;
        int b1 = rgb1 & 0xFF;

        int r2 = (rgb2 >> 16) & 0xFF;
        int g2 = (rgb2 >> 8) & 0xFF;
        int b2 = rgb2 & 0xFF;

        int dr = r1 - r2;
        int dg = g1 - g2;
        int db = b1 - b2;

        return (int) Math.sqrt(dr * dr + dg * dg + db * db); // Euclidean RGB distance
    }
}
