package com.example.jwt_demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "todo_api")
public class Todo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tno;

    private String title;

    private String writer;

    private LocalDate dueDate;

    private boolean complete;


    public void changeTitle(String title){
        this.title = title;
    }
    public void changeComplete(boolean complete){
        this.complete = complete;
    }

    public void settingDueDate(){
        this.dueDate = LocalDate.now();
    }

}
