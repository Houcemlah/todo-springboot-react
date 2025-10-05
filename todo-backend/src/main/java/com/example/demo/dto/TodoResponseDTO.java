package com.example.demo.dto;

import com.example.demo.entities.TaskStatus;
import com.example.demo.entities.Todo;


public class TodoResponseDTO {

    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private Long assignedUserId;
    private String assignedUserName;

    public TodoResponseDTO(Todo todo){

        this.id = todo.getId();
        this.title = todo.getTitle();
        this.description = todo.getDescription();
        this.status = todo.getStatus();
        if (todo.getUser() != null){

            this.assignedUserId = todo.getUser().getId();
            this.assignedUserName = todo.getUser().getName();
        }

    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public Long getAssignedUserId() {
        return assignedUserId;
    }

    public String getAssignedUserName() {
        return assignedUserName;
    }
}
