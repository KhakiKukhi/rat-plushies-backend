package lol.khakikukhi.ratplushies.services;

import jakarta.persistence.EntityNotFoundException;
import lol.khakikukhi.ratplushies.DTOs.AuthResult;
import lol.khakikukhi.ratplushies.DTOs.LoginRequest;
import lol.khakikukhi.ratplushies.entities.Owner;
import lol.khakikukhi.ratplushies.repositories.OwnerRepository;
import lol.khakikukhi.ratplushies.constants.UserRoles;
import lol.khakikukhi.ratplushies.utilities.PasswordHasher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final OwnerRepository ownerRepository;
    private final PasswordHasher passwordHasher;

    public  AuthResult authenticate(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        Owner owner = ownerRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("Owner not found!"));


        return new AuthResult(false, UserRoles.Role.USER, null, ""); // TODO: placeholder
    }

}
