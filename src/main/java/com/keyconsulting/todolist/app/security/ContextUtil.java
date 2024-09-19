package com.keyconsulting.todolist.app.security;

import com.keyconsulting.todolist.app.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ContextUtil {

    public User getCurrentUserFromContext(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
