package com.wipro.newsapp.user.repository;

import com.wipro.newsapp.user.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(String role);
}
