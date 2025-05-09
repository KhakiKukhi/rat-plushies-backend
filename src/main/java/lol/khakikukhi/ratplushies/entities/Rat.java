package lol.khakikukhi.ratplushies.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@ToString(exclude = {"owner", "partners", "parents", "children"})
public class Rat {
    @PrePersist
    public void generateId() {
        if (this.ratId == null) {
            this.ratId = "RAT_" + UUID.randomUUID().toString().replace("-", "");
        }
    }

    @Id
    @Column(length = 36, nullable = false, updatable = false)
    private String ratId;

    @Column(nullable = false, updatable = true)
    private String name; // TODO: Should this be unique?

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            foreignKey = @ForeignKey(name = "fk_rat_owner", foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES owner(\"ownerId\") ON DELETE SET NULL"
            )
    )
    @Setter(AccessLevel.NONE)
    private Owner owner;

    @ManyToMany
    @JoinTable(
            name = "rat_partners",
            joinColumns = @JoinColumn(name = "rat_id"),
            inverseJoinColumns = @JoinColumn(name = "partner_id")
    )
    private List<Rat> partners = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "rat_parents",
            joinColumns = @JoinColumn(name = "child_id"),
            inverseJoinColumns = @JoinColumn(name = "parent_id")
    )
    private List<Rat> parents = new ArrayList<>();

    @ManyToMany(mappedBy = "parents")
    private List<Rat> children = new ArrayList<>();


    public void setOwner(Owner newOwner) {
        if (this.owner != null) {
            this.owner.internalRemoveRat(this); // remove from old owner
        }

        this.owner = newOwner;

        if (newOwner != null && !newOwner.getRatsInternal().contains(this)) {
            newOwner.internalAddRat(this); // add to new owner
        }
    }

    public void addPartner(Rat partner) {
        if (partner == null || partner == this || this.partners.contains(partner)) {
            return;
        }
        this.partners.add(partner);
        partner.internalGetPartners().add(this);
    }

    public void removePartner(Rat partner) {
        if (partner == null || partner == this || !this.partners.contains(partner)) {
            return;
        }
        this.partners.remove(partner);
        partner.internalGetPartners().remove(this);
    }

    public List<Rat> getPartners() {
        return List.copyOf(partners);
    }

    List<Rat> internalGetPartners() {
        return partners;
    }

    public void addParent(Rat parent) {
        if (parent == null || parent == this || this.parents.contains(parent)) {
            return;
        }
        this.parents.add(parent);
        parent.internalGetChildren().add(this);
    }

    public void removeParent(Rat parent) {
        if (parent == null || parent == this || !this.parents.contains(parent)) {
            return;
        }
        this.parents.remove(parent);
        parent.internalGetChildren().remove(this);
    }

    public List<Rat> getParents() {
        return List.copyOf(parents);
    }

    List<Rat> internalGetParents() {
        return parents;
    }

    public void addChild(Rat child) {
        if (child == null || child == this || this.children.contains(child)) {
            return;
        }
        this.children.add(child);
        child.internalGetParents().add(this);
    }

    public void removeChild(Rat child) {
        if (child == null || child == this || !this.children.contains(child)) {
            return;
        }
        this.children.remove(child);
        child.getParents().remove(this);
    }

    public List<Rat> getChildren() {
        return List.copyOf(children);
    }

    List<Rat> internalGetChildren() {
        return children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rat rat)) return false;
        return ratId != null && ratId.equals(rat.getRatId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
