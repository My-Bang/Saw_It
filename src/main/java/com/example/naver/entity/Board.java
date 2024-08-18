package com.example.naver.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity // DB의 Table을 의미
@Data
public class Board {

    @Id // pk를 의미
    @GeneratedValue(strategy = GenerationType.IDENTITY) // mariaDB, MYSQL에서 사용하는 자동생성
    private Integer id;
    private String userName;
    private String title;
    private String content;
    private LocalDateTime postedAt;
    private String filename;
    private String filepath;
}

