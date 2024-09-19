package com.keyconsulting.todolist.app.repo;

import com.keyconsulting.todolist.app.entity.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<TodoItem, Long> {
    List<TodoItem> findAllByUserId(Long userId);

}
