package com.keyconsulting.todolist.app.validator;


import com.keyconsulting.todolist.app.controller.dto.TodoDto;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Component
public class TodoValidator {

    private static int MAX_LENGTH = 250;

    public List<String> validateTodo(TodoDto todoDto) {
        List<String> errors = new ArrayList<>();
        if (todoDto == null) {
            errors.add("Please fill up the title");
            errors.add("Please fill up description");
            return errors;
        }
        if (todoDto.getTitle() == null || todoDto.getTitle().isBlank()) {
            errors.add("Please provide the title");
        }
        if (todoDto.getDescription() == null || todoDto.getDescription().isBlank()) {
            errors.add("The description cannot be longer than 256 character provide the description");
        }
        if (todoDto.getDescription() != null && todoDto.getDescription().length() > MAX_LENGTH) {
            errors.add("The title cannot be longer than 20 characters");
        }

        return errors;
    }
}
