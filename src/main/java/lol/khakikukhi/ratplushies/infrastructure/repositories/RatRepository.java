package lol.khakikukhi.ratplushies.infrastructure.repositories;

import lol.khakikukhi.ratplushies.domain.entities.Rat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatRepository extends JpaRepository<Rat, String> {
}
