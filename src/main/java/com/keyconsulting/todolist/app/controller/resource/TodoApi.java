package com.keyconsulting.todolist.app.controller.resource;

import com.keyconsulting.todolist.app.controller.dto.TodoDto;
import com.keyconsulting.todolist.app.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// todo : add this to the web security configuration
@CrossOrigin(origins = "http://localhost:4200")
public interface TodoApi {

    @Operation(
            operationId = "createNewTodo",
            summary = "Create/update new todo",
            description = "Create new Todo List",
            tags = {"Todo"},
            responses = {
                    @ApiResponse(responseCode = "201", description = "Todo successfully created", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = TodoDto.class))
                    })
            },
            security = {
                    @SecurityRequirement(name = "bearer")
            }
    )
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/api/v1/todo/",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    ResponseEntity<TodoDto> updateTodo(@Parameter(name = "Todo DTO", required = true) @RequestBody TodoDto todo);


    @Operation(
            operationId = "deleteTodo",
            summary = "Delete the Todo if exists.",
            tags = {"Todo"},
            responses = {
                    @ApiResponse(responseCode = "204", description = "Todo deleted"),
                    @ApiResponse(responseCode = "404", description = "Todo not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = NotFoundException.class))
                    })
            },
            security = {
                    @SecurityRequirement(name = "bearer")
            }
    )
    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/api/v1/todo/{todoId}",
            produces = {"application/json"}
    )
    ResponseEntity<Void> deleteTodo(@Parameter(name = "todoId", description = "Todo Id", required = true, in = ParameterIn.PATH) @PathVariable("todoId") java.lang.Long todoId);


    @Operation(
            operationId = "getAllTodos",
            summary = "Get all Todos for user",
            tags = {"Todo"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),

            },
            security = {
                    @SecurityRequirement(name = "bearer")
            }
    )
    // Could ideally be placed in a new UserApi
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/api/v1/user/todos",
            produces = {"application/json"}
    )
    ResponseEntity<List<TodoDto>> getAllTodos();
}
