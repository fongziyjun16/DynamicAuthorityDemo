package stu.fzy.dynamicauthorization.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import stu.fzy.dynamicauthorization.enums.ResourceAuthType;
import stu.fzy.dynamicauthorization.model.db.Resource;
import stu.fzy.dynamicauthorization.model.db.Role;
import stu.fzy.dynamicauthorization.model.db.User;
import stu.fzy.dynamicauthorization.repository.RoleRepository;
import stu.fzy.dynamicauthorization.repository.UserRepository;
import stu.fzy.dynamicauthorization.utils.JWTUtils;

import java.util.*;

@Service
public class UserService {

    private final JWTUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(
            JWTUtils jwtUtils, PasswordEncoder passwordEncoder,
            UserRepository userRepository, RoleRepository roleRepository) {
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public boolean signUp(String username, String password) {
        if (userRepository.findByUsername(username).isEmpty()) {
            userRepository.save(User.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .build());
            userRepository.flush();
            return true;
        }
        return false;
    }

    public String signIn(String username, String password) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User dbUser = optionalUser.get();
            if (passwordEncoder.matches(password, dbUser.getPassword())) {
                return jwtUtils.generate(dbUser.getUsername());
            }
        }
        return null;
    }

    public User get(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Set<Role> getRoles(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        return optionalUser.isEmpty() ?
                new HashSet<>() :
                optionalUser.get().getRoles();
    }

    public boolean assignRole(String username, List<String> roleNames) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
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
        User user = optionalUser.get();
        if (roles.size() == 0) {
            user.getRoles().clear();
        } else {
            for (Role role : roles) {
                user.getRoles().add(role);
            }
        }
        userRepository.save(user);
        return true;
    }
}
