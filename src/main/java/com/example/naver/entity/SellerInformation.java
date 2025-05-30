package com.example.naver.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Setter
@Getter
public class SellerInformation {

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
