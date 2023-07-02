package stu.fzy.dynamicauthorization.service;

import org.springframework.stereotype.Service;
import stu.fzy.dynamicauthorization.enums.ResourceAuthType;
import stu.fzy.dynamicauthorization.model.db.Resource;
import stu.fzy.dynamicauthorization.model.db.User;
import stu.fzy.dynamicauthorization.repository.ResourceRepository;
import stu.fzy.dynamicauthorization.repository.UserRepository;

import java.util.Collections;
import java.util.Optional;

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
        if (optionalUser.isPresent() &&optionalResource.isPresent()) {
            User user = optionalUser.get();
            Resource resource = optionalResource.get();
            if (!resource.getAuthType().equals(ResourceAuthType.PERMIT_ALL) && !resource.getAuthType().equals(ResourceAuthType.JUST_AUTHENTICATION)) {
                if (resource.getAuthType().equals(ResourceAuthType.ANY_ROLE)) {
                    return !Collections.disjoint(user.getRoles(), resource.getRoles());
                } else { // ResourceAuthType.ALL_ROLE
                    return user.getRoles().containsAll(resource.getRoles());
                }
            }
            return true;
        }
        return false;
    }
}
