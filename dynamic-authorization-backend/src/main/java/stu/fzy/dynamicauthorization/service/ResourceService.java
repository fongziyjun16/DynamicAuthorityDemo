package stu.fzy.dynamicauthorization.service;

import org.springframework.stereotype.Service;
import stu.fzy.dynamicauthorization.enums.ResourceAuthType;
import stu.fzy.dynamicauthorization.model.db.Resource;
import stu.fzy.dynamicauthorization.model.db.Role;
import stu.fzy.dynamicauthorization.repository.ResourceRepository;
import stu.fzy.dynamicauthorization.repository.RoleRepository;

import java.util.*;

@Service
public class ResourceService {

    private final RoleRepository roleRepository;
    private final ResourceRepository resourceRepository;

    public ResourceService(RoleRepository roleRepository, ResourceRepository resourceRepository) {
        this.roleRepository = roleRepository;
        this.resourceRepository = resourceRepository;
    }

    public boolean create(String method, String path, ResourceAuthType authType) {
        if (resourceRepository.findByMethodAndPath(method, path).isEmpty()) {
            resourceRepository.save(Resource.builder()
                    .method(method)
                    .path(path)
                    .authType(authType)
                    .build());
            resourceRepository.flush();
            return true;
        }
        return false;
    }

    public boolean updateAuthType(String method, String path, ResourceAuthType newAuthType) {
        Optional<Resource> optionalResource = resourceRepository.findByMethodAndPath(method, path);
        if (optionalResource.isPresent()) {
            Resource resource = optionalResource.get();
            resource.setAuthType(newAuthType);
            resourceRepository.save(resource);
            return true;
        }
        return false;
    }

    public Map<String, Set<String>> getDBEndpoints() {
        List<Resource> dbResources = resourceRepository.findAll();
        if (dbResources.size() == 0) {
            return null;
        }
        Map<String, Set<String>> mapping = new HashMap<>();
        dbResources.forEach(dbResource -> {
            String method = dbResource.getMethod();
            if (!mapping.containsKey(method)) {
                mapping.put(method, new HashSet<>());
            }
            mapping.get(method).add(dbResource.getPath());
        });
        return mapping;
    }

    public Resource get(String method, String path) {
        return resourceRepository.findByMethodAndPath(method, path).orElse(null);
    }

    public List<Resource> getAll() {
        return resourceRepository.findAll();
    }

    public Resource getResource(String method, String path) {
        return resourceRepository.findByMethodAndPath(method, path).orElse(null);
    }

    public boolean delete(String method, String path) {
        Optional<Resource> optionalResource = resourceRepository.findByMethodAndPath(method, path);
        if (optionalResource.isPresent()) {
            resourceRepository.delete(optionalResource.get());
            return true;
        }
        return false;
    }

    public boolean assignRole(String method, String path, String authType, List<String> roleNames) {
        Optional<Resource> optionalResource = resourceRepository.findByMethodAndPath(method, path);
        if (optionalResource.isEmpty()) {
            return false;
        }
        List<Role> roles = new ArrayList<>();
        for (String roleName : roleNames) {
            Optional<Role> optionalRole = roleRepository.findByName(roleName);
            if (optionalRole.isEmpty()) {
                return false;
            }
            roles.add(optionalRole.get());
        }
        Resource resource = optionalResource.get();
        if (roles.size() == 0) {
            resource.getRoles().clear();
        } else {
            for (Role role : roles) {
                resource.getRoles().add(role);
            }
        }
        resource.setAuthType(ResourceAuthType.valueOf(authType));
        resourceRepository.save(resource);
        return true;
    }

    public boolean clearRoles(String method, String path) {
        Optional<Resource> optionalResource = resourceRepository.findByMethodAndPath(method, path);
        if (optionalResource.isPresent()) {
            Resource resource = optionalResource.get();
            resource.getRoles().clear();
            resourceRepository.save(resource);
            return true;
        }
        return false;
    }

    public Set<Role> getRoles(String method, String path) {
        Optional<Resource> optionalResource = resourceRepository.findByMethodAndPath(method, path);
        if (optionalResource.isEmpty()) {
            return new HashSet<>();
        }
        return optionalResource.get().getRoles();
    }

}
