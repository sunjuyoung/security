package com.example.jwt_demo.controller;

import com.example.jwt_demo.dto.PageRequestDTO;
import com.example.jwt_demo.dto.PageResponseDTO;
import com.example.jwt_demo.dto.TodoDTO;
import com.example.jwt_demo.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/todo")
public class TodoController {

    private final TodoService todoService;


    @PostMapping(value = "/save",consumes = MediaType.APPLICATION_JSON_VALUE )
    public Map<String,Long> saveTodo(@RequestBody TodoDTO todoDTO){
        log.info(todoDTO);
        Long saveTodo = todoService.saveTodo(todoDTO);
        return Map.of("tno",saveTodo);
    }

    @GetMapping("/read/{tno}")
    public TodoDTO readTodo(@PathVariable("tno") Long tno){
        TodoDTO todoDTO = todoService.readTodo(tno);
        return todoDTO;
    }

    @GetMapping(value = "/list",produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO){
        PageResponseDTO<TodoDTO> pageResponseDTO = todoService.listTodo(pageRequestDTO);
        return pageResponseDTO;
    }


    @DeleteMapping("/{tno}")
    public Map<String , String> deleteTodo(@PathVariable("tno") Long tno){
        todoService.deleteTodo(tno);
        return Map.of("result","succees");
    }


    @PutMapping(value = "/{tno}",consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String , String> modifyTodo(@PathVariable("tno") Long tno,@RequestBody TodoDTO todoDTO){
        todoDTO.setTno(tno);
        todoService.modifyTodo(todoDTO);
        return Map.of("result","succees");
    }


}
