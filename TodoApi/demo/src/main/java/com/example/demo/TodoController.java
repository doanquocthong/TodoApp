package com.example.demo;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "*")
public class TodoController {

    private final TodoService service;

    public TodoController(TodoService service) {
        this.service = service;
    }

//    Annotation: GetMapping{id}, DeleteMapping, PutMapping, PostMapping
    @GetMapping
    public List<Todo> getall(){
        return service.getAll();
    }

//    @GetMapping("/{id}")
//    public Todo getByID(@PathVariable int id){
//        return service.getByID(id).orElse(null);
//    }
    @GetMapping("/{id}")
    public ResponseEntity<Todo> getByID(@PathVariable int id){
        return service.getByID(id)
            .map(ResponseEntity::ok)            // 200 OK nếu có
            .orElse(ResponseEntity.notFound().build());      // 404 Not Found nếu không có
    }
    @PostMapping()
    public Todo create(@RequestBody Todo todo){
        return service.save(todo);
    }

//    @PutMapping("/{id}")
//    public Todo update(@PathVariable int id, @RequestBody Todo todo){
//        todo.setId(id);
//        return service.save(todo);
//    }
    @PutMapping("/{id}")
    public ResponseEntity<Todo> update(@PathVariable int id, @RequestBody Todo todo) {
        return service.getByID(id)
                .map(existingTodo -> {
                    existingTodo.setTitle(todo.getTitle());
                    existingTodo.setCompleted(todo.getCompleted());
                    existingTodo.setDescription(todo.getDescription());
                    return ResponseEntity.ok(service.save(existingTodo));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void deleteByID(@PathVariable int id){
        service.delete(id);
    }


}
