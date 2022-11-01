package com.example.jwt_demo.repository;

import com.example.jwt_demo.dto.PageRequestDTO;
import com.example.jwt_demo.dto.TodoDTO;
import org.springframework.data.domain.Page;

public interface TodoSearch {

    Page<TodoDTO> list(PageRequestDTO pageRequestDTO);
}
