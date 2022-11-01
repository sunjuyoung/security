package com.example.jwt_demo.repository;

import com.example.jwt_demo.domain.QTodo;
import com.example.jwt_demo.domain.Todo;
import com.example.jwt_demo.dto.PageRequestDTO;
import com.example.jwt_demo.dto.TodoDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class TodoSearchImpl extends QuerydslRepositorySupport implements TodoSearch {

    public TodoSearchImpl() {
        super(Todo.class);
    }

    @Override
    public Page<TodoDTO> list(PageRequestDTO pageRequestDTO) {
        QTodo todo = QTodo.todo;

        JPQLQuery<Todo> query = from(todo);
        if(pageRequestDTO.getFrom() != null && pageRequestDTO.getTo() != null){
            BooleanBuilder builder = new BooleanBuilder();
            builder.and(todo.dueDate.goe(pageRequestDTO.getFrom()));
            builder.and(todo.dueDate.loe(pageRequestDTO.getTo()));
            query.where(builder);
        }

        if(pageRequestDTO.getKeyword() != null){
            query.where(todo.title.containsIgnoreCase(pageRequestDTO.getKeyword()));
        }

        this.getQuerydsl().applyPagination(pageRequestDTO.getPageable("tno"), query);

        JPQLQuery<TodoDTO> dtoQuery = query.select(Projections.bean(TodoDTO.class,
                todo.dueDate,
                todo.title,
                todo.complete,
                todo.tno,
                todo.writer
                ));

        List<TodoDTO> list = dtoQuery.fetch();
        long count = dtoQuery.fetchCount();

        return new PageImpl<>(list,pageRequestDTO.getPageable("tno"), count);
    }
}
