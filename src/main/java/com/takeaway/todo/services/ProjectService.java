package com.takeaway.todo.services;

import com.takeaway.todo.dao.ProjectRepository;
import com.takeaway.todo.repo.Project;
import com.takeaway.todo.repo.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;


@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    public Project addNewProject(Project project, HttpSession session){
        User currentUser = (User) session.getAttribute("user");
        project.setUser(currentUser);
        project.setCreatedAt(Instant.now());
        return projectRepository.save(project);

    }
}
