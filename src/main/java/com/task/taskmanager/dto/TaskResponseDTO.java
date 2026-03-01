package com.task.taskmanager.dto;

import com.task.taskmanager.model.TaskPriority;
import com.task.taskmanager.model.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TaskResponseDTO {

    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDateTime createdAt;
    private LocalDateTime dueDate;
    private Long userId;
}
