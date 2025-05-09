package lol.khakikukhi.ratplushies.repositories;

import lol.khakikukhi.ratplushies.entities.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<Owner, String> {
}
