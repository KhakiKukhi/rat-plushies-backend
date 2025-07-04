package lol.khakikukhi.ratplushies.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Owner{
    @PrePersist
    public void generateId() {
        if (this.id == null) {
            this.id = "USR_" + UUID.randomUUID().toString().replace("-", "");
        }
    }

    // TODO: create custom constructor and include creation timestamp

    @Id
    @Column(length = 36, nullable = false, updatable = false)
    @EqualsAndHashCode.Include
    private String id;

    @Column(unique = true, nullable = false, updatable = true)
    private String username;

    @Column(nullable = false, updatable = true)
    private String passwordHash;

    @Builder.Default
    @Column(nullable = false, updatable = false)
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    @Column(nullable = true, updatable = true)
    private String profilePicture;

    // Optional
    @Column(nullable = true, updatable = true)
    private String emailAddress;

    @Builder.Default
    @Column(nullable = true, updatable = true)
    private Timestamp birthday = new Timestamp(System.currentTimeMillis());

    @Column(nullable = true, updatable = true)
    private String bio;

    @Builder.Default
    @Column(nullable = false, updatable = true)
    private boolean admin = false;

    @Builder.Default
    @Column(nullable = false, updatable = true)
    private boolean enabled = true;
}
