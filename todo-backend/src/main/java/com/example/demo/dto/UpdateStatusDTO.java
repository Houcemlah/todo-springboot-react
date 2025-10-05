package com.example.demo.dto;

import com.example.demo.entities.TaskStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateStatusDTO {

    @NotNull(message = "The Status should not be empty")
    private TaskStatus status;

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(@NotNull(message = "The Status should not be empty") TaskStatus status) {
        this.status = status;
    }
}
