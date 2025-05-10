package lol.khakikukhi.ratplushies.DTOs.mappers;

import lol.khakikukhi.ratplushies.DTOs.OwnerDto;
import lol.khakikukhi.ratplushies.entities.Owner;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OwnerMapper {
    public OwnerDto mapToDto(Owner owner) {
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setOwnerId(owner.getId());
        ownerDto.setUsername(owner.getUsername());
        ownerDto.setProfilePicture(owner.getProfilePicture());
        ownerDto.setEmailAddress(owner.getEmailAddress());
        ownerDto.setBio(owner.getBio());
        ownerDto.setBirthday(
                owner.getBirthday() != null
                        ? owner.getBirthday().toInstant().toString()
                        : null
        );
        ownerDto.setRatNames(
                owner.getRats().stream()
                        .map(rat -> rat.getName())
                        .collect(Collectors.toList())
        );
        return ownerDto;
    }
}
