package com.keyconsulting.todolist.app.controller;

import com.keyconsulting.todolist.app.controller.dto.TodoDto;
import com.keyconsulting.todolist.app.controller.resource.TodoApi;
import com.keyconsulting.todolist.app.entity.User;
import com.keyconsulting.todolist.app.security.ContextUtil;
import com.keyconsulting.todolist.app.service.TodoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@Slf4j
public class TodoController implements TodoApi {

    private final TodoService todoService;
    private final ContextUtil contextUtil;

    @Override
    public ResponseEntity<TodoDto> updateTodo(TodoDto todoDto) {
        LOG.info("[Todo Controller] Create/Update todo | todoTitle={}", todoDto.getTitle());

        return new ResponseEntity<>(todoService.createOrUpdateTodo(todoDto), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteTodo(Long todoId) {
        LOG.info("[Todo Controller] Delete todo | userEmail={}", todoId);

        todoService.deleteTodo(todoId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<TodoDto>> getAllTodos() {
        User principal = contextUtil.getCurrentUserFromContext();
        LOG.info("[Todo Controller] Get todos for user | userEmail={}", principal.getId());

        return  new ResponseEntity<>(todoService.getTodosByUserId(principal.getId()), HttpStatus.OK);
    }
}
