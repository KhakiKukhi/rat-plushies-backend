package lol.khakikukhi.ratplushies.utils;

import com.password4j.Argon2Function;
import com.password4j.BadParametersException;
import com.password4j.Password;
import com.password4j.types.Argon2;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PasswordHasherTest {

    @Test
    void verify_ShouldReturnTrue_WhenPasswordMatchesHashedPassword() {
        // Arrange
        String plainPassword = "securePassword123";
        String hashedPassword = Password.hash(plainPassword)
                .with(Argon2Function.getInstance(65536, 3, 1, 32, Argon2.ID, 19))
                .getResult();
        PasswordHasher passwordHasher = new PasswordHasher();

        // Act
        boolean result = passwordHasher.verify(plainPassword, hashedPassword);

        // Assert
        assertTrue(result);
    }

    @Test
    void verify_ShouldReturnFalse_WhenPasswordDoesNotMatchHashedPassword() {
        // Arrange
        String plainPassword = "securePassword123";
        String incorrectPassword = "wrongPassword";
        String hashedPassword = Password.hash(plainPassword)
                .with(Argon2Function.getInstance(65536, 3, 1, 32, Argon2.ID, 19))
                .getResult();
        PasswordHasher passwordHasher = new PasswordHasher();

        // Act
        boolean result = passwordHasher.verify(incorrectPassword, hashedPassword);

        // Assert
        assertFalse(result);
    }

    @Test
    void verify_ShouldReturnFalse_WhenInvalidHashedPasswordIsProvided() {
        // Arrange
        String plainPassword = "securePassword123";
        String invalidHashedPassword = "invalidHashedPassword";
        PasswordHasher passwordHasher = new PasswordHasher();

        assertThrows(BadParametersException.class, () -> passwordHasher.verify(plainPassword, invalidHashedPassword));
    }
}