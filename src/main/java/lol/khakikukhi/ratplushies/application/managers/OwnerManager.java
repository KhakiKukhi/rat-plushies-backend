package lol.khakikukhi.ratplushies.application.managers;

import jakarta.transaction.Transactional;
import lol.khakikukhi.ratplushies.application.services.entity.OwnerService;
import lol.khakikukhi.ratplushies.application.services.entity.RatOwnershipService;
import lol.khakikukhi.ratplushies.domain.entities.Owner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OwnerManager {

    private final OwnerService ownerService;
    private final OwnershipManager ownershipManager;

    @Transactional
    public void removeOwner(String ownerId) {
        ownershipManager.removeAllOwnershipsByOwner(ownerId);
        ownerService.deleteOwner(ownerId);
    }

    public boolean verifyOwnerIsValidAndActive(String ownerId) {
        Owner owner = ownerService.getOwnerById(ownerId);
        if (owner.isEnabled()) {
            return true;
        } else {
            return false;
        }
    }

    public void updateProfilePicture(String ownerId, String filename) {
        Owner owner = ownerService.getOwnerById(ownerId);
        owner.setProfilePicture(filename);
        ownerService.saveOwner(owner);
    }

}
