package stu.fzy.dynamicauthorization.service;

import org.springframework.stereotype.Service;
import stu.fzy.dynamicauthorization.model.db.Role;
import stu.fzy.dynamicauthorization.repository.ResourceRepository;
import stu.fzy.dynamicauthorization.repository.RoleRepository;
import stu.fzy.dynamicauthorization.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private final UserRepository userRepository;
    private final ResourceRepository resourceRepository;
    private final RoleRepository roleRepository;

    public RoleService(UserRepository userRepository, ResourceRepository resourceRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.resourceRepository = resourceRepository;
        this.roleRepository = roleRepository;
    }

    public boolean create(String name) {
        if (roleRepository.findByName(name).isEmpty()) {
            roleRepository.save(Role.builder()
                    .name(name)
                    .build());
            roleRepository.flush();
            return true;
        }
        return false;
    }

    public boolean delete(String name) {
        Optional<Role> optionalRole = roleRepository.findByName(name);
        if (optionalRole.isPresent()) {
            Role role = optionalRole.get();
            role.getUsers().forEach(user -> user.getRoles().remove(role));
            role.getResources().forEach(resource -> resource.getRoles().remove(role));
            roleRepository.delete(optionalRole.get());
            return true;
        }
        return false;
    }

    public Role get(String name) {
        return roleRepository.findByName(name).orElse(null);
    }

    public List<Role> getAll() {
        return roleRepository.findAll();
    }

}
