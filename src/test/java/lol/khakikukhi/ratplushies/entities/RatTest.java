package lol.khakikukhi.ratplushies.entities;

import jakarta.transaction.Transactional;
import lol.khakikukhi.ratplushies.repositories.RatRepository;
import lol.khakikukhi.ratplushies.repositories.OwnerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RatTest {

    @Autowired
    private RatRepository ratRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Test
    void testIdIsGeneratedOnPersist() {
        Owner owner = new Owner();
        owner.setUsername("testuser");
        owner.setPassword("secret123");
        ownerRepository.save(owner);

        Rat rat = new Rat();
        rat.setName("Nibbles");
        rat.setOwner(owner);

        ratRepository.save(rat);
        assertNotNull(rat.getRatId());
        assertTrue(rat.getRatId().startsWith("RAT_"));
        assertEquals(36, rat.getRatId().length());
    }

    @Test
    void testPartnerBidirectionalSync() {
        Rat ratA = new Rat();
        ratA.setName("Cheddar");

        Rat ratB = new Rat();
        ratB.setName("Brie");

        ratA.addPartner(ratB);

        assertTrue(ratA.getPartners().contains(ratB));
        assertTrue(ratB.getPartners().contains(ratA));
    }

    @Test
    void testAddAndRemovePartner() {
        Rat ratA = new Rat();
        ratA.setName("Gouda");

        Rat ratB = new Rat();
        ratB.setName("Feta");

        ratA.addPartner(ratB);
        ratA.removePartner(ratB);

        assertFalse(ratA.getPartners().contains(ratB));
        assertFalse(ratB.getPartners().contains(ratA));
    }

    @Test
    void testParentChildRelationship() {
        Rat parent = new Rat();
        parent.setName("Mama Rat");

        Rat child = new Rat();
        child.setName("Baby Rat");

        parent.addChild(child);

        assertTrue(parent.getChildren().contains(child));
        assertTrue(child.getParents().contains(parent));
    }

    @Test
    void testAddAndRemoveParent() {
        Rat parent = new Rat();
        parent.setName("Papa Rat");

        Rat child = new Rat();
        child.setName("Tiny Rat");

        child.addParent(parent);
        child.removeParent(parent);

        assertFalse(child.getParents().contains(parent));
        assertFalse(parent.getChildren().contains(child));
    }

    @Test
    void testRatPersistence() {
        Owner owner = new Owner();
        owner.setUsername("testuser");
        owner.setPassword("secret123");
        ownerRepository.save(owner);

        Rat rat = new Rat();
        rat.setName("Whiskers");
        rat.setOwner(owner);

        ratRepository.save(rat);

        Optional<Rat> retrieved = ratRepository.findById(rat.getRatId());

        assertTrue(retrieved.isPresent());
        assertEquals("Whiskers", retrieved.get().getName());
        assertEquals("testuser", retrieved.get().getOwner().getUsername());
    }

    @Test
    void testSaveBidirectionalChildren() {
        Rat parent = new Rat();
        parent.setName("Queen");

        Rat child = new Rat();
        child.setName("Heir");

        parent.addChild(child);

        ratRepository.save(parent);
        ratRepository.save(child);

        Optional<Rat> loadedParent = ratRepository.findById(parent.getRatId());
        Optional<Rat> loadedChild = ratRepository.findById(child.getRatId());

        assertTrue(loadedParent.isPresent());
        assertTrue(loadedChild.isPresent());

        assertTrue(loadedParent.get().getChildren().contains(loadedChild.get()));
        assertTrue(loadedChild.get().getParents().contains(loadedParent.get()));
    }
}
