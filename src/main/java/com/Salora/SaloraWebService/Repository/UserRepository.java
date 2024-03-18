package com.Salora.SaloraWebService.Repository;

import com.Salora.SaloraWebService.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

    Optional<User> findById(String id);
}
