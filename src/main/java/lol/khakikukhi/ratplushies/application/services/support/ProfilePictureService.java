package lol.khakikukhi.ratplushies.application.services.support;

import lol.khakikukhi.ratplushies.application.managers.OwnerManager;
import lol.khakikukhi.ratplushies.application.managers.OwnershipManager;
import lol.khakikukhi.ratplushies.application.managers.RatManager;
import lol.khakikukhi.ratplushies.infrastructure.storage.FileStorage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ProfilePictureService {

    private final OwnershipManager ownershipManager;
    private final OwnerManager ownerManager;
    private final RatManager ratManager;

    private final FileStorage fileStorage;

    public ProfilePictureService(OwnershipManager ownershipManager, OwnerManager ownerManager, RatManager ratManager, @Qualifier("localImageStorage") FileStorage fileStorage) {
        this.ownershipManager = ownershipManager;
        this.ownerManager = ownerManager;
        this.ratManager = ratManager;
        this.fileStorage = fileStorage;
    }

    // TODO: multiple images with rats support.
    // TODO: limit filesize instead of resolution
    // TODO: limit by x number of images or filesize
    // TODO: limit by stooge space used instead of number of images

    public void uploadProfilePictureOwner(String userId, MultipartFile file) throws IOException {
        if (ownerManager.verifyOwnerIsValidAndActive(userId)) {
            String filename = uploadProfilePicture(file, userId);
            ownerManager.updateProfilePicture(userId, filename);
        }
    }

    public void uploadProfilePictureRat(String userId, String ratId, MultipartFile file) throws IOException {
        if(ownerManager.verifyOwnerIsValidAndActive(userId) &&
                ratManager.verifyRatIsValidAndActive(ratId) &&
                ownershipManager.verifyOwnership(userId, ratId)) {
            String filename = uploadProfilePicture(file, ratId);
            ratManager.updateProfilePicture(ratId, filename);
        } else {
            throw new IllegalArgumentException("Rat not owned by this owner!");
        }
    }

    private String uploadProfilePicture(MultipartFile file, String entityId) throws IOException {
        byte[] imageData = fileStorage.validateAndRead(file);
        String filename = entityId + fileStorage.getValidExtension(file.getOriginalFilename());
        fileStorage.store(filename, imageData);
        return filename;
    }

}
