package org.web.guffgaff.guffgaff.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.web.guffgaff.guffgaff.entities.User;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Query("SELECT u.username FROM User u WHERE u.username <> :currentUsername")
    List<String> findAllUsernames(@Param("currentUsername") String currentUsername);
}
