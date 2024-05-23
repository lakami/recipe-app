package dev.pjatk.recipeapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;

/**
 * EqualsAndHashCode should not be used widely for entities. We use it here because it is simple record-like entity.
 * @see <a href="https://thorben-janssen.com/ultimate-guide-to-implementing-equals-and-hashcode-with-hibernate/">About equals() and hashCode()</a>
 */

@Entity
@Table(name = "authority")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Authority implements Serializable, Persistable<String> {

    @Id
    @NotNull
    @Size(max = 50)
    @Getter
    @Setter
    @EqualsAndHashCode.Include
    @Column(length = 50)
    private String name;

    @Transient
    private boolean isPersisted;

    @Override
    public String getId() {
        return name;
    }

    @Override
    @Transient
    public boolean isNew() {
        return !isPersisted;
    }

    public void setIsPersisted() {
        isPersisted = true;
    }

    @PostLoad
    @PostPersist
    public void updateEntityState() {
        this.setIsPersisted();
    }

}
