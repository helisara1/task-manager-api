package com.task.taskmanager.service;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.task.taskmanager.dto.TaskRequestDTO;
import com.task.taskmanager.dto.TaskResponseDTO;
import com.task.taskmanager.exception.TaskNotFoundException;
import com.task.taskmanager.exception.UserNotFoundException;
import com.task.taskmanager.model.Role;
import com.task.taskmanager.model.Task;
import com.task.taskmanager.model.TaskStatus;
import com.task.taskmanager.model.User;
import com.task.taskmanager.repository.TaskRepository;
import com.task.taskmanager.repository.UserRepository;

@Service
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    // ---------------- CREATE ----------------

    public TaskResponseDTO createTask(TaskRequestDTO dto) {

        String username = SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getName();

User user = userRepository.findByUsername(username)
        .orElseThrow(() ->
                new UserNotFoundException("User not found: " + username));

logger.info("Creating task for logged-in user {}", username);

        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setPriority(dto.getPriority());
        task.setDueDate(dto.getDueDate());
        task.setCreatedAt(LocalDateTime.now());
        task.setUser(user);

        Task saved = taskRepository.save(task);

        logger.info("Task created with id {}", saved.getId());

        return mapToResponse(saved);
    }

    
    // ---------------- GET ALL ----------------
public Page<Task> getTasks(int page, int size) {

    String username = SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getName();

    User currentUser = userRepository.findByUsername(username)
            .orElseThrow(() ->
                    new UserNotFoundException("User not found: " + username));

    logger.info("Fetching tasks for user {}", currentUser.getId());

    if (currentUser.getRole() == Role.ADMIN) {
        return taskRepository.findByDeletedFalse(PageRequest.of(page, size));
    }

    return taskRepository.findByUserIdAndDeletedFalse(
            currentUser.getId(),
            PageRequest.of(page, size)
    );
}

    // ---------------- UPDATE STATUS ----------------

    public TaskResponseDTO updateStatus(Long taskId, TaskStatus newStatus) {

    String username = SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getName();

    User currentUser = userRepository.findByUsername(username)
            .orElseThrow(() ->
                    new UserNotFoundException("User not found: " + username));

    Task task = taskRepository.findById(taskId)
            .orElseThrow(() ->
                    new TaskNotFoundException("Task not found with id: " + taskId));

    if (currentUser.getRole() != Role.ADMIN &&
            !task.getUser().getId().equals(currentUser.getId())) {
        throw new RuntimeException("You are not allowed to update this task");
    }

    if (!isValidTransition(task.getStatus(), newStatus)) {
        throw new IllegalArgumentException("Invalid status transition");
    }

    task.setStatus(newStatus);

    Task updated = taskRepository.save(task);

    logger.info("Task {} status updated by user {}", taskId, currentUser.getId());

    return mapToResponse(updated);
}
    
    // ---------------- FULL UPDATE ----------------

public TaskResponseDTO updateTask(Long taskId, TaskRequestDTO dto) {

    String username = SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getName();

    User currentUser = userRepository.findByUsername(username)
            .orElseThrow(() ->
                    new UserNotFoundException("User not found: " + username));

    Task task = taskRepository.findById(taskId)
            .orElseThrow(() ->
                    new TaskNotFoundException("Task not found with id: " + taskId));

    // 🔐 Ownership Check
    if (currentUser.getRole() != Role.ADMIN &&
            !task.getUser().getId().equals(currentUser.getId())) {
        throw new RuntimeException("You are not allowed to update this task");
    }

    // ✏ Update Fields
    task.setTitle(dto.getTitle());
    task.setDescription(dto.getDescription());
    task.setPriority(dto.getPriority());
    task.setDueDate(dto.getDueDate());

    // Optional: also allow status update here
    task.setStatus(dto.getStatus());

    Task updated = taskRepository.save(task);

    logger.info("Task {} fully updated by user {}", taskId, currentUser.getId());

    return mapToResponse(updated);
}
    public void deleteTask(Long taskId) {

    String username = SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getName();

    User currentUser = userRepository.findByUsername(username)
            .orElseThrow(() ->
                    new UserNotFoundException("User not found: " + username));

    Task task = taskRepository.findById(taskId)
            .orElseThrow(() ->
                    new TaskNotFoundException("Task not found with id: " + taskId));

    if (currentUser.getRole() != Role.ADMIN &&
            !task.getUser().getId().equals(currentUser.getId())) {
        throw new RuntimeException("You are not allowed to delete this task");
    }

    task.setDeleted(true);
    taskRepository.save(task);

    logger.info("Task {} soft deleted by user {}", taskId, currentUser.getId());
}
    // ---------------- WORKFLOW VALIDATION ----------------

    private boolean isValidTransition(TaskStatus current, TaskStatus next) {

        if (current == TaskStatus.TODO && next == TaskStatus.IN_PROGRESS)
            return true;

        if (current == TaskStatus.IN_PROGRESS && next == TaskStatus.DONE)
            return true;

        return current == next;
    }

    // ---------------- MAPPER ----------------

    private TaskResponseDTO mapToResponse(Task task) {
        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getCreatedAt(),
                task.getDueDate(),
                task.getUser().getId()
        );
    }
}
