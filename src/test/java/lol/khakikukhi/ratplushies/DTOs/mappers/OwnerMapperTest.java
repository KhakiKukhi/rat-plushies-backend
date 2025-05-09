package lol.khakikukhi.ratplushies.DTOs.mappers;

import lol.khakikukhi.ratplushies.DTOs.OwnerDto;
import lol.khakikukhi.ratplushies.DTOs.RatDto;
import lol.khakikukhi.ratplushies.entities.Owner;
import lol.khakikukhi.ratplushies.entities.Rat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.MapperConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@MapperConfig(componentModel = "spring")
@SpringBootTest(classes = {OwnerMapperImpl.class}) // generated class
public class OwnerMapperTest {

    @Autowired
    private OwnerMapper ownerMapper;

    @Test
    void shouldMapOwnerToOwnerDto() {
        Owner owner = new Owner();
        owner.setOwnerId("USR123");
        owner.setUsername("khaki");

        Rat rat1 = new Rat();
        rat1.setRatId("RAT1");
        rat1.setName("Cheddar");

        Rat rat2 = new Rat();
        rat2.setRatId("RAT2");
        rat2.setName("Brie");

        owner.addRat(rat1);
        owner.addRat(rat2);

        OwnerDto dto = ownerMapper.toDto(owner);

        assertEquals("USR123", dto.getOwnerId ());
        assertEquals("khaki", dto.getUsername());
        assertEquals(List.of("Cheddar", "Brie"), dto.getRatNames());
    }

    @Test
    void shouldMapRatToRatDto() {
        Owner owner = new Owner();
        owner.setOwnerId("USR456");

        Rat rat = new Rat();
        rat.setRatId("RAT007");
        rat.setName("Sniffles");
        rat.setOwner(owner);

        RatDto dto = ownerMapper.toDto(rat);

        assertEquals("RAT007", dto.getRatId());
        assertEquals("Sniffles", dto.getName());
        assertEquals("USR456", dto.getOwnerId());
    }

    @Test
    void shouldMapRatListToDtoList() {
        Rat r1 = new Rat();
        r1.setRatId("RAT1");
        r1.setName("Nibble");

        Rat r2 = new Rat();
        r2.setRatId("RAT2");
        r2.setName("Scurry");

        List<RatDto> dtos = ownerMapper.toRatDtos(List.of(r1, r2));

        assertEquals(2, dtos.size());
        assertEquals("Nibble", dtos.get(0).getName());
        assertEquals("Scurry", dtos.get(1).getName());
    }
}

