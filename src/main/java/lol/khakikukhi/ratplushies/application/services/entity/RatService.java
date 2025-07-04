package lol.khakikukhi.ratplushies.application.services.entity;

import lol.khakikukhi.ratplushies.domain.entities.Rat;
import lol.khakikukhi.ratplushies.infrastructure.repositories.RatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class RatService {
    private final RatRepository ratRepository;

    public boolean ratExists(String id) {
        return ratRepository.existsById(id);
    }

    public Rat getRatById(String id) {
        return ratRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rat " + id + " not found"));
    }

    public Rat saveRat(Rat rat) {
        return ratRepository.save(rat);
    }

}
