package com.takeaway.todo.services;

import com.takeaway.todo.dao.ProjectRepository;
import com.takeaway.todo.dao.TodoRepository;
import com.takeaway.todo.dto.TodoDTO;
import com.takeaway.todo.repo.Project;
import com.takeaway.todo.repo.Status;
import com.takeaway.todo.repo.Todo;
import com.takeaway.todo.repo.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;


@Service
public class TodoService {
    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public Todo addNewTodo(TodoDTO todoDTO, HttpSession session){
        User currentUser = (User) session.getAttribute("user");
        Project project = projectRepository.findById(todoDTO.getProjectId()).orElseThrow(() -> new RuntimeException("Project not found"));
        Todo todo = new Todo();

        todo.setProject(project);
        todo.setTodoName(todoDTO.getTodoName());
        todo.setTodoDesc(todoDTO.getTodoDesc());
        todo.setStatus(Status.PENDING);
        todo.setCreatedAt(Instant.now());
        return todoRepository.save(todo);

    }

    public Todo updateTodo(TodoDTO todoDTO, HttpSession session){
        User currentUser = (User) session.getAttribute("user");
        Project project = projectRepository.findById(todoDTO.getProjectId()).orElseThrow(() -> new RuntimeException("Project not found"));
        Optional<Todo> existingTodo = todoRepository.findById(todoDTO.getId());

        if(existingTodo.isPresent()){
            Todo todo = existingTodo.get();
            todo.setTodoName(todoDTO.getTodoName());
            todo.setTodoDesc(todoDTO.getTodoDesc());
            todo.setStatus(todoDTO.getStatus() == null ? (Status) todo.getStatus() : todoDTO.getStatus());
            todo.setUpdatedAt(Instant.now());

            return todoRepository.save(todo);
        } else {
            throw new RuntimeException("Todo with id " + todoDTO.getId() + " not found");
        }


    }

    public Todo getTodo(Long todoId,HttpSession session){
        User currentUser = (User) session.getAttribute("user");
        Long userId = currentUser.getId();
        Optional<Todo> todoWithId = todoRepository.findById(todoId);
        if(todoWithId.isPresent()){
            Todo todo = todoWithId.get();
            if (!todo.getProject().getUser().getId().equals(userId)) {
                throw new RuntimeException("Unauthorized access to project");
            }
            return todo;
        } else {
            throw new RuntimeException("Todo with ID " + todoId + " not found");
        }
    }
//
//    public List<Todo> getAllTodos(Long projectId,HttpSession session){
//        User currentUser = (User) session.getAttribute("user");
//        Long userId = currentUser.getId();
//        return todoRepository.findByProjectId(projectId);
//
//    }
//
    public void deleteTodo(Long todoId, HttpSession session){
        User currentUser = (User) session.getAttribute("user");
        Long userId =currentUser.getId();
        Optional<Todo> tempTodo = todoRepository.findById(todoId);

        if (tempTodo.isPresent()) {
            Todo existingTodo = tempTodo.get();
            if (!existingTodo.getProject().getUser().getId().equals(userId)) {
                throw new RuntimeException("Unauthorized access request to project");
            }

             todoRepository.delete(existingTodo);
        } else {
            throw new RuntimeException("Todo with ID " + todoId + " not found");
        }
    }

}
