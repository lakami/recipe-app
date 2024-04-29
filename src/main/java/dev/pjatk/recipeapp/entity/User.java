package dev.pjatk.recipeapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.NaturalId;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * We use {@link AuditedEntityBase} so that we know who and when created or modified this entity. Might be useful.
 */
@Entity
@Table(name = "\"user\"")
@Getter
@Setter
public class User extends AuditedEntityBase<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen")
    @SequenceGenerator(name = "seq_gen")
    private Long id;

    @NaturalId
    @NotNull
    @Email
    @Size(min = 5, max = 128)
    @Column(unique = true, nullable = false, length = 128)
    private String email;

    @JsonIgnore
    @NotNull
    @Size(min = 60, max = 60)
    @Column(name = "password_hash", length = 60, nullable = false)
    private String password;

    @Size(min = 1, max = 50)
    @Column(length = 50)
    private String firstName;

    @Size(min = 1, max = 50)
    @Column(length = 50)
    private String lastName;

    @NotNull
    @Size(min = 1, max = 255)
    @Column(unique = true, nullable = false)
    private String profileUrl;

    @Size(max = 255)
    @Column(name = "image_url")
    private String imageUrl;

    @NotNull
    @Column(nullable = false)
    private boolean activated = false;

    @JsonIgnore
    @Size(max = 20)
    @Column(name = "activation_token", length = 20)
    private String activationToken;

    @Column(name = "activation_date")
    private Instant activationDate = null;

    @JsonIgnore
    @Size(max = 20)
    @Column(name = "reset_token", length = 20)
    private String resetToken;

    @Column(name = "reset_date")
    private Instant resetDate = null;

    @ManyToMany
    @BatchSize(size = 10) // optimize Hibernate performance
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")}
    )
    private Set<Authority> authorities = new HashSet<>();

    /**
     * We treat email as a unique identifier for the user. It can be treated as businessKey according to the article below.
     * @see <a href="https://thorben-janssen.com/ultimate-guide-to-implementing-equals-and-hashcode-with-hibernate/">About equals() and hashCode()</a>
     * @return true if emails are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    /**
     * @see <a href="https://thorben-janssen.com/ultimate-guide-to-implementing-equals-and-hashcode-with-hibernate/">About equals() and hashCode()</a>
     * @see <a href="https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/">About equals() and hashCode() 2</a>
     * @return constant value
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
