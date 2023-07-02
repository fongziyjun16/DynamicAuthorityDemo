package stu.fzy.dynamicauthorization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;
import stu.fzy.dynamicauthorization.model.db.Resource;

import java.util.Optional;


@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    Optional<Resource> findByMethodAndPath(String method, String path);
}
