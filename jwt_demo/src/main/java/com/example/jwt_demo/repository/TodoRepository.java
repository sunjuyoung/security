package com.example.jwt_demo.repository;

import com.example.jwt_demo.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo,Long> , TodoSearch{
}
