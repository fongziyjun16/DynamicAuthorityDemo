package proj.fzy.dynamicauthority.service;

import org.springframework.stereotype.Service;
import proj.fzy.dynamicauthority.enums.ResourceAuthorizationType;
import proj.fzy.dynamicauthority.model.db.Authority;
import proj.fzy.dynamicauthority.model.db.Resource;
import proj.fzy.dynamicauthority.model.db.Role;
import proj.fzy.dynamicauthority.model.dto.ResourceInfo;
import proj.fzy.dynamicauthority.repository.AuthorityRepository;
import proj.fzy.dynamicauthority.repository.ResourceRepository;
import proj.fzy.dynamicauthority.repository.RoleRepository;
import proj.fzy.dynamicauthority.untils.EndpointUtils;

import java.util.*;

@Service
public class ResourceService {

    private final EndpointUtils endpointUtils;
    private final ResourceRepository resourceRepository;
    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;

    public ResourceService(
            EndpointUtils endpointUtils,
            ResourceRepository resourceRepository,
            AuthorityRepository authorityRepository,
            RoleRepository roleRepository) {
        this.endpointUtils = endpointUtils;
        this.resourceRepository = resourceRepository;
        this.authorityRepository = authorityRepository;
        this.roleRepository = roleRepository;
    }

    public Map<String, Set<String>> getEndpointInfo() {
        return endpointUtils.getEndpointInfo();
    }

    public ResourceInfo get(String method, String path) {
        Optional<Resource> optionalResource = resourceRepository.findByMethodAndPath(method, path);
        if (optionalResource.isPresent()) {
            Resource resource = optionalResource.get();
            return ResourceInfo.builder()
                    .method(resource.getMethod())
                    .path(resource.getPath())
                    .authorizationType(resource.getAuthorizationType().name())
                    .authorityNames(resource.getAuthorities().stream().map(Authority::getName).toList())
                    .roleNames(resource.getRoles().stream().map(Role::getName).toList())
                    .build();
        }
        return null;
    }

    public List<ResourceInfo> getAll() {
        List<Resource> resources = resourceRepository.findAll();
        if (resources.size() != 0) {
            return resources.stream()
                    .map(resource -> get(resource.getMethod(), resource.getPath()))
                    .toList();
        }
        return new ArrayList<>();
    }

    public boolean updateAuthorizationType(String method, String path, ResourceAuthorizationType authorizationType) {
        Optional<Resource> optionalResource = resourceRepository.findByMethodAndPath(method, path);
        if (optionalResource.isPresent()) {
            Resource resource = optionalResource.get();
            resource.setAuthorizationType(authorizationType);
            resourceRepository.save(resource);
            return true;
        }
        return false;
    }

    public boolean assignAuthority(String method, String path, String authorityName) {
        Optional<Resource> optionalResource = resourceRepository.findByMethodAndPath(method, path);
        Optional<Authority> optionalAuthority = authorityRepository.findByName(authorityName);
        if (optionalResource.isPresent() && optionalAuthority.isPresent()) {
            Resource resource = optionalResource.get();
            resource.getAuthorities().add(optionalAuthority.get());
            resourceRepository.save(resource);
            return true;
        }
        return false;
    }

    public boolean removeAuthority(String method, String path, String authorityName) {
        Optional<Resource> optionalResource = resourceRepository.findByMethodAndPath(method, path);
        Optional<Authority> optionalAuthority = authorityRepository.findByName(authorityName);
        if (optionalResource.isPresent() && optionalAuthority.isPresent()) {
            Resource resource = optionalResource.get();
            resource.getAuthorities().remove(optionalAuthority.get());
            resourceRepository.save(resource);
            return true;
        }
        return false;
    }

    public boolean assignRole(String method, String path, String roleName) {
        Optional<Resource> optionalResource = resourceRepository.findByMethodAndPath(method, path);
        Optional<Role> optionalRole = roleRepository.findByName(roleName);
        if (optionalResource.isPresent() && optionalRole.isPresent()) {
            Resource resource = optionalResource.get();
            resource.getRoles().add(optionalRole.get());
            resourceRepository.save(resource);
            return true;
        }
        return false;
    }

    public boolean removeRole(String method, String path, String roleName) {
        Optional<Resource> optionalResource = resourceRepository.findByMethodAndPath(method, path);
        Optional<Role> optionalRole = roleRepository.findByName(roleName);
        if (optionalResource.isPresent() && optionalRole.isPresent()) {
            Resource resource = optionalResource.get();
            resource.getRoles().remove(optionalRole.get());
            resourceRepository.save(resource);
            return true;
        }
        return false;
    }

}
