package com.example.naver.controller;

import com.example.naver.login.NaverLoginService;
import com.example.naver.login.vo.NaverLoginProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    @Autowired
    private NaverLoginService naverLoginService;

    @GetMapping("/naver/profile")
    public String profilePage(Model model) {
        // 최근에 저장된 네이버 프로필을 가져옴
        NaverLoginProfile lastProfile = naverLoginService.getLastNaverProfile();

        // 프로필이 null일 경우 처리 (예: 기본값 설정, 에러 메시지 추가)
        if (lastProfile == null) {
            lastProfile = new NaverLoginProfile(); // 빈 객체로 초기화
        }

        model.addAttribute("naverProfile", lastProfile);

        return "profile"; // profile.html을 반환
    }
}
