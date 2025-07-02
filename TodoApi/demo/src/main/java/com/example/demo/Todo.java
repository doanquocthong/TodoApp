package com.example.demo;

import jakarta.persistence.*;
import lombok.Data;
//***************
@Data
//***************
@Entity
@Table(name = "todo")
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private String description;
    private Boolean completed;

}
