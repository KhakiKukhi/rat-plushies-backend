package lol.khakikukhi.ratplushies.infrastructure.repositories;

import lol.khakikukhi.ratplushies.domain.entities.associations.RatRelationshipGroup;
import lol.khakikukhi.ratplushies.domain.entities.associations.RatRelationshipGroupType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RatRelationshipGroupRepository extends JpaRepository<RatRelationshipGroup, Long> {
    Optional<RatRelationshipGroup> findRatRelationshipGroupByGroupId(Long groupId);
    Optional<RatRelationshipGroup> findRatRelationshipGroupsByType(RatRelationshipGroupType type);
}
