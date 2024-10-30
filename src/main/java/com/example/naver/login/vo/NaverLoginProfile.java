package com.example.naver.login.vo;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
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
    @Column(unique = true)  // 유니크 제약 조건
    private String email;  // email 컬럼이 String 타입으로 정의됨
    private String gender;
    private String birthday;
    private String birthyear;
    private String mobile;

    private LocalDateTime loginTime;
    public NaverLoginProfile(String email) {
        this.email = email;
    }
}
