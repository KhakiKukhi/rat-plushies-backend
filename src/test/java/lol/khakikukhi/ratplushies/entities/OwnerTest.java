package lol.khakikukhi.ratplushies;

import jakarta.transaction.Transactional;
import lol.khakikukhi.ratplushies.entities.Owner;
import lol.khakikukhi.ratplushies.repositories.OwnerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OwnerTest {

    @Autowired
    private OwnerRepository ownerRepository;

    @Test
    void saveOwner_withMinimalValidData_shouldPersist() {
        Owner owner = new Owner();
        owner.setUsername("khaki");
        owner.setPassword("squeakyRatLover42");

        Owner saved = ownerRepository.save(owner);

        assertNotNull(saved.getOwnerId());
        assertTrue(saved.getOwnerId().startsWith("USR"));
        assertEquals("khaki", saved.getUsername());
        assertEquals("squeakyRatLover42", saved.getPassword());
    }

    @Test
    void saveOwner_withAllFieldsSet_shouldPersistAndRetrieveAccurately() {
        Owner owner = new Owner();
        owner.setUsername("cheeseFan");
        owner.setPassword("miceAreNice");
        owner.setEmailAddress("cheese@example.com");
        owner.setBirthday(Timestamp.from(Instant.parse("1990-05-15T00:00:00Z")));
        owner.setBio("I collect plush rats and actual ones too.");

        Owner saved = ownerRepository.save(owner);

        Optional<Owner> loaded = ownerRepository.findById(saved.getOwnerId());
        assertTrue(loaded.isPresent());

        Owner fetched = loaded.get();
        assertEquals("cheeseFan", fetched.getUsername());
        assertEquals("cheese@example.com", fetched.getEmailAddress());
        assertEquals("I collect plush rats and actual ones too.", fetched.getBio());
        assertEquals(saved.getBirthday(), fetched.getBirthday());
    }

    @Test
    void ownerId_shouldAutoGenerateOnPersist() {
        Owner owner = new Owner();
        owner.setUsername("autoID");
        owner.setPassword("secure");

        assertNull(owner.getOwnerId());

        Owner saved = ownerRepository.save(owner);

        assertNotNull(saved.getOwnerId());
        assertTrue(saved.getOwnerId().matches("^USR[0-9a-fA-F]{32}$"), "ID should be 'USR' + 32 hex chars");
    }

    @Test
    void saveOwner_missingUsername_shouldThrowException() {
        Owner owner = new Owner();
        owner.setPassword("somePass");

        assertThrows(Exception.class, () -> ownerRepository.saveAndFlush(owner));
    }

    @Test
    void saveOwner_missingPassword_shouldThrowException() {
        Owner owner = new Owner();
        owner.setUsername("noPass");

        assertThrows(Exception.class, () -> ownerRepository.saveAndFlush(owner));
    }
}
