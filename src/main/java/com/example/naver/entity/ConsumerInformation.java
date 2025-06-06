package com.example.naver.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
public class ConsumerInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String userName;
    private String title;
    private String content;
    private String email;
    private String filename;
    private String filepath;
    private double latitude;
    private double longitude;
    private LocalDateTime postedAt;

    private boolean deleted = false; // 마커 삭제 상태를 나타내는 필드
}