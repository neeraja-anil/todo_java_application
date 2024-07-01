package com.takeaway.todo.controllers;


import com.takeaway.todo.repo.Project;
import com.takeaway.todo.services.ProjectService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    HttpSession session;

    @PostMapping("/projects")
    public ResponseEntity<?> addProject(@RequestBody Project project){
        try{
            if(project.getProjectName() == null){
                return ResponseEntity.badRequest().body("Please add all fields");
            }
            if(session.getAttribute("user") != null) {
                Project projectResponse = projectService.addNewProject(project,session);
                System.out.println(projectResponse);
                return ResponseEntity.status(201).body(projectResponse);
            } else {
                return new ResponseEntity<>("User Not Logged In", HttpStatus.BAD_REQUEST);
            }

        }
        catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
