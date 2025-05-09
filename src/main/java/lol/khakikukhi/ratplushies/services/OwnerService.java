//package lol.khakikukhi.ratplushies.services;
//
//import lol.khakikukhi.ratplushies.DTOs.OwnerDto;
//import lol.khakikukhi.ratplushies.entities.Owner;
//import lol.khakikukhi.ratplushies.repositories.OwnerRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.server.ResponseStatusException;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class OwnerService {
//
//    private final OwnerRepository ownerRepository;
//
//    public OwnerDto getOwnerDtoByID(String id) {
//        Owner owner = ownerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//        return
//    }
//
//    private OwnerDto mapToDto()
//
//}
