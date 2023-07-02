package proj.fzy.dynamicauthority.model.db;

import jakarta.persistence.*;
import lombok.*;
import proj.fzy.dynamicauthority.enums.ResourceAuthorizationType;
import proj.fzy.dynamicauthority.enums.ResourcePermitAll;

import java.util.Set;

@Entity
@Table(
        name = "resource",
        uniqueConstraints = @UniqueConstraint(columnNames = {"method", "path"})
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String path;
    @Column(nullable = false)
    private String method;
    @Column(name = "permit_all", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    @Builder.Default
    private ResourcePermitAll permitAll = ResourcePermitAll.AUTH; // 0(default) - authentication(must need) & authority(may need), 1 - permit all
    @Column(name = "authorization_type", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    @Builder.Default
    private ResourceAuthorizationType authorizationType = ResourceAuthorizationType.NO_AUTH; // 0(default) - no need authorization, 1 - any roles and authorities, 2 - all roles and authorities. only work when permit_all is 0
    @ManyToMany
    @JoinTable(
            name = "resource_role",
            joinColumns = @JoinColumn(name = "resource_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles;
    @ManyToMany
    @JoinTable(
            name = "resource_authority",
            joinColumns = @JoinColumn(name = "resource_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id")
    )
    private Set<Authority> authorities;
}
