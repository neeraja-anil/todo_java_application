package com.takeaway.todo.services;

import com.takeaway.todo.dao.ProjectRepository;
import com.takeaway.todo.repo.Project;
import com.takeaway.todo.repo.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;


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

    public Project updateProject(Project project, HttpSession session){
        User currentUser = (User) session.getAttribute("user");
        Optional<Project> tempProject = projectRepository.findById(project.getProjectId());

        if (tempProject.isPresent()) {
            Project existingProject = tempProject.get();

            existingProject.setUpdatedAt(Instant.now());
            existingProject.setProjectName(project.getProjectName());

            return projectRepository.save(existingProject);
        } else {
            throw new RuntimeException("Project with ID " + project.getProjectId() + " not found");
        }
    }

    public Project getUserProjects(Long projectId,HttpSession session){
        User currentUser = (User) session.getAttribute("user");
        Long userId = currentUser.getId();
        Optional<Project> projectWithId = projectRepository.findById(projectId);
        if(projectWithId.isPresent()){
            Project project = projectWithId.get();
            if (!project.getUser().getId().equals(userId)) {
                throw new RuntimeException("Unauthorized access to project");
            }
            return project;
        } else {
            throw new RuntimeException("Project with ID " + projectId + " not found");
        }
    }

    public List<Project> getAllUserProjects(HttpSession session){
        User currentUser = (User) session.getAttribute("user");
        Long userId = currentUser.getId();
        return projectRepository.findByUserid(userId);
    }

    public void deleteUserProject(Long projectId, HttpSession session){
        User currentUser = (User) session.getAttribute("user");
        Long userId =currentUser.getId();
        Optional<Project> tempProject = projectRepository.findById(projectId);

        if (tempProject.isPresent()) {
            Project existingProject = tempProject.get();
            if (!existingProject.getUser().getId().equals(userId)) {
                throw new RuntimeException("Unauthorized access request to project");
            }

             projectRepository.delete(existingProject);
        } else {
            throw new RuntimeException("Project with ID " + projectId+ " not found");
        }
    }

}
