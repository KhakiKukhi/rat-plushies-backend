package lol.khakikukhi.ratplushies.entities;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AdditionalEntityTest {

    @Test
    void testOwnerAddRatDuplicate() {
        Owner owner = new Owner();
        owner.setUsername("testUser");
        owner.setPasswordHash("pass");

        Rat rat = new Rat();
        rat.setName("Ratty");

        // Add the same rat twice
        owner.addRat(rat);
        owner.addRat(rat);

        // The rat should only appear once in the collection
        assertEquals(1, owner.getRats().size(), "Duplicate rat should not be added");
    }

    @Test
    void testOwnerRemoveRatNotPresent() {
        Owner owner = new Owner();
        owner.setUsername("testUser");
        owner.setPasswordHash("pass");

        Rat rat = new Rat();
        rat.setName("Ratty");

        // Removing a rat that was never added should not fail
        owner.removeRat(rat);
        assertTrue(owner.getRats().isEmpty(), "Owner's rat list should remain empty");
    }

    @Test
    void testRatAddPartnerNullAndSelf() {
        Rat ratA = new Rat();
        ratA.setName("RatA");

        // Adding null partner should have no effect
        ratA.addPartner(null);
        assertTrue(ratA.getPartners().isEmpty(), "Null partner should not be added");

        // Adding self as partner should have no effect
        ratA.addPartner(ratA);
        assertTrue(ratA.getPartners().isEmpty(), "Self partner should not be added");
    }

    @Test
    void testRatRemovePartnerNotPresent() {
        Rat ratA = new Rat();
        ratA.setName("RatA");

        Rat ratB = new Rat();
        ratB.setName("RatB");

        // Removing a non-existent partner should have no effect
        ratA.removePartner(ratB);
        assertTrue(ratA.getPartners().isEmpty(), "Removing non-existent partner should leave list empty");
    }

    @Test
    void testRatAddParentWithNullAndSelf() {
        Rat rat = new Rat();
        rat.setName("RatA");

        // Adding null parent should do nothing
        rat.addParent(null);
        assertTrue(rat.getParents().isEmpty(), "Null parent should not be added");

        // Adding self as parent should do nothing
        rat.addParent(rat);
        assertTrue(rat.getParents().isEmpty(), "Self should not be added as parent");
    }

    @Test
    void testRatRemoveParentNotPresent() {
        Rat rat = new Rat();
        rat.setName("RatA");

        Rat parent = new Rat();
        parent.setName("ParentRat");

        // Removing a parent that wasn't added should not affect the list
        rat.removeParent(parent);
        assertTrue(rat.getParents().isEmpty(), "Removing non-existent parent should leave list empty");
    }

    @Test
    void testRatAddChildWithNullAndSelf() {
        Rat rat = new Rat();
        rat.setName("RatA");

        // Adding null child should do nothing
        rat.addChild(null);
        assertTrue(rat.getChildren().isEmpty(), "Null child should not be added");

        // Adding self as child should do nothing
        rat.addChild(rat);
        assertTrue(rat.getChildren().isEmpty(), "Self should not be added as child");
    }

    @Test
    void testRatRemoveChildNotPresent() {
        Rat rat = new Rat();
        rat.setName("RatA");

        Rat child = new Rat();
        child.setName("ChildRat");

        // Removing a child not present should not affect the list
        rat.removeChild(child);
        assertTrue(rat.getChildren().isEmpty(), "Removing non-existent child should leave list empty");
    }
}