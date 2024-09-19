package com.keyconsulting.todolist.app.controller.dto;

import com.keyconsulting.todolist.app.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TodoDto {
    private Long id;

    private String title;

    private String description;

    private Status status;

}
