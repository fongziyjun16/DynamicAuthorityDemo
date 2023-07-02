package proj.fzy.dynamicauthority.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proj.fzy.dynamicauthority.model.db.Resource;

import java.util.Optional;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Integer> {
    Optional<Resource> findByMethodAndPath(String method, String name);
}
