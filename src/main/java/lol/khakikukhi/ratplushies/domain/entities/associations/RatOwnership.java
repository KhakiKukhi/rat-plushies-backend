package lol.khakikukhi.ratplushies.domain.entities.associations;

import jakarta.persistence.*;
import lol.khakikukhi.ratplushies.domain.entities.Owner;
import lol.khakikukhi.ratplushies.domain.entities.Rat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"ownerId", "ratId"}))
@Data
@NoArgsConstructor
public class RatOwnership {
    public RatOwnership(String ownerId, String ratId) {
        this.ownerId = ownerId;
        this.ratId = ratId;
    }

    public RatOwnership(Owner owner, Rat rat) {
        this(owner.getId(), rat.getId());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    @Column(nullable = false, updatable = false)
    private String ownerId;
    @Column(nullable = false, updatable = false)
    private String ratId;

}
