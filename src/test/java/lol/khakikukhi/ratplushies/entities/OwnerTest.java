package lol.khakikukhi.ratplushies.entities;

import lol.khakikukhi.ratplushies.domain.entities.Owner;
import lol.khakikukhi.ratplushies.infrastructure.repositories.OwnerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
        owner.setPasswordHash("squeakyRatLover42");

        Owner saved = ownerRepository.save(owner);

        assertNotNull(saved.getId());
        assertTrue(saved.getId().startsWith("USR"));
        assertEquals("khaki", saved.getUsername());
        assertEquals("squeakyRatLover42", saved.getPasswordHash());
    }

    @Test
    void saveOwner_withAllFieldsSet_shouldPersistAndRetrieveAccurately() {
        Owner owner = new Owner();
        owner.setUsername("cheeseFan");
        owner.setPasswordHash("miceAreNice");
        owner.setEmailAddress("cheese@example.com");
        owner.setBirthday(Timestamp.from(Instant.parse("1990-05-15T00:00:00Z")));
        owner.setBio("I collect plush rats and actual ones too.");

        Owner saved = ownerRepository.save(owner);

        Optional<Owner> loaded = ownerRepository.findById(saved.getId());
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
        owner.setPasswordHash("secure");

        assertNull(owner.getId());

        Owner saved = ownerRepository.save(owner);

        assertNotNull(saved.getId());
        assertTrue(saved.getId().matches("^USR_[0-9a-fA-F]{32}$"), "ID should be 'USR_' + 32 hex chars");
    }

    @Test
    void saveOwner_missingUsername_shouldThrowException() {
        Owner owner = new Owner();
        owner.setPasswordHash("somePass");

        assertThrows(Exception.class, () -> ownerRepository.saveAndFlush(owner));
    }

    @Test
    void saveOwner_missingPassword_shouldThrowException() {
        Owner owner = new Owner();
        owner.setUsername("noPass");

        assertThrows(Exception.class, () -> ownerRepository.saveAndFlush(owner));
    }

}
