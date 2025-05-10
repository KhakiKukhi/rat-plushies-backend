package lol.khakikukhi.ratplushies.services;

import lol.khakikukhi.ratplushies.DTOs.OwnerDto;
import lol.khakikukhi.ratplushies.entities.Owner;
import lol.khakikukhi.ratplushies.repositories.OwnerRepository;
import lol.khakikukhi.ratplushies.DTOs.mappers.OwnerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

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
