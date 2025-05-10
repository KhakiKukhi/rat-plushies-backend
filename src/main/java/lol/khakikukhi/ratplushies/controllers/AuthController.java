package lol.khakikukhi.ratplushies.controllers;

import jakarta.servlet.http.HttpSession;
import lol.khakikukhi.ratplushies.DTOs.AuthResult;
import lol.khakikukhi.ratplushies.DTOs.LoginRequest;
import lol.khakikukhi.ratplushies.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        AuthResult authResult = authService.authenticate(loginRequest);

        if (authResult.isSuccess()) {
            session.setAttribute("userId", authResult.getUserId());
            session.setAttribute("userRole", authResult.getRole());
            return ResponseEntity.ok("Login successful!");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authResult.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logout successful!");
    }

}
