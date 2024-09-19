package com.keyconsulting.todolist.app.mapper;


import com.keyconsulting.todolist.app.controller.dto.TodoDto;
import com.keyconsulting.todolist.app.entity.TodoItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TodoItemToDtoMapper {
    TodoItemToDtoMapper MAPPER = Mappers.getMapper(TodoItemToDtoMapper.class);

    TodoDto map(TodoItem operation);

    List<TodoDto> map(List<TodoItem> operation);


}
