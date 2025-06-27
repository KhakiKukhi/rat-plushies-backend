package lol.khakikukhi.ratplushies.application.services;

import lol.khakikukhi.ratplushies.presentation.controllers.DTOs.OwnerDto;
import lol.khakikukhi.ratplushies.domain.entities.Owner;
import lol.khakikukhi.ratplushies.infrastructure.repositories.OwnerRepository;
import lol.khakikukhi.ratplushies.presentation.controllers.DTOs.mappers.OwnerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;

    public OwnerDto getOwnerDtoByID(String id) {
        Owner owner = ownerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ownerMapper.mapToDto(owner);
    }

    public OwnerDto getOwnerDtoByUsernameAndPassword(String username, String password) {
        return null;
    }
}
