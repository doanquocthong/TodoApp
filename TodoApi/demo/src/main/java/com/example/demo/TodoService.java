package com.example.demo;


import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    private final TodoRepository repo;

    public TodoService(TodoRepository repo) {
        this.repo = repo;
    }

    public List<Todo> getAll() {return repo.findAll();};
    public Optional<Todo> getByID(int id) {return repo.findById(id);};
    public void delete(int id) {repo.deleteById(id);}
    public Todo save(Todo todo) {return repo.save(todo);}
}
