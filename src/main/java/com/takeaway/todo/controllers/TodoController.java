package com.takeaway.todo.controllers;


import com.takeaway.todo.dto.TodoDTO;
import com.takeaway.todo.repo.Todo;
import com.takeaway.todo.services.TodoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @Autowired
    HttpSession session;

    @PostMapping("/todo")
    public ResponseEntity<?> addTodo(@RequestBody TodoDTO todo){
        try{
            if(todo.getTodoName() == null){
                return ResponseEntity.badRequest().body("Please add all fields");
            }
            if(session.getAttribute("user") != null) {
                Todo todoResponse = todoService.addNewTodo(todo,session);
                return ResponseEntity.status(201).body(todoResponse);
            } else {
                return new ResponseEntity<>("User Not Logged In", HttpStatus.BAD_REQUEST);
            }

        }
        catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/todo")
    public ResponseEntity<?> updateTodo(@RequestBody TodoDTO todo){
        try{
            if(todo.getId() == null ){
                return ResponseEntity.badRequest().body("Please provide todo Id");
            }
            if(session.getAttribute("user") != null) {
                Todo todoResponse = todoService.updateTodo(todo,session);
                return ResponseEntity.status(201).body(todoResponse);
            } else {
                return new ResponseEntity<>("User Not Logged In", HttpStatus.BAD_REQUEST);
            }

        }
        catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//    @GetMapping("/todo")
//    public ResponseEntity<?> getAllTodo(Long projectId,HttpSession session){
//        try {
//            List<Todo> response = todoService.getAllTodos(projectId,session);
//            return ResponseEntity.status(200).body(response);
//        }
//        catch (Exception e){
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }

    @GetMapping("/todo/{todoId}")
    public ResponseEntity<?> getProject(@PathVariable Long todoId, HttpSession session){
        try {
            Todo response = todoService.getTodo(todoId,session);
            return ResponseEntity.status(200).body(response);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/todo/{todoId}")
    public ResponseEntity<?> deleteTodo(@PathVariable Long todoId, HttpSession session){
        try {
             todoService.deleteTodo(todoId,session);
            return ResponseEntity.status(200).body("Todo delete success");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
