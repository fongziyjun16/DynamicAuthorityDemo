package proj.fzy.dynamicauthority.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import proj.fzy.dynamicauthority.model.db.Authority;
import proj.fzy.dynamicauthority.repository.AuthorityRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorityService {
    private final AuthorityRepository authorityRepository;

    public AuthorityService(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    public boolean createNewAuthority(String name) {
        if (authorityRepository.findByName(name).isEmpty()) {
            authorityRepository.save(Authority.builder().name(name).build());
            return true;
        }
        return false;
    }

    @Transactional
    public boolean deleteAuthority(String name) {
        Optional<Authority> optionalAuthority = authorityRepository.findByName(name);
        if (optionalAuthority.isPresent()) {
            Authority dbAuthority = optionalAuthority.get();
            dbAuthority.getUsers().forEach(user -> user.getAuthorities().remove(dbAuthority));
            dbAuthority.getResources().forEach(resource -> resource.getAuthorities().remove(dbAuthority));
            dbAuthority.getRoles().forEach(role -> role.getAuthorities().remove(dbAuthority));
            authorityRepository.deleteByName(name);
            return true;
        }
        return false;
    }

    public Authority getByName(String name) {
        return authorityRepository.findByName(name).orElse(null);
    }

    public List<Authority> getAll() {
        return authorityRepository.findAll();
    }
}
