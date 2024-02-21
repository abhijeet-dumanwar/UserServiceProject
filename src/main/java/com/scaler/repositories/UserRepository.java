package com.scaler.repositories;

import com.scaler.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User save(User user);

    Optional<User> findByEmail(String email);
}
