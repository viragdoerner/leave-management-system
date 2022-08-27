package lms.backend.dao;

import lms.backend.auth.model.RoleName;
import lms.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    @Query("Select u From User u join u.roles role where role.name = :roleName")
    List<User> findAllAdmin(RoleName roleName);
}
