package com.example.demo.repositories;

import com.example.demo.entities.Todo;
import com.example.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends GenericRepository<Todo, Long> {

    List<Todo> findByUser(User user);

}
