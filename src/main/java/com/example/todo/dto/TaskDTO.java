package com.example.todo.dto;

import lombok.Data;
import java.util.Date;

@Data
public class TaskDTO {
    // This class represents the data transfer object for a task.
    // It includes the task's ID, title, description, creation date, due date,
    private int id;
    private String title;
    private String description;
    private Date createdDate;
    private Date dueDate;
    private Integer userId;  // Can be used for references
}


