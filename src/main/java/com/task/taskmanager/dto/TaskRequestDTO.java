package com.task.taskmanager.dto;

import com.task.taskmanager.model.TaskPriority;
import com.task.taskmanager.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskRequestDTO {

    @NotBlank
    private String title;

    private String description;

    private TaskStatus status;

    private TaskPriority priority;

    private LocalDateTime dueDate;

    private Long userId;
}
