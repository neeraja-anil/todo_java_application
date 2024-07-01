package com.takeaway.todo.dao;

import com.takeaway.todo.repo.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {
    @Query(value = "SELECT * FROM projects q WHERE q.user_id=:user",nativeQuery = true)
    List<Project> findByUserid(Long user);
}
