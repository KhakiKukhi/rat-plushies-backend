package lol.khakikukhi.ratplushies.infrastructure.configs;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
public class ConstructConfig {

    @Value("${upload.dir}")
    private String uploadDir;

    @PostConstruct
    private void createUploadsDirectory() throws IOException {
        Files.createDirectories(Paths.get(uploadDir));
    }

}
