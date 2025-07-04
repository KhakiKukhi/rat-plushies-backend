package lol.khakikukhi.ratplushies.application.managers;

import lol.khakikukhi.ratplushies.application.services.entity.OwnerService;
import lol.khakikukhi.ratplushies.application.services.entity.RatService;
import lol.khakikukhi.ratplushies.domain.entities.Owner;
import lol.khakikukhi.ratplushies.domain.entities.Rat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatManager {

    private final RatService ratService;

    public boolean verifyRatIsValidAndActive(String ratId) {
        Rat rat = ratService.getRatById(ratId);
        if (rat.isEnabled()) {
            return true;
        } else {
            return false;
        }
    }

    public void updateProfilePicture(String ratId, String filename) {
        Rat rat = ratService.getRatById(ratId);
        rat.setProfilePicture(filename);
        ratService.saveRat(rat);
    }

}
