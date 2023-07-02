package stu.fzy.dynamicauthorization.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.http.HttpMethod;
import stu.fzy.dynamicauthorization.enums.ResourceAuthType;

import java.util.Set;

@Entity
@Table(
        name = "resource",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"method", "path"})}
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String method;
    @Column(nullable = false)
    private String path;
    @Column(name = "auth-type", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    @Builder.Default
    private ResourceAuthType authType = ResourceAuthType.JUST_AUTHENTICATION;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "resource_role",
            joinColumns = {@JoinColumn(name = "resource_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    private Set<Role> roles;
}
