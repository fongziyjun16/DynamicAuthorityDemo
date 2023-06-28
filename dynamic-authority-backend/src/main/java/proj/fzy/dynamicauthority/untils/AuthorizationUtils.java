package proj.fzy.dynamicauthority.untils;

import org.springframework.stereotype.Component;
import proj.fzy.dynamicauthority.model.db.Authority;
import proj.fzy.dynamicauthority.model.db.Role;
import proj.fzy.dynamicauthority.repository.ResourceRepository;
import proj.fzy.dynamicauthority.repository.UserRepository;

import java.util.*;

public class AuthorizationUtils {

    public static Set<String> getTags(Set<Role> roles, Set<Authority> authorities) {
        Set<String> tags = new HashSet<>();
        roles.forEach(role -> {
            if (role.getAuthorities().size() == 0) {
                tags.add(role.getName());
            } else {
                role.getAuthorities().forEach(authority -> tags.add(authority.getName()));
            }
        });
        authorities.forEach(authority -> {
            tags.add(authority.getName());
        });
        return tags;
    }

    public static boolean hasAll(Set<String> userTags, Set<String> resourceTags) {
        return userTags.size() == resourceTags.size() && userTags.containsAll(resourceTags);
    }

    public static boolean hasAny(Set<String> userTags, Set<String> resourceTags) {
        return !Collections.disjoint(userTags, resourceTags);
    }

}
