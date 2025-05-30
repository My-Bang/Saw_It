package com.example.naver.login.vo;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class NaverLoginProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String gender;
    private String birthday;
    private String birthyear;
    private String mobile;

    // 접속 시각 필드 추가
    private LocalDateTime loginTime;
}
