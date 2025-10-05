package com.example.demo.controllers;

import com.example.demo.dto.TodoDTO;
import com.example.demo.dto.TodoResponseDTO;
import com.example.demo.entities.RoleType;
import com.example.demo.entities.Todo;
import com.example.demo.entities.User;
import com.example.demo.services.TodoService;
import com.example.demo.services.UserService;
import com.example.demo.security.JwtUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("api/todos")
public class TodoController {

    private final TodoService todoService;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    public TodoController(TodoService todoService, UserService userService, JwtUtils jwtUtils){
        this.todoService = todoService;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    private User getAuthenticatedUser(String authHeader){
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        String token = authHeader.replace("Bearer ", "");
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtUtils.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        Long userId = claims.get("userId", Long.class);
        List<String> roles = claims.get("roles", List.class);

        return userService.findByID(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
            }

    private boolean isAdmin(User user){
        return user.getRoles().stream().anyMatch(r -> r.getName() == RoleType.ADMIN);
    }

    @GetMapping
    public ResponseEntity<List<TodoResponseDTO>> getTodos(@RequestHeader("Authorization") String authHeader){

        User user = getAuthenticatedUser(authHeader);
        List<Todo> todos = isAdmin(user) ? todoService.findAll() : todoService.findByUser(user);

        List<TodoResponseDTO> dtoList = todos.stream()
                .map(TodoResponseDTO::new)
                .toList();

        return ResponseEntity.ok(dtoList);
    }

    @PostMapping
    public ResponseEntity<TodoResponseDTO> createTodo(@RequestHeader("Authorization") String authHeader,
                                           @Valid @RequestBody TodoDTO dto){
        User authUser = getAuthenticatedUser(authHeader);
        if (!isAdmin(authUser)) {
            return ResponseEntity.status(403).build();
        }

        if (dto.getAssignedUserId() == null) {
            return ResponseEntity.badRequest().build();
        }

        User assignedUser = userService.findByID(dto.getAssignedUserId())
                .orElseThrow(() -> new RuntimeException("Assigned user not found"));

        Todo todo = new Todo(dto.getTitle(), dto.getDescription(), assignedUser);
        if (dto.getStatus() != null) {
            todo.setStatus(dto.getStatus());
        }

        Todo saved = todoService.create(todo);
        return ResponseEntity.created(URI.create("/api/todos/" + saved.getId())).body(new TodoResponseDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoResponseDTO> updateTodo(@RequestHeader("Authorization") String authHeader,
                                           @PathVariable Long id,
                                           @Valid @RequestBody TodoDTO dto){
        User authUser = getAuthenticatedUser(authHeader);

        Todo todo = todoService.findByID(id)
                .orElseThrow(() -> new RuntimeException("Todo not found"));

        if (isAdmin(authUser)){
            if (dto.getAssignedUserId() != null){
                User newUser = userService.findByID(dto.getAssignedUserId())
                        .orElseThrow(() -> new RuntimeException("Assigned user not found"));
                todo.setUser(newUser);
            }
            if (dto.getTitle() != null) todo.setTitle(dto.getTitle());
            if (dto.getDescription() != null) todo.setDescription(dto.getDescription());
            if (dto.getStatus() != null) todo.setStatus(dto.getStatus());
            todoService.update(id, todo);
            return ResponseEntity.ok(new TodoResponseDTO(todo));
        }

        if (!todo.getUser().getId().equals(authUser.getId())) {
            return ResponseEntity.status(403).build();
        }
        if (dto.getStatus() != null){
            todo.setStatus(dto.getStatus());
            todoService.update(id, todo);
            return ResponseEntity.ok(new TodoResponseDTO(todo));
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@RequestHeader("Authorization") String authHeader,
                                           @PathVariable Long id){
        User authUser = getAuthenticatedUser(authHeader);
        if (!isAdmin(authUser)){
            return ResponseEntity.status(403).build();
        }
        todoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/status")
    public ResponseEntity<TodoResponseDTO> updateStatus(@RequestHeader("Authorization") String authHeader,
                                             @RequestParam Long id,
                                             @RequestParam String status){
        User authUser = getAuthenticatedUser(authHeader);

        Todo todo = todoService.findByID(id)
                .orElseThrow(() -> new RuntimeException("Todo not found"));

        try{
            todo.setStatus(Enum.valueOf(com.example.demo.entities.TaskStatus.class, status));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        if (isAdmin(authUser)){
            todoService.update(id, todo);
            return ResponseEntity.ok(new TodoResponseDTO(todo));
        }

        todoService.update(id, todo);
        return ResponseEntity.ok(new TodoResponseDTO(todo));
    }
}
