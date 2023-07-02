package proj.fzy.dynamicauthority.component;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import proj.fzy.dynamicauthority.config.AuthorizationConfiguration;
import proj.fzy.dynamicauthority.enums.ResourcePermitAll;
import proj.fzy.dynamicauthority.model.db.Resource;
import proj.fzy.dynamicauthority.model.db.Role;
import proj.fzy.dynamicauthority.model.db.User;
import proj.fzy.dynamicauthority.repository.ResourceRepository;
import proj.fzy.dynamicauthority.repository.RoleRepository;
import proj.fzy.dynamicauthority.repository.UserRepository;
import proj.fzy.dynamicauthority.untils.EndpointUtils;

import java.util.*;

@Component
public class ApplicationStartedHandler implements ApplicationListener<ApplicationStartedEvent> {

    private final AuthorizationConfiguration authorizationConfiguration;
    private final EndpointUtils endpointUtils;
    private final ResourceRepository resourceRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public ApplicationStartedHandler(
            AuthorizationConfiguration authorizationConfiguration,
            EndpointUtils endpointUtils,
            ResourceRepository resourceRepository,
            UserRepository userRepository,
            RoleRepository roleRepository) {
        this.authorizationConfiguration = authorizationConfiguration;
        this.endpointUtils = endpointUtils;
        this.resourceRepository = resourceRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        Map<String, Set<String>> endpointInfo = endpointUtils.getEndpointInfo();
//        System.out.println(endpointInfo);
        // store all endpoint information
        endpointInfo.forEach((method, paths) -> {
            paths.forEach(path -> {
                Optional<Resource> optionalResource = resourceRepository.findByMethodAndPath(method, path);
                if (optionalResource.isEmpty()) {
                    resourceRepository.save(Resource.builder()
                            .method(method)
                            .path(path)
                            .build());
                } else {
                    Resource resource = optionalResource.get();
                    resource.setPermitAll(ResourcePermitAll.AUTH);
                    resourceRepository.save(resource);
                }
            });
        });
        resourceRepository.flush();
        // set permit all
        authorizationConfiguration.getWhiteList().forEach(resource -> {
            Optional<Resource> optionalResource
                    = resourceRepository.findByMethodAndPath(resource.getMethod(), resource.getPath());
            if (optionalResource.isPresent()) {
                Resource dbResource = optionalResource.get();
                dbResource.setPermitAll(ResourcePermitAll.FREE_TO_GO);
                resourceRepository.save(dbResource);
            }
        });

        // default role: root
        if (roleRepository.findByName("root").isEmpty()) {
            roleRepository.save(Role.builder()
                    .name("root")
                    .build());
            roleRepository.flush();
        }

        // default root user
        if (userRepository.findByUsername("default").isEmpty()) {
            User defaultUser = User.builder()
                    .username("default")
                    .password(new StrongPasswordEncryptor().encryptPassword("123qweasd"))
                    .roles(new HashSet<>())
                    .build();
            if (roleRepository.findByName("root").isPresent()) {
                defaultUser.getRoles().add(roleRepository.findByName("root").get());
            }
            userRepository.save(defaultUser);
        }
    }
}
