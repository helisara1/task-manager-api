package com.task.taskmanager.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.task.taskmanager.dto.ApiResponse;
import com.task.taskmanager.dto.TaskRequestDTO;
import com.task.taskmanager.dto.TaskResponseDTO;
import com.task.taskmanager.model.Task;
import com.task.taskmanager.service.TaskService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    // ✅ Only inject TaskService now
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // ---------------- CREATE ----------------
    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponseDTO>> createTask(
            @Valid @RequestBody TaskRequestDTO dto) {

        TaskResponseDTO response = taskService.createTask(dto);

        return ResponseEntity.status(201)
                .body(new ApiResponse<>("Task created successfully", response));
    }

    // ---------------- GET (User-Specific / Admin-All) ----------------
    @GetMapping
    public ResponseEntity<ApiResponse<Page<Task>>> getTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<Task> tasks = taskService.getTasks(page, size);

        return ResponseEntity.ok(
                new ApiResponse<>("Tasks fetched successfully", tasks)
        );
    }

    // ---------------- UPDATE STATUS ----------------
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<TaskResponseDTO>> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        TaskResponseDTO updated =
                taskService.updateStatus(id,
                        com.task.taskmanager.model.TaskStatus.valueOf(status));

        return ResponseEntity.ok(
                new ApiResponse<>("Task status updated successfully", updated)
        );
    }
     @PutMapping("/{id}")
public ResponseEntity<ApiResponse<TaskResponseDTO>> updateTask(
        @PathVariable Long id,
        @RequestBody TaskRequestDTO dto) {

    TaskResponseDTO response = taskService.updateTask(id, dto);

    return ResponseEntity.ok(
            new ApiResponse<>("Task updated successfully", response)
    );
}
    // ---------------- DELETE ----------------
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteTask(
            @PathVariable Long id) {

        taskService.deleteTask(id);

        return ResponseEntity.ok(
                new ApiResponse<>("Task deleted successfully", null)
        );
    }
}