package lol.khakikukhi.ratplushies.infrastructure.repositories;

import lol.khakikukhi.ratplushies.domain.entities.associations.RatRelationshipMembership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatRelationshipMembershipRepository extends JpaRepository<RatRelationshipMembership, Long> {
}
