package lol.khakikukhi.ratplushies.domain.entities.associations;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class RatRelationshipGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, updatable = false)
    private Long groupId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = true)
    private RatRelationshipGroupType type;
}
