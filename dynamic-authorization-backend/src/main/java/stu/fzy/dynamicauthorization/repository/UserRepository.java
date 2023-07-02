package stu.fzy.dynamicauthorization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import stu.fzy.dynamicauthorization.model.db.Role;
import stu.fzy.dynamicauthorization.model.db.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
