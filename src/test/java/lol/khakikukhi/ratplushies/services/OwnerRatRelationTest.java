package lol.khakikukhi.ratplushies.services;

import jakarta.transaction.Transactional;
import lol.khakikukhi.ratplushies.application.managers.OwnershipManager;
import lol.khakikukhi.ratplushies.application.services.entity.OwnerService;
import lol.khakikukhi.ratplushies.application.services.entity.RatOwnershipService;
import lol.khakikukhi.ratplushies.application.services.entity.RatService;
import lol.khakikukhi.ratplushies.domain.entities.Owner;
import lol.khakikukhi.ratplushies.domain.entities.Rat;
import lol.khakikukhi.ratplushies.domain.entities.associations.RatOwnership;
import lol.khakikukhi.ratplushies.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OwnerRatRelationTest {

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private RatService ratService;

    @Autowired
    private RatOwnershipService ratOwnershipService;

    @Autowired
    private TestUtils testUtils;

    @Autowired
    private OwnershipManager ownershipManager;
    

    @Test
    @Transactional
    void ratCanBeAssignedOwnerLater() {
        Rat rat = testUtils.createRat("Scruffy");

        Owner owner = testUtils.createOwner("scruffmaster");

        this.ownershipManager.assignOwnership(owner.getId(), rat.getId());
        assertTrue(this.ratOwnershipService.ratOwnershipExists(owner.getId(), rat.getId()));
    }

    @Test
    @Transactional
    void ratOwnershipCanBeSwitched() {
        Owner oldOwner = testUtils.createOwner("old");

        Owner newOwner = testUtils.createOwner("new");

        Rat rat = testUtils.createRat("Switchy");

        // create ownership
        this.ownershipManager.assignOwnership(oldOwner.getId(), rat.getId());
        // Switch owner
        this.ownershipManager.transferOwnership(oldOwner, rat, newOwner);

        assertFalse(this.ratOwnershipService.ratOwnershipExists(oldOwner, rat));
        assertTrue(this.ratOwnershipService.ratOwnershipExists(newOwner, rat));
        assertEquals(rat.getId(), this.ratOwnershipService.getRatOwnership(newOwner, rat).getRatId());
        assertEquals("Switchy", ratService.getRatById(this.ratOwnershipService.getRatOwnership(newOwner, rat).getRatId()).getName());
    }

    @Test
    @Transactional
    void ratPersistsAfterOwnerDeleted() {
        Owner owner = testUtils.createOwner("ghost");
        Rat rat = testUtils.createRat("lefty");

        this.ratOwnershipService.newOwnerships(owner, List.of(rat));
        assertTrue(this.ratOwnershipService.ratOwnershipExists(owner, rat));

        // delete all ownerships and then delete owner
        this.ratOwnershipService.deleteOwnership(owner, rat);

        assertFalse(this.ratOwnershipService.ratOwnershipExists(owner, rat));
        assertThrows(IllegalArgumentException.class, () -> this.ratOwnershipService.getRatOwnership(owner, rat));

        this.ownerService.deleteOwner(owner);
        assertFalse(this.ownerService.ownerExists(owner));
        assertThrows(IllegalArgumentException.class, () -> this.ownerService.getOwnerById(owner.getId()));

        assertTrue(this.ratService.ratExists(rat.getId()));
        assertEquals("lefty", this.ratService.getRatById(rat.getId()).getName());
    }

    @Test
    @Transactional
    void ownerSeesAllOwnedRats() {
        Owner owner = testUtils.createOwner("ratcollector");

        Rat r1 = testUtils.createRat("Alpha");
        this.ratOwnershipService.newOwnership(owner, r1);
        assertTrue(this.ratOwnershipService.ratOwnershipExists(owner, r1));

        Rat r2 = testUtils.createRat("Beta");
        this.ratOwnershipService.newOwnership(owner, r2);
        assertTrue(this.ratOwnershipService.ratOwnershipExists(owner, r2));

        assertEquals(2, this.ratOwnershipService.getAllOwnershipsByOwner(owner).size());
    }

    @Test
    @Transactional
    void groupOwnershipDeleteTest() {
        Owner owner = testUtils.createOwner("owner");
        Rat rat1 = testUtils.createRat("rat1");
        Rat rat2 = testUtils.createRat("rat2");
        Rat rat3 = testUtils.createRat("rat3");
        Rat rat4 = testUtils.createRat("rat4");
        Rat rat5 = testUtils.createRat("rat5");

        this.ratOwnershipService.newOwnerships(owner, List.of(rat1, rat2, rat3, rat4, rat5));

        assertEquals(5, this.ratOwnershipService.getAllOwnershipsByOwner(owner).size());

        this.ownerService.deleteOwner(owner);

        assertFalse(this.ownerService.ownerExists(owner));
        assertThrows(IllegalArgumentException.class, () -> this.ownerService.getOwnerById(owner.getId()));

        for (Rat rat : List.of(rat1, rat2, rat3, rat4, rat5)) {
            assertTrue(this.ratService.ratExists(rat.getId()));
            assertThrows(IllegalArgumentException.class, () -> this.ratOwnershipService.getOwnerOfRat(rat));
            assertThrows(IllegalArgumentException.class, () -> this.ratOwnershipService.getRatOwnership(owner, rat));
        }
    }

    @Test
    @Transactional
    void uuidAssignmentTest() {
        Owner owner = testUtils.createOwner("owner");
        Rat rat1 = testUtils.createRat("rat1");
        RatOwnership ratOwnership = this.ratOwnershipService.newOwnership(owner, rat1);

        assertNotNull(owner.getId());
        assertNotNull(rat1.getId());
        assertNotNull(ratOwnership.getId());
    }

}
