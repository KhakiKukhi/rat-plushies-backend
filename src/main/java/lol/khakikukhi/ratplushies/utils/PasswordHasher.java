package lol.khakikukhi.ratplushies.utils;

import com.password4j.Argon2Function;
import com.password4j.Hash;
import com.password4j.Password;
import com.password4j.types.Argon2;
import org.springframework.stereotype.Component;

@Component
public class PasswordHasher {

    private static final Argon2Function ARGON2 = Argon2Function.getInstance(
            65536,     // memory
            3,         // iterations
            1,         // parallelism
            32,        // output length
            Argon2.ID, // type
            19         // version
    );

    public String hash(String plainPassword) {
        Hash hash = Password.hash(plainPassword)
                .addRandomSalt()
                .with(ARGON2);
        return hash.getResult();
    }

    public boolean verify(String plainPassword, String hashedPassword) {
        return Password.check(plainPassword, hashedPassword)
                .with(ARGON2);
    }
}