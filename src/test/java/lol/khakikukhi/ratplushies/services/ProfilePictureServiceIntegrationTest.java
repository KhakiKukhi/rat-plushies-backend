package lol.khakikukhi.ratplushies.services;

import lol.khakikukhi.ratplushies.application.services.entity.OwnerService;
import lol.khakikukhi.ratplushies.application.services.entity.RatService;
import lol.khakikukhi.ratplushies.application.services.entity.RatOwnershipService;
import lol.khakikukhi.ratplushies.application.services.support.ProfilePictureService;
import lol.khakikukhi.ratplushies.domain.entities.Owner;
import lol.khakikukhi.ratplushies.domain.entities.Rat;
import lol.khakikukhi.ratplushies.domain.entities.associations.RatOwnership;
import lol.khakikukhi.ratplushies.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProfilePictureServiceIntegrationTest {

    @Autowired
    private ProfilePictureService service;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private RatService ratService;

    @Autowired
    private RatOwnershipService ratOwnershipService;

    @TempDir
    static Path uploadDir;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestUtils testUtils;


    @DynamicPropertySource
    static void registerDynamicProps(DynamicPropertyRegistry registry) {
        registry.add("upload.dir", () -> uploadDir.toString());
        registry.add("upload.max-width", () -> 1000);
        registry.add("upload.max-height", () -> 1000);
    }

    @Test
    void uploadOwnerProfilePicture_happyPath() throws Exception {
        // given
        Owner owner = testUtils.createOwner("alice");

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
        Owner updated = ownerService.getOwnerById(owner.getId());
        String expected = owner.getId() + ".png";
        assertEquals(expected, updated.getProfilePicture());
        assertTrue(Files.exists(Path.of(uploadDir.toString(), expected)));

        mockMvc.perform(get("/uploads/" + expected))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/png")) // or whatever the mime type is
                .andExpect(content().bytes(baos.toByteArray()));

    }

    @Test
    void uploadRatProfilePicture_happyPath() throws IOException {
        // given
        Rat rat = testUtils.createRat("Remy");

        Owner owner = testUtils.createOwner("bob");

        this.ratOwnershipService.saveOwnership(new RatOwnership(owner, rat));


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
        Rat updated = ratService.getRatById(rat.getId());
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
    void uploadRatProfilePicture_notOwner() throws IOException {
        // given
        Rat rat = testUtils.createRat("Remy");

        Owner owner = testUtils.createOwner("bob");

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
        assertThrows(IllegalArgumentException.class, () -> service.uploadProfilePictureRat(owner.getId(), rat.getId(), file));
    }


    @Test
    void emptyFile_throwsIllegalArgument() {
        Owner owner = testUtils.createOwner("charlie");

        var emptyFile = new MockMultipartFile("file", "empty.png", "image/png", new byte[0]);
        var ex = assertThrows(IllegalArgumentException.class,
                () -> service.uploadProfilePictureOwner(owner.getId(), emptyFile)
        );
        assertEquals("Empty file", ex.getMessage());
    }

    @Test
    void invalidContentType_throwsIllegalArgument() {
        Owner owner = testUtils.createOwner("dan");

        var txtFile = new MockMultipartFile("file", "foo.txt", "text/plain", "hello".getBytes());
        assertThrows(IllegalArgumentException.class,
                () -> service.uploadProfilePictureOwner(owner.getId(), txtFile)
        );
    }

    @Test
    void tooLargeImage_throwsIllegalArgument() throws IOException {
        Owner owner = testUtils.createOwner("eve");

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
        var file = new MockMultipartFile("file", "pic.png", "image/png", new byte[]{1, 2, 3});
        assertThrows(IllegalArgumentException.class,
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
