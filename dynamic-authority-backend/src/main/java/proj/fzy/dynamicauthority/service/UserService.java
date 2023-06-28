package proj.fzy.dynamicauthority.service;

import jakarta.servlet.http.HttpServletRequest;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.stereotype.Service;
import proj.fzy.dynamicauthority.model.db.Authority;
import proj.fzy.dynamicauthority.model.db.Role;
import proj.fzy.dynamicauthority.model.db.User;
import proj.fzy.dynamicauthority.model.dto.UserInfo;
import proj.fzy.dynamicauthority.repository.AuthorityRepository;
import proj.fzy.dynamicauthority.repository.RoleRepository;
import proj.fzy.dynamicauthority.repository.UserRepository;
import proj.fzy.dynamicauthority.untils.JWTUtils;

import java.util.Optional;

@Service
public class UserService {

    private final JWTUtils jwtUtils;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;

    public UserService(
            JWTUtils jwtUtils,
            UserRepository userRepository,
            RoleRepository roleRepository,
            AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
    }

    public String signIn(String username, String password) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent() && new StrongPasswordEncryptor().checkPassword(password, optionalUser.get().getPassword())) {
            return jwtUtils.generate(username);
        }
        return null;
    }

    public boolean hasRoot(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            for (Role role : optionalUser.get().getRoles()) {
                if (role.getName().equals("root")) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean authenticate(HttpServletRequest request) {
        return jwtUtils.verify(jwtUtils.extraTokenFromHttpServletRequest(request));
    }

    public boolean signUp(String username, String password) {
        if (userRepository.findByUsername(username).isEmpty()) {
            userRepository.save(User.builder()
                    .username(username)
                    .password(new StrongPasswordEncryptor().encryptPassword(password))
                    .build());
            return true;
        }
        return false;
    }

    public UserInfo getUserInfo(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User dbUser = optionalUser.get();
            return UserInfo.builder()
                    .username(dbUser.getUsername())
                    .roleNames(dbUser.getRoles().stream().map(Role::getName).toList())
                    .authorityNames(dbUser.getAuthorities().stream().map(Authority::getName).toList())
                    .build();
        }
        return null;
    }

    public boolean addRole(String username, String roleName) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        Optional<Role> optionalRole = roleRepository.findByName(roleName);
        if (optionalUser.isPresent() && optionalRole.isPresent()) {
            User dbUser = optionalUser.get();
            dbUser.getRoles().add(optionalRole.get());
            userRepository.save(dbUser);
            return true;
        }
        return false;
    }

    public boolean addAuthority(String username, String authorityName) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        Optional<Authority> optionalAuthority = authorityRepository.findByName(authorityName);
        if (optionalUser.isPresent() && optionalAuthority.isPresent()) {
            User dbUser = optionalUser.get();
            dbUser.getAuthorities().add(optionalAuthority.get());
            userRepository.save(dbUser);
            return true;
        }
        return false;
    }


}
