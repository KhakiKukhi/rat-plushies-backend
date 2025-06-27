package lol.khakikukhi.ratplushies.presentation.controllers;

import jakarta.servlet.http.HttpSession;
import lol.khakikukhi.ratplushies.application.services.ProfilePicturesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("profile-pictures")
public class ProfilePictureController {

    private final ProfilePicturesService profilePicturesService;

    @PostMapping(path = "/owners/{id}/upload-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadProfilePictureOwner (@PathVariable String id, @RequestParam("file")MultipartFile file) {
        try {
            profilePicturesService.uploadProfilePictureOwner(id, file);
            return ResponseEntity.ok("Profile picture uploaded!");
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File processing error: " + e.getMessage());
        }
    }

    @PostMapping(path = "/rats/{ratId}/upload-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadProfilePictureRat (@PathVariable String ratId, @RequestParam("file")MultipartFile file, HttpSession session) {
        try {

            String userId  = session.getAttribute("userId").toString();
            profilePicturesService.uploadProfilePictureRat(userId, ratId, file);
            return ResponseEntity.ok("Profile picture uploaded!");


        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File processing error: " + e.getMessage());
        }
    }
}
