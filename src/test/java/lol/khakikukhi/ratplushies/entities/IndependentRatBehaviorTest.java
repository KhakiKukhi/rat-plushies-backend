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
public class IndependentRatBehaviorTest {

    @Autowired
    private RatRepository ratRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Test
    void ratCanExistIndependentlyWithoutOwner() {
        // Create and save a rat without setting an owner.
        Rat rat = new Rat();
        rat.setName("IndependentRat");
        ratRepository.save(rat);

        // Retrieve the rat and verify that it persists and has no owner.
        Optional<Rat> retrieved = ratRepository.findById(rat.getRatId());
        assertTrue(retrieved.isPresent(), "Rat should be persisted independently.");
        assertNull(retrieved.get().getOwner(), "Rat created without owner should have a null owner.");
    }

    @Test
    void ratStaysAfterOwnerDeletion() {
        // Create and save a rat first, independently.
        Rat rat = new Rat();
        rat.setName("PersistentRat");
        ratRepository.save(rat);

        // Later, assign an owner to this existing rat.
        Owner owner = new Owner();
        owner.setUsername("tempOwner");
        owner.setPassword("tempPass");
        ownerRepository.save(owner);

        rat.setOwner(owner);
        ratRepository.save(rat);

        // Verify assignment is successful.
        Rat assigned = ratRepository.findById(rat.getRatId()).orElseThrow();
        assertNotNull(assigned.getOwner(), "Rat should have an owner after assignment.");
        assertEquals(owner.getOwnerId(), assigned.getOwner().getOwnerId(), "Owner id should match.");

        // Delete the owner. In our mapping, on owner deletion, rat's owner reference becomes null.
        owner.clearAllRats();
        ownerRepository.delete(owner);

        // The rat should still exist and not be deleted.
        Rat retrieved = ratRepository.findById(rat.getRatId()).orElseThrow();
        assertNotNull(retrieved, "Rat should still exist in the repository.");
        assertNull(retrieved.getOwner(), "Rat's owner should be null after its owner is deleted.");
    }

    @Test
    void ratCanBeAssignedAndUnassignedFromOwner() {
        // Create a rat independently.
        Rat rat = new Rat();
        rat.setName("SwitchableRat");
        ratRepository.save(rat);

        Owner firstOwner = new Owner();
        firstOwner.setUsername("firstOwner");
        firstOwner.setPassword("pass1");
        ownerRepository.save(firstOwner);

        // Assign the rat to the first owner.
        rat.setOwner(firstOwner);
        ratRepository.save(rat);
        Rat afterFirstAssign = ratRepository.findById(rat.getRatId()).orElseThrow();
        assertEquals(firstOwner.getOwnerId(), afterFirstAssign.getOwner().getOwnerId(),
                     "Rat should be assigned to first owner.");

        // Now, remove the association by unassigning the owner.
        firstOwner.removeRat(rat);
        ownerRepository.save(firstOwner);
        ratRepository.save(rat);

        Rat afterUnassign = ratRepository.findById(rat.getRatId()).orElseThrow();
        assertNull(afterUnassign.getOwner(), "Rat should have a null owner after unassignment.");

        // Finally, confirm the rat still exists independently even though owner assignment changed.
        assertTrue(ratRepository.findById(rat.getRatId()).isPresent(),
                   "Rat should persist regardless of owner assignments.");
    }
}