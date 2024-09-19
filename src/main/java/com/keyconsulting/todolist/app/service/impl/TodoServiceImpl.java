package com.keyconsulting.todolist.app.service.impl;

import com.keyconsulting.todolist.app.controller.dto.TodoDto;
import com.keyconsulting.todolist.app.entity.TodoItem;
import com.keyconsulting.todolist.app.enums.Status;
import com.keyconsulting.todolist.app.exception.ErrorCodes;
import com.keyconsulting.todolist.app.exception.InvalidEntityException;
import com.keyconsulting.todolist.app.exception.NotFoundException;
import com.keyconsulting.todolist.app.exception.UnauthorizedException;
import com.keyconsulting.todolist.app.mapper.TodoDtoToItemMapper;
import com.keyconsulting.todolist.app.mapper.TodoItemToDtoMapper;
import com.keyconsulting.todolist.app.repo.TodoRepository;
import com.keyconsulting.todolist.app.security.ContextUtil;
import com.keyconsulting.todolist.app.service.TodoService;
import com.keyconsulting.todolist.app.validator.TodoValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TodoServiceImpl implements TodoService {

    private final TodoValidator todoValidator;
    private final TodoRepository todoRepository;
    private final ContextUtil contextUtil;

    @Override
    public TodoDto createOrUpdateTodo(TodoDto todoDto) {
        List<String> errors = todoValidator.validateTodo(todoDto);
        if (!errors.isEmpty()) {
            LOG.error("Todo is not valid {}", todoDto);
            throw new InvalidEntityException("Todo is not valid", ErrorCodes.TODO_NOT_VALID, errors);
        }

        if (todoDto.getId() == null) {
            todoDto.setStatus(Status.NEW);
        }
        TodoItem todoItem = TodoDtoToItemMapper.MAPPER.map(todoDto);
        todoItem.setUser(contextUtil.getCurrentUserFromContext());
        return TodoItemToDtoMapper.MAPPER.map(todoRepository.save(todoItem));
    }

    @Override
    public void deleteTodo(Long id) {
        Optional<TodoItem> byId = todoRepository.findById(id);
        if (byId.isEmpty()) {
            throw new NotFoundException("Todo is not found");
        } else {
            boolean isTodoBelongToUser = byId.get().getUser().getId().equals(contextUtil.getCurrentUserFromContext().getId());

            if (!isTodoBelongToUser) {
                LOG.error("Todo is not authorized for user | todoId={}", id);
                throw new UnauthorizedException("Todo does not belong to user");
            }
        }
        todoRepository.deleteById(id);

    }

    @Override
    public List<TodoDto> getTodosByUserId(Long userId) {
        return TodoItemToDtoMapper.MAPPER.map(todoRepository.findAllByUserId(userId));
    }

    // todo: get todos by pagination
}
