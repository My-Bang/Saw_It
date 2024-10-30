package com.example.naver.entity;

import com.example.naver.login.vo.NaverLoginProfile;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Data
@Table(name = "information")
public class Information {

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
}
