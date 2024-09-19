package com.keyconsulting.todolist.app.service.impl;

import com.keyconsulting.todolist.app.controller.dto.TodoDto;
import com.keyconsulting.todolist.app.entity.TodoItem;
import com.keyconsulting.todolist.app.entity.User;
import com.keyconsulting.todolist.app.enums.Status;
import com.keyconsulting.todolist.app.exception.InvalidEntityException;
import com.keyconsulting.todolist.app.exception.NotFoundException;
import com.keyconsulting.todolist.app.exception.UnauthorizedException;
import com.keyconsulting.todolist.app.repo.TodoRepository;
import com.keyconsulting.todolist.app.security.ContextUtil;
import com.keyconsulting.todolist.app.validator.TodoValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


/**
 * Unit test sample
 */
@ExtendWith(MockitoExtension.class)
class TodoServiceImplTest {

    private static final String USER_EMAIL1 = "user1@gmail.com";
    private static final String USER_EMAIL2 = "user2@gmail.com";
    private static final String PASSWORD1 = "DummyPass";
    private static final String DTO_TITLE1 = "Title 1";
    private static final String DTO_DESC1 = "Desc 1";
    public static final long TODO_ID = 1l;
    public static final long USER_ID1 = 8l;
    public static final long USER_ID2 = 2l;


    @Mock
    private TodoRepository todoRepository;
    @Mock
    private ContextUtil contextUtil;
    @Mock
    private TodoValidator todoValidator;

    @InjectMocks
    TodoServiceImpl todoService;


    @Captor
    ArgumentCaptor<Long> todoIdCaptor;

    @Nested
    class CreateOrUpdateTodo {

        @Test
        @DisplayName("Test creation of valid entity")
        public void createDto_success() {
            TodoDto dto = TodoDto.builder().description(DTO_DESC1).title(DTO_TITLE1).build();
            TodoItem item = TodoItem.builder().status(Status.NEW).description(DTO_DESC1).title(DTO_TITLE1).build();

            when(todoValidator.validateTodo(eq(dto))).thenReturn(Collections.emptyList());
            when(todoRepository.save(eq(item))).thenReturn(item);

            TodoDto response = todoService.createOrUpdateTodo(dto);

            assertThat(response.getTitle()).isEqualTo(DTO_TITLE1);
        }

        @Test
        @DisplayName("Test creation of not valid entity")
        public void createDto_validationFailure() {
            TodoDto dto = TodoDto.builder().description(DTO_DESC1).title(DTO_TITLE1).build();

            when(todoValidator.validateTodo(eq(dto))).thenReturn(Collections.singletonList("Error"));

            assertThrows(InvalidEntityException.class, () -> todoService.createOrUpdateTodo(dto));

            verify(todoRepository, never()).save(any());
        }

    }


    @Nested
    class DeleteTodo {
        @Test
        @DisplayName("Test deletion of valid entity")
        public void delete_success() {
            TodoItem item = TodoItem.builder().status(Status.NEW).description(DTO_DESC1).title(DTO_TITLE1).user(User.builder().id(USER_ID1).build()).build();

            when(todoRepository.findById(eq(TODO_ID))).thenReturn(Optional.of(item));
            when(contextUtil.getCurrentUserFromContext()).thenReturn(User.builder().id(USER_ID1).build());

            todoService.deleteTodo(TODO_ID);

            verify(todoRepository).deleteById(todoIdCaptor.capture());
            assertThat(todoIdCaptor.getValue()).isEqualTo(TODO_ID);
        }

        @Test
        @DisplayName("Test deletion of non existing todo")
        public void delete_failedNotFound() {
            when(todoRepository.findById(eq(TODO_ID))).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> todoService.deleteTodo(TODO_ID));

            verify(todoRepository, never()).save(any());
        }

        @Test
        @DisplayName("Test deletion of another user's entity")
        public void delete_failedUnauthorized() {
            TodoItem item = TodoItem.builder().status(Status.NEW).description(DTO_DESC1).title(DTO_TITLE1).user(User.builder().id(USER_ID1).build()).build();

            when(todoRepository.findById(eq(TODO_ID))).thenReturn(Optional.of(item));
            when(contextUtil.getCurrentUserFromContext()).thenReturn(User.builder().id(USER_ID2).build());

            assertThrows(UnauthorizedException.class, () -> todoService.deleteTodo(TODO_ID));

            verify(todoRepository, never()).save(any());
        }
    }

}