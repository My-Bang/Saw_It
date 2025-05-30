package com.example.naver.login;

import com.example.naver.login.vo.NaverLoginProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class NaverLoginController {

    @Autowired
    private NaverLoginService naverLoginService;

    @GetMapping("/naver/callback")
    public String naverLoginCallback(@RequestParam Map<String, String> callbackParams, Model model, HttpSession session) {


        // 네이버 로그인 서비스를 통해 사용자 프로필 정보를 가져옴
        NaverLoginProfile naverLoginProfile = naverLoginService.processNaverLogin(callbackParams);

        // 가져온 프로필 정보를 모델에 추가
        model.addAttribute("naverProfile", naverLoginProfile);
        session.setAttribute("loginUser", naverLoginProfile);
        // 최종적으로 보여줄 화면 (예: 지도 페이지)
        return "map";
    }

    // 마지막으로 저장된 네이버 프로필 정보를 반환하는 API 엔드포인트
    @GetMapping("/naver/last-profile")
    public ResponseEntity<NaverLoginProfile> getLastNaverProfile() {
        NaverLoginProfile lastProfile = naverLoginService.getLastNaverProfile();
        return ResponseEntity.ok(lastProfile);
    }

    @GetMapping("/naver/userprofile")
    public String profilePage(Model model) {
        // 최근에 저장된 네이버 프로필을 가져와서 모델에 추가
        NaverLoginProfile lastProfile = naverLoginService.getLastNaverProfile();
        model.addAttribute("naverProfile", lastProfile);

        return "profile"; // profile.html을 반환하는 논리적 뷰 이름
    }
}
