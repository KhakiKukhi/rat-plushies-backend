package lol.khakikukhi.ratplushies.domain.entities.associations;

import jakarta.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"participantId", "groupId"}))
public class RatRelationshipMembership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long membershipId;

    @Enumerated(EnumType.STRING)
    private RatRelationshipMembershipRole role;

    private String groupId;
    private String participantId;
}
