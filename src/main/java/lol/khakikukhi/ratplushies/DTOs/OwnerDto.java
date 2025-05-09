package lol.khakikukhi.ratplushies.DTOs;

import lombok.Data;

import java.util.List;

@Data
public class OwnerDto {
    private String ownerId;
    private String username;
    private String emailAddress;
    private String bio;
    private String birthday;
    private List<String> ratNames;
}
