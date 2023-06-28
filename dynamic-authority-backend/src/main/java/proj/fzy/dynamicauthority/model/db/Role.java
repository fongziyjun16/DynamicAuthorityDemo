package proj.fzy.dynamicauthority.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "role")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique = true)
    private String name;
    @JsonIgnore
    @ManyToMany(
            mappedBy = "roles",
            fetch = FetchType.LAZY
    )
    private Set<User> users;
    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "role_authority",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id")
    )
    private Set<Authority> authorities;
    @JsonIgnore
    @ManyToMany(
            mappedBy = "roles",
            fetch = FetchType.LAZY
    )
    private Set<Resource> resources;
}
