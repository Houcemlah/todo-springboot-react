package com.example.demo.services;

import com.example.demo.entities.Todo;
import com.example.demo.entities.User;
import com.example.demo.repositories.GenericRepository;
import com.example.demo.repositories.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService extends GenericServices<Todo, Long>{


    private final TodoRepository repo;

    public TodoService(TodoRepository repo){
        this.repo = repo;
    }

    @Override
    protected GenericRepository<Todo, Long> getRepository() {
        return repo;
    }

    public List<Todo> findByUser(User user){
        return repo.findByUser(user);
    }
    public Optional<Todo> update (Long id, Todo updated){
        return repo.findById(id).map(todo -> {
            if (updated.getTitle() != null) todo.setTitle(updated.getTitle());
            if (updated.getDescription() != null) todo.setDescription(updated.getDescription());
            if (updated.getStatus() != null) todo.setStatus(updated.getStatus());
            if (updated.getUser() != null) todo.setUser(updated.getUser());
            return repo.save(todo);
        });
    }
}
