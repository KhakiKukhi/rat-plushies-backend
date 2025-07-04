package lol.khakikukhi.ratplushies.utils;

import lol.khakikukhi.ratplushies.application.services.entity.OwnerService;
import lol.khakikukhi.ratplushies.application.services.entity.RatService;
import lol.khakikukhi.ratplushies.domain.entities.Owner;
import lol.khakikukhi.ratplushies.domain.entities.Rat;
import org.springframework.stereotype.Component;

@Component
public class TestUtils {

    private final OwnerService ownerService;
    private final RatService ratService;

    public TestUtils(OwnerService ownerService, RatService ratService) {
        this.ownerService = ownerService;
        this.ratService = ratService;
    }

    public Owner createOwner(String name) {
        return ownerService.newOwner(builder -> builder
                .username(name)
        );
    }

    public Rat createRat(String name) {
        return ratService.newRat(builder -> builder
                .name(name)
        );
    }
}
