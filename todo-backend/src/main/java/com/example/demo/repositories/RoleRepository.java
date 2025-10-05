package com.example.demo.repositories;

import com.example.demo.entities.Role;
import com.example.demo.entities.Role;
import com.example.demo.entities.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleType name);
}
