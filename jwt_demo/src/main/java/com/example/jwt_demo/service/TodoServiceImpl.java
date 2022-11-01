package com.example.jwt_demo.service;

import com.example.jwt_demo.domain.Todo;
import com.example.jwt_demo.dto.PageRequestDTO;
import com.example.jwt_demo.dto.PageResponseDTO;
import com.example.jwt_demo.dto.TodoDTO;
import com.example.jwt_demo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.modelmbean.ModelMBean;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService{

    private final TodoRepository todoRepository;
    private final ModelMapper modelMapper;

    @Override
    public Long saveTodo(TodoDTO todoDTO){
        Todo todo = modelMapper.map(todoDTO, Todo.class);
        return todoRepository.save(todo).getTno();
    }

    @Override
    public PageResponseDTO<TodoDTO> listTodo(PageRequestDTO pageRequestDTO){
        Page<TodoDTO> list = todoRepository.list(pageRequestDTO);

        PageResponseDTO<TodoDTO> pageResponseDTO = PageResponseDTO.<TodoDTO>withAll()
                .dtoList(list.getContent())
                .pageRequestDTO(pageRequestDTO)
                .total((int)list.getTotalElements())
                .build();

        return pageResponseDTO;
    }

    @Override
    public TodoDTO readTodo(Long tno) {
        Todo todo = todoRepository.findById(tno).
                orElseThrow(() -> new UsernameNotFoundException("not fond todo"));
        TodoDTO todod = modelMapper.map(todo, TodoDTO.class);
        return todod;
    }

    @Override
    public void deleteTodo(Long tno) {
        todoRepository.deleteById(tno);
    }

    @Transactional
    @Override
    public void modifyTodo(TodoDTO todoDTO) {
        Todo todo = todoRepository.findById(todoDTO.getTno()).orElseThrow();
        todo.changeTitle(todoDTO.getTitle());
        todo.changeComplete(todoDTO.isComplete());

    }
}
