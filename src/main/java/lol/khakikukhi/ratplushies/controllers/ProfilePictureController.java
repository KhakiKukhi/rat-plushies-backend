package lol.khakikukhi.ratplushies.controllers;

import lol.khakikukhi.ratplushies.services.ProfilePicturesService;
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
        catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body("Validation error: " + e.getMessage());
        }
    }

    @PostMapping(path = "/rats/{id}/upload-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadProfilePictureRat (@PathVariable String id, @RequestParam("file")MultipartFile file) {
        try {
            profilePicturesService.uploadProfilePictureRat(id, file);
            return ResponseEntity.ok("Profile picture uploaded!");
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File processing error: " + e.getMessage());
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body("Validation error: " + e.getMessage());
        }
    }
}
