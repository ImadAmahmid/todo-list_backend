package com.keyconsulting.todolist.app.exception.hanlder;

import com.keyconsulting.todolist.app.controller.dto.ErrorDto;
import com.keyconsulting.todolist.app.exception.InvalidEntityException;
import com.keyconsulting.todolist.app.exception.NotFoundException;
import com.keyconsulting.todolist.app.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@RestControllerAdvice
@CrossOrigin
@Slf4j
public class ControllerAdvice {


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDto> handleException(NotFoundException exception, WebRequest webRequest) {
        LOG.error("", exception);

        final HttpStatus notFound = HttpStatus.NOT_FOUND;
        final ErrorDto errorDto = ErrorDto.builder()
                .httpCode(HttpStatus.NOT_FOUND.value())
                .message(exception.getMessage())
                .errors(List.of(exception.getMessage()))
                .build();

        return new ResponseEntity<>(errorDto, notFound);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorDto> handleException(UnauthorizedException exception, WebRequest webRequest) {
        LOG.error("", exception);

        final HttpStatus notFound = HttpStatus.BAD_REQUEST;
        final ErrorDto errorDto = ErrorDto.builder()
                .httpCode(HttpStatus.UNAUTHORIZED.value())
                .message(exception.getMessage())
                .errors(List.of(exception.getMessage()))
                .build();

        return new ResponseEntity<>(errorDto, notFound);
    }

    @ExceptionHandler(InvalidEntityException.class)
    public ResponseEntity<ErrorDto> handleException(InvalidEntityException exception, WebRequest webRequest) {
        LOG.error("", exception);

        final HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        final ErrorDto errorDto = ErrorDto.builder()
                .code(exception.getErrorCode())
                .httpCode(badRequest.value())
                .message(exception.getMessage())
                .errors(exception.getErrors())
                .build();

        return new ResponseEntity<>(errorDto, badRequest);
    }

}
