package lol.khakikukhi.ratplushies.DTOs;

import lol.khakikukhi.ratplushies.constants.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResult {
    private boolean success;
    private UserRoles.Role role;
    private String userId;
    private String message;
}
