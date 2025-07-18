package com.example.todo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<com.example.todo.model.Task, Integer> {
    // Find tasks by user ID
    // This method retrieves all tasks associated with a specific user.
    List<com.example.todo.model.Task> findByUserId(Integer userId);
}

