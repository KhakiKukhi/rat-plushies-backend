package lol.khakikukhi.ratplushies.application.services.entity;

import jakarta.transaction.Transactional;
import lol.khakikukhi.ratplushies.domain.entities.Owner;
import lol.khakikukhi.ratplushies.infrastructure.repositories.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class OwnerService {
    private final OwnerRepository ownerRepository;

    public void deleteOwner(String ownerId) {
        ownerRepository.deleteById(ownerId);
    }

    public boolean ownerExists(Owner owner) {
        return ownerRepository.existsById(owner.getId());
    }

    public Owner getOwnerById(String id) {
        return ownerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Owner doesn't exist."));
    }

    public Owner saveOwner(Owner owner) {
        return ownerRepository.save(owner);
    }

}
