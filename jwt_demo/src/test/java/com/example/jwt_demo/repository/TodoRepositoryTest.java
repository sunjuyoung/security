package com.example.jwt_demo.repository;

import com.example.jwt_demo.domain.Todo;
import com.example.jwt_demo.dto.PageRequestDTO;
import com.example.jwt_demo.dto.TodoDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
@SpringBootTest
class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    @Test
    public void test1(){
        IntStream.rangeClosed(1,100).forEach(i->{
            Todo todo = Todo.builder()
                    .complete(false)
                    .dueDate(LocalDate.now())
                    .title("todo title.."+i)
                    .writer("apiUser"+(i%10))
                    .build();
            todoRepository.save(todo);
        });
    }

    @Test
    public void pageTest1(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .keyword("2")
                .build();
        Page<TodoDTO> result = todoRepository.list(pageRequestDTO);
        List<TodoDTO> content = result.getContent();
        for (TodoDTO todoDTO : content) {
            log.info(todoDTO);
        }
        log.info(result.getTotalElements());
    }

}