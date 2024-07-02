package com.takeaway.todo.repo;


import jakarta.persistence.*;

@Entity
@Table(name = "projects")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    private Long todoId;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;




}
