package lol.khakikukhi.ratplushies.application.services.entity;

import jakarta.transaction.Transactional;
import lol.khakikukhi.ratplushies.domain.entities.Owner;
import lol.khakikukhi.ratplushies.domain.entities.Rat;
import lol.khakikukhi.ratplushies.domain.entities.associations.RatOwnership;
import lol.khakikukhi.ratplushies.infrastructure.repositories.RatOwnershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RatOwnershipService {

    private final RatOwnershipRepository ratOwnershipRepository;

    @Transactional
    public void deleteOwnership(String ownerId, String ratId) {
        ratOwnershipRepository.deleteByOwnerIdAndRatId(ownerId, ratId);
    }

    public RatOwnership getOwnershipByRat(Rat rat) {
        return this.getOwnershipByRatId( rat.getId());
    }

    public RatOwnership getOwnershipByRatId(String ratId) {
        return ratOwnershipRepository.findByRatId(ratId)
                .orElseThrow(() -> new IllegalArgumentException("Rat " + ratId + " is not owned"));
    }

    public void delete(RatOwnership ratOwnership) {
        ratOwnershipRepository.delete(ratOwnership);
    }

    public boolean ratOwnershipExists(String ownerId, String ratId) {
        return ratOwnershipRepository.existsByOwnerIdAndRatId(ownerId, ratId);
    }

    public boolean ratIsOwned(String ratId) {
        return this.ratOwnershipRepository.existsByRatId(ratId);
    }

    public List<RatOwnership> getAllOwnershipsByOwner(String ownerId) {
        return ratOwnershipRepository.findAllByOwnerId(ownerId);
    }

    public RatOwnership saveOwnership(RatOwnership ratOwnership) {
        return ratOwnershipRepository.save(ratOwnership);
    }

}
