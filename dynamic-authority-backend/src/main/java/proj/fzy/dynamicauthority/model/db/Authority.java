package proj.fzy.dynamicauthority.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "authority")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique = true)
    private String name;
    @JsonIgnore
    @ManyToMany(
            mappedBy = "authorities",
            fetch = FetchType.LAZY
    )
    private Set<User> users;
    @JsonIgnore
    @ManyToMany(
            mappedBy = "authorities",
            fetch = FetchType.LAZY
    )
    private Set<Role> roles;
    @JsonIgnore
    @ManyToMany(
            mappedBy = "authorities",
            fetch = FetchType.LAZY
    )
    private Set<Resource> resources;
}
