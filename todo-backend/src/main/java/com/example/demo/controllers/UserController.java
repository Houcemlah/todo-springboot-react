package com.example.demo.controllers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Set;

import com.example.demo.entities.RoleType;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.dto.UserDTO;
import com.example.demo.entities.Role;
import com.example.demo.security.JwtUtils;

import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entities.User;
import com.example.demo.services.UserService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService service;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public UserController(UserService service, RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.service = service;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    private User getAuthenticatedUser(String authHeader){
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        String token = authHeader.replace("Bearer ","");
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtUtils.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        Long userId = claims.get("userId", Long.class);

        return service.findByID(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @GetMapping
    public List<User> all() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> get(@PathVariable Long id){
        return service.findByID(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody UserDTO userDTO){
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        Role userRole = roleRepository.findByName(RoleType.USER)
                .orElseThrow(() -> new RuntimeException("Role USER not found"));
        user.setRoles(Set.of(userRole));

        User saved = service.create(user);
        return ResponseEntity.created(URI.create("/api/users/"+ saved.getId())).body(saved);
    }

    @PostMapping("/admin")
    public ResponseEntity<User> createAdmin(@Valid @RequestBody UserDTO userDTO,
                                            @RequestHeader("Authorization") String authHeader){
        User authUser = getAuthenticatedUser(authHeader);


        boolean isAdmin = authUser.getRoles().stream()
                .anyMatch(r -> r.getName().equals(RoleType.ADMIN));
        if (!isAdmin){
            return ResponseEntity.status(403).build();
        }

        User admin = new User();
        admin.setName(userDTO.getName());
        admin.setEmail(userDTO.getEmail());
        admin.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        Role adminRole = roleRepository.findByName(RoleType.ADMIN)
                .orElseThrow(() -> new RuntimeException("Role ADMIN not found"));
        admin.setRoles(Set.of(adminRole));

        User saved = service.create(admin);
        return ResponseEntity.created(URI.create("/api/users/admin/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO){
        User updated = new User();
        updated.setName(userDTO.getName());
        updated.setEmail(userDTO.getEmail());

        return service.update(id, updated).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
