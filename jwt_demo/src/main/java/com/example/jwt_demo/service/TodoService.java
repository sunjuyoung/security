package com.example.jwt_demo.service;

import com.example.jwt_demo.dto.PageRequestDTO;
import com.example.jwt_demo.dto.PageResponseDTO;
import com.example.jwt_demo.dto.TodoDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TodoService {

    public Long saveTodo(TodoDTO todoDTO);

    public PageResponseDTO<TodoDTO> listTodo(PageRequestDTO pageRequestDTO);

    public TodoDTO readTodo(Long tno);

    public void deleteTodo(Long tno);

    public void modifyTodo(TodoDTO todoDTO);
}
