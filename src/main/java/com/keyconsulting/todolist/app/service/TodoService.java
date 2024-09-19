package com.keyconsulting.todolist.app.service;

import com.keyconsulting.todolist.app.controller.dto.TodoDto;

import java.util.List;

public interface TodoService {

    TodoDto createOrUpdateTodo(TodoDto todo);

    void deleteTodo(Long id);

    List<TodoDto> getTodosByUserId(Long userId);
}
