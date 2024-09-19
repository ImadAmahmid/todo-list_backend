package com.keyconsulting.todolist.app.mapper;


import com.keyconsulting.todolist.app.controller.dto.TodoDto;
import com.keyconsulting.todolist.app.entity.TodoItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TodoDtoToItemMapper {
    TodoDtoToItemMapper MAPPER = Mappers.getMapper(TodoDtoToItemMapper.class);

    TodoItem map(TodoDto operation);


}
