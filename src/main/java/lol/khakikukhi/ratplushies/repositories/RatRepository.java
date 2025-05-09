package lol.khakikukhi.ratplushies.repositories;

import lol.khakikukhi.ratplushies.entities.Rat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatRepository extends JpaRepository<Rat, String> {
}
