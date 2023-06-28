package proj.fzy.dynamicauthority.service;

import org.springframework.stereotype.Service;
import proj.fzy.dynamicauthority.enums.ResourceAuthorizationType;
import proj.fzy.dynamicauthority.enums.ResourcePermitAll;
import proj.fzy.dynamicauthority.exception.NotFoundException;
import proj.fzy.dynamicauthority.model.db.Resource;
import proj.fzy.dynamicauthority.model.db.User;
import proj.fzy.dynamicauthority.repository.ResourceRepository;
import proj.fzy.dynamicauthority.repository.UserRepository;
import proj.fzy.dynamicauthority.untils.AuthorizationUtils;
import proj.fzy.dynamicauthority.untils.EndpointUtils;

import java.util.Optional;
import java.util.Set;

@Service
public class VerificationService {

    private final UserRepository userRepository;
    private final ResourceRepository resourceRepository;

    public VerificationService(UserRepository userRepository, ResourceRepository resourceRepository) {
        this.userRepository = userRepository;
        this.resourceRepository = resourceRepository;
    }

    public boolean verify(String username, String method, String path) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        Optional<Resource> optionalResource = resourceRepository.findByMethodAndPath(method, path);
        if (optionalUser.isPresent() && optionalResource.isPresent()) {
            Resource resource = optionalResource.get();
            if (resource.getPermitAll() == ResourcePermitAll.FREE_TO_GO ||
                    resource.getAuthorizationType() == ResourceAuthorizationType.NO_AUTH) {
                return true;
            }
            Set<String> resourceTags = AuthorizationUtils.getTags(resource.getRoles(), resource.getAuthorities());
            User user = optionalUser.get();
            Set<String> userTags = AuthorizationUtils.getTags(user.getRoles(), user.getAuthorities());
            if (resource.getAuthorizationType() == ResourceAuthorizationType.ANY) {
                return AuthorizationUtils.hasAny(userTags, resourceTags);
            } else {
                return AuthorizationUtils.hasAll(userTags, resourceTags);
            }
        }
        throw new NotFoundException();
    }
}
