package lol.khakikukhi.ratplushies.entities;

import lol.khakikukhi.ratplushies.repositories.OwnerRepository;
import lol.khakikukhi.ratplushies.repositories.RatRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class OwnerRatRelationTest {

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private RatRepository ratRepository;

    @Test
    void ratCanBeCreatedWithoutOwner() {
        Rat rat = new Rat();
        rat.setName("Solo");

        ratRepository.save(rat);

        Optional<Rat> found = ratRepository.findById(rat.getRatId());
        assertTrue(found.isPresent());
        assertNull(found.get().getOwner());
    }

    @Test
    void ratCanBeAssignedOwnerLater() {
        Rat rat = new Rat();
        rat.setName("Scruffy");

        ratRepository.save(rat);

        Owner owner = new Owner();
        owner.setUsername("scruffmaster");
        owner.setPassword("pw");
        ownerRepository.save(owner);

        rat.setOwner(owner);
        ratRepository.save(rat);

        Rat updated = ratRepository.findById(rat.getRatId()).orElseThrow();
        assertEquals(owner.getOwnerId(), updated.getOwner().getOwnerId());
    }

    @Test
    void ratOwnershipCanBeSwitched() {
        Owner originalOwner = new Owner();
        originalOwner.setUsername("old");
        originalOwner.setPassword("123");
        ownerRepository.save(originalOwner);

        Owner newOwner = new Owner();
        newOwner.setUsername("new");
        newOwner.setPassword("456");
        ownerRepository.save(newOwner);

        Rat rat = new Rat();
        rat.setName("Switchy");
        rat.setOwner(originalOwner);
        ratRepository.save(rat);

        // Switch owner
        rat.setOwner(newOwner);
        ratRepository.save(rat);

        Rat fetched = ratRepository.findById(rat.getRatId()).orElseThrow();
        assertEquals(newOwner.getOwnerId(), fetched.getOwner().getOwnerId());
    }

    @Test
    void ratPersistsAfterOwnerDeleted() {
        Owner owner = new Owner();
        owner.setUsername("ghost");
        owner.setPassword("pw");
        ownerRepository.save(owner);

        Rat rat = new Rat();
        rat.setName("LeftBehind");
        rat.setOwner(owner);
        ratRepository.save(rat);

        owner.clearAllRats();
        ownerRepository.delete(owner);

        Rat stillThere = ratRepository.findById(rat.getRatId()).orElseThrow();
        assertNull(stillThere.getOwner());
    }

    @Test
    void ownerSeesAllOwnedRats() {
        Owner owner = new Owner();
        owner.setUsername("ratcollector");
        owner.setPassword("secure");
        ownerRepository.save(owner);

        Rat r1 = new Rat();
        r1.setName("Alpha");
        owner.addRat(r1);

        Rat r2 = new Rat();
        r2.setName("Beta");
        owner.addRat(r2);

        ratRepository.save(r1);
        ratRepository.save(r2);

        Owner fetched = ownerRepository.findById(owner.getOwnerId()).orElseThrow();
        assertEquals(2, fetched.getRats().size());
    }

    @Test
    void reassigningRatRemovesItFromOldOwner() {
        // Create Owner A
        Owner ownerA = new Owner();
        ownerA.setUsername("first");
        ownerA.setPassword("a123");

        // Create Owner B
        Owner ownerB = new Owner();
        ownerB.setUsername("second");
        ownerB.setPassword("b123");

        ownerRepository.save(ownerA);
        ownerRepository.save(ownerB);

        // Create Rat and assign to A
        Rat rat = new Rat();
        rat.setName("Ghosty");
        ownerA.addRat(rat); // keeps both sides synced

        ratRepository.save(rat);

        // Confirm Rat is in A's list
        Owner freshA = ownerRepository.findById(ownerA.getOwnerId()).orElseThrow();
        assertEquals(1, freshA.getRats().size());
        assertEquals("Ghosty", freshA.getRats().get(0).getName());

        // Reassign to B using .addRat()
        ownerB.addRat(rat); // should remove from A and assign to B

        ratRepository.save(rat);
        ownerRepository.save(ownerA);
        ownerRepository.save(ownerB);

        // Reload everything
        Rat updatedRat = ratRepository.findById(rat.getRatId()).orElseThrow();
        Owner updatedA = ownerRepository.findById(ownerA.getOwnerId()).orElseThrow();
        Owner updatedB = ownerRepository.findById(ownerB.getOwnerId()).orElseThrow();

        // Owner A should no longer have the rat
        assertFalse(updatedA.getRats().contains(updatedRat), "Old owner still references the rat — ghost rat!");

        // Owner B should now own the rat
        assertTrue(updatedB.getRats().contains(updatedRat), "New owner doesn't have the rat");
        assertEquals(updatedB.getOwnerId(), updatedRat.getOwner().getOwnerId(), "Rat's owner field is not updated correctly");
    }

    @Test
    void ratIsNotDeletedWhenOwnerIs() {
        Rat rat = new Rat();
        rat.setName("AloneRat");
        ratRepository.save(rat); // saved first, on its own

        Owner owner = new Owner();
        owner.setUsername("lonely_owner");
        owner.setPassword("<PASSWORD>");
        owner.addRat(rat); // links them
        ownerRepository.save(owner);

        // Delete the owner
        owner.removeRat(rat); // break the link properly
        ownerRepository.delete(owner);

        // Rat should still exist
        assertTrue(ratRepository.findById(rat.getRatId()).isPresent());
    }

}
