package lol.khakikukhi.ratplushies.infrastructure.repositories;

import lol.khakikukhi.ratplushies.domain.entities.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, String> {
    void deleteById(String id);
}
