package lol.khakikukhi.ratplushies.DTOs.mappers;

import lol.khakikukhi.ratplushies.DTOs.OwnerDto;
import lol.khakikukhi.ratplushies.DTOs.RatDto;
import lol.khakikukhi.ratplushies.entities.Owner;
import lol.khakikukhi.ratplushies.entities.Rat;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OwnerMapper {
    @Mapping(target = "ratNames", ignore = true)
    OwnerDto toDto(Owner owner);

    @Mapping(source = "owner.ownerId", target = "ownerId")
    RatDto toDto(Rat rat);

    List<RatDto> toRatDtos(List<Rat> rats);

    @AfterMapping
    default void mapRatNames(Owner owner, @MappingTarget OwnerDto dto) {
        if (owner.getRats() != null) {
            dto.setRatNames(owner.getRats().stream()
                    .map(Rat::getName)
                    .toList());
        }
    }
}
