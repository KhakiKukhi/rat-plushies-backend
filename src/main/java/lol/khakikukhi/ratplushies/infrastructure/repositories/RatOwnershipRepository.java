package lol.khakikukhi.ratplushies.infrastructure.repositories;

import lol.khakikukhi.ratplushies.domain.entities.associations.RatOwnership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RatOwnershipRepository extends JpaRepository<RatOwnership, Long> {
    List<RatOwnership> findAllByOwnerId(String ownerId);
    Optional<RatOwnership> findByOwnerIdAndRatId(String ownerId, String ratId);
    boolean existsByOwnerIdAndRatId(String ownerId, String ratId);
    void deleteByOwnerIdAndRatId(String ownerId, String ratId);
    boolean existsByRatId(String ratId);
    Optional<RatOwnership> findByRatId(String ratId);
}
