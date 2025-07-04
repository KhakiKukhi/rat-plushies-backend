package lol.khakikukhi.ratplushies.application.managers;

import jakarta.transaction.Transactional;
import lol.khakikukhi.ratplushies.application.services.entity.OwnerService;
import lol.khakikukhi.ratplushies.application.services.entity.RatOwnershipService;
import lol.khakikukhi.ratplushies.application.services.entity.RatService;
import lol.khakikukhi.ratplushies.domain.entities.Owner;
import lol.khakikukhi.ratplushies.domain.entities.Rat;
import lol.khakikukhi.ratplushies.domain.entities.associations.RatOwnership;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OwnershipManager {

    private final RatOwnershipService ratOwnershipService;
    private final OwnerService ownerService;
    private final RatService ratService;

    @Transactional
    public void transferOwnership(String oldOwnerId, String ratId, String newOwnerId) {
        ratOwnershipService.deleteOwnership(oldOwnerId, ratId);
        RatOwnership newRatOwnership = new RatOwnership(newOwnerId, ratId);
        ratOwnershipService.saveOwnership(newRatOwnership);
    }

    public Owner searchOwnerOfRat(String ratId) {
        RatOwnership ratOwnership = ratOwnershipService.getOwnershipByRatId(ratId);
        return ownerService.getOwnerById(ratOwnership.getOwnerId());
    }

    @Transactional
    public void removeAllOwnershipsByOwner(String ownerId) {
        for (RatOwnership ratOwnership : ratOwnershipService.getAllOwnershipsByOwner(ownerId)) {
            ratOwnershipService.delete(ratOwnership);
        }
    }

    public RatOwnership assignOwnership(String ownerId, String ratId) {
        if (ratOwnershipService.ratIsOwned(ratId)) {
            throw new IllegalArgumentException("Rat " + ratId + " is already owned");
        } else {
            return ratOwnershipService.saveOwnership(new RatOwnership(ownerId, ratId));
        }
    }

    @Transactional
    public void assignOwnerships(String ownerId, List<String> ratIds) {
        for (String ratId : ratIds) {
            assignOwnership(ownerId, ratId);
        }
    }

    public boolean verifyOwnership(String ownerId, String ratId) {
        Owner owner = ownerService.getOwnerById(ownerId);
        Rat rat = ratService.getRatById(ratId);
        return owner.isEnabled() && rat.isEnabled() && ratOwnershipService.ratOwnershipExists(ownerId, ratId);
    }

}
