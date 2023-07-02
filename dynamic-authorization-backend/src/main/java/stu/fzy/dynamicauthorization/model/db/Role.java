package stu.fzy.dynamicauthorization.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

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
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @ManyToMany(
            mappedBy = "roles",
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private Set<User> users;
    @ManyToMany(
            mappedBy = "roles",
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private Set<Resource> resources;
}
