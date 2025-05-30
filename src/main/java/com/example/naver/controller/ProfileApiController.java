package com.example.naver.controller;

import com.example.naver.login.NaverLoginService;
import com.example.naver.login.vo.NaverLoginProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
public class ProfileApiController {

    @Autowired
    private NaverLoginService naverLoginService;

    @GetMapping("/email")
    public ResponseEntity<String> getUserEmail() {
        // 최근에 저장된 네이버 프로필에서 이메일 가져오기
        NaverLoginProfile lastProfile = naverLoginService.getLastNaverProfile();

        if (lastProfile != null && lastProfile.getEmail() != null) {
            return ResponseEntity.ok(lastProfile.getEmail());
        } else {
            return ResponseEntity.badRequest().body("이메일 정보가 없습니다");
        }
    }
}
