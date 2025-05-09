package lol.khakikukhi.ratplushies.entities;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "rats")
public class Owner{
    @PrePersist
    public void generateId() {
        if (this.id == null) {
            this.id = "USR_" + UUID.randomUUID().toString().replace("-", "");
        }
    }

    @Id
    @Column(length = 36, nullable = false, updatable = false)
    @EqualsAndHashCode.Include
    public String id;

    @Column(nullable = false, updatable = true)
    private String username;

    @Column(nullable = false, updatable = true)
    private String password;

    @Column(nullable = true, updatable = true)
    private String profilePicture;

    // Optional
    @Column(nullable = true, updatable = true)
    private String emailAddress;

    @Column(nullable = true, updatable = true)
    private Timestamp birthday;

    @Column(nullable = true, updatable = true)
    private String bio;

    @OneToMany(mappedBy = "owner", cascade = {}, orphanRemoval = false)
    private List<Rat> rats = new ArrayList<>();

    public void addRat(Rat rat) {
        if (!this.rats.contains(rat)) {
            this.rats.add(rat);
            rat.setOwner(this);
        }
    }

    public void removeRat(Rat rat) {
        if (this.rats.contains(rat)) {
            this.rats.remove(rat);
            rat.setOwner(null);
        }
    }

    public void clearAllRats() {
        for (Rat rat : new ArrayList<>(rats)) {
            removeRat(rat);
        }
    }

    public List<Rat> getRats() {
        return List.copyOf(rats);
    }

    void internalAddRat(Rat rat) {
        rats.add(rat);
    }

    void internalRemoveRat(Rat rat) {
        rats.remove(rat);
    }

    List<Rat> getRatsInternal() {
        return rats;
    }
}
