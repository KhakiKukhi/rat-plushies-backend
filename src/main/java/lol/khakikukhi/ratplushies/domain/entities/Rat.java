package lol.khakikukhi.ratplushies.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rat{
    @PrePersist
    public void generateId() {
        if (this.id == null) {
            this.id = "RAT_" + UUID.randomUUID().toString().replace("-", "");
        }
    }

    // TODO: create custom constructor and include creation timestamp
    // TODO: set maximum limits on publicly-defined fields such as bios and names.

    @Id
    @Column(length = 36, nullable = false, updatable = false)
    @EqualsAndHashCode.Include
    private String id;

    @Column(nullable = false, updatable = true, unique = true)
    private String name;

    @Builder.Default
    @Column(nullable = false, updatable = true)
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    @Builder.Default
    @Column(nullable = false, updatable = true)
    private Timestamp birthday = new Timestamp(System.currentTimeMillis());

    @Column(nullable = true, updatable = true)
    private String profilePicture;

    @Column(length = 4000, nullable = true, updatable = true)
    private String bio;

    @Builder.Default
    @Column(nullable = false, updatable = true)
    private boolean enabled = true;
}
