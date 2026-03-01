package com.task.taskmanager.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.task.taskmanager.model.Task;
import com.task.taskmanager.model.TaskStatus;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByDeletedFalse(Pageable pageable);

    Page<Task> findByUserIdAndDeletedFalse(Long userId, Pageable pageable);

    Page<Task> findByStatusAndDeletedFalse(TaskStatus status, Pageable pageable);
}