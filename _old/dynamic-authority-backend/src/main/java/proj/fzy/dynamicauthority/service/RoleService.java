package proj.fzy.dynamicauthority.service;

import org.springframework.stereotype.Service;
import proj.fzy.dynamicauthority.model.db.Authority;
import proj.fzy.dynamicauthority.model.db.Role;
import proj.fzy.dynamicauthority.repository.AuthorityRepository;
import proj.fzy.dynamicauthority.repository.RoleRepository;

import java.util.*;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;

    public RoleService(RoleRepository roleRepository, AuthorityRepository authorityRepository) {
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
    }

    public boolean createNewRole(String name) {
        if (roleRepository.findByName(name).isEmpty()) {
            roleRepository.save(Role.builder().name(name).build());
            return true;
        }
        return false;
    }

    public boolean deleteRole(String name) {
        Optional<Role> optionalRole = roleRepository.findByName(name);
        if (optionalRole.isPresent()) {
            roleRepository.delete(optionalRole.get());
            return true;
        }
        return false;
    }

    public Role getByName(String name) {
        return roleRepository.findByName(name).orElse(null);
    }

    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    public Set<Authority> getAuthorities(String roleName) {
        Optional<Role> optionalRole = roleRepository.findByName(roleName);
        return optionalRole.isPresent() ?
                optionalRole.get().getAuthorities() :
                new HashSet<>();
    }

    public boolean addAuthority(String roleName, String authorityName) {
        Optional<Role> optionalRole = roleRepository.findByName(roleName);
        Optional<Authority> optionalAuthority = authorityRepository.findByName(authorityName);
        if (optionalRole.isPresent() && optionalAuthority.isPresent()) {
            Role dbRole = optionalRole.get();
            Authority dbAuthority = optionalAuthority.get();
            if (!dbRole.getAuthorities().contains(dbAuthority)) {
                dbRole.getAuthorities().add(dbAuthority);
                roleRepository.save(dbRole);
                return true;
            }
        }
        return false;
    }

    public boolean removeAuthority(String roleName, String authorityName) {
        Optional<Role> optionalRole = roleRepository.findByName(roleName);
        Optional<Authority> optionalAuthority = authorityRepository.findByName(authorityName);
        if (optionalRole.isPresent() && optionalAuthority.isPresent()) {
            Role dbRole = optionalRole.get();
            Authority dbAuthority = optionalAuthority.get();
            if (dbRole.getAuthorities().contains(dbAuthority)) {
                optionalRole.get().getAuthorities().remove(dbAuthority);
                roleRepository.save(dbRole);
                return true;
            }
        }
        return false;
    }
}
