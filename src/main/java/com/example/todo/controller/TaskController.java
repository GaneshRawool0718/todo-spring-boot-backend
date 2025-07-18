package com.example.todo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.example.todo.dto.TaskDTO;
import com.example.todo.model.Task;
import com.example.todo.model.User;
import com.example.todo.repository.TaskRepository;
import com.example.todo.repository.UserRepository;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/user/tasks")
@RequiredArgsConstructor
public class TaskController {
    // This controller handles task-related operations for users.
    // It allows users to create, retrieve, update, and delete tasks.
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @PostMapping
public Task createTask(@RequestBody TaskDTO taskDTO) {
    // This method creates a new task for a user.
    // It expects a TaskDTO object containing task details and the user ID.
    User user = userRepository.findById(taskDTO.getUserId())
        .orElseThrow(() -> new RuntimeException("User not found"));

    Task task = new Task();
    task.setTitle(taskDTO.getTitle());
    task.setDescription(taskDTO.getDescription());
    task.setCreatedDate(new Date());
    task.setDueDate(taskDTO.getDueDate());
    task.setUser(user);

    return taskRepository.save(task);
}


    @GetMapping
public List<Task> getUserTasks(@RequestParam Integer userId) {
    // This method retrieves all tasks for a specific user.
    // It expects a user ID as a request parameter.
    return taskRepository.findByUserId(userId);
}

@PutMapping("/{taskId}")
public Task updateTask(@PathVariable Integer taskId, @RequestBody TaskDTO taskDTO) {
    // This method updates an existing task.
    // It expects a task ID in the path and a TaskDTO object in the request body.
    // It updates the task's title, description, and due date if they have changed.
    Task task = taskRepository.findById(taskId)
        .orElseThrow(() -> new RuntimeException("Task not found"));

    boolean isChanged = false;

    if (!task.getTitle().equals(taskDTO.getTitle())) {
        // Check if the title has changed
        // If the title has changed, update it and mark as changed
        task.setTitle(taskDTO.getTitle());
        isChanged = true;
    }

    if (!task.getDescription().equals(taskDTO.getDescription())) {
        // Check if the description has changed
        // If the description has changed, update it and mark as changed
        task.setDescription(taskDTO.getDescription());
        isChanged = true;
    }

    if (!task.getDueDate().equals(taskDTO.getDueDate())) {
        // Check if the due date has changed
        // If the due date has changed, update it and mark as changed
        task.setDueDate(taskDTO.getDueDate());
        isChanged = true;
    }

    if (isChanged) {
        task.setCreatedDate(new Date()); // Update only if content changed
    }

    return taskRepository.save(task); // Save the updated task
}

@DeleteMapping("/{taskId}")
public void deleteTask(@PathVariable Integer taskId) {
    // This method deletes a task by its ID.
    // It expects a task ID in the path.
    Task task = taskRepository.findById(taskId)
        .orElseThrow(() -> new RuntimeException("Task not found"));
    taskRepository.delete(task);
}

}

