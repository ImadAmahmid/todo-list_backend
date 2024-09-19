package com.keyconsulting.todolist.app.exception;


public enum ErrorCodes {
    TODO_NOT_VALID(1001),
    TODO_NOT_FOUND(1002)
    ;

    private int code;

    ErrorCodes(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}