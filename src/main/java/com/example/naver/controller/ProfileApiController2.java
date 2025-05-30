package com.example.naver.controller;

import com.example.naver.login.vo.NaverLoginProfile;
import com.example.naver.login.NaverLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileApiController2 {

    @Autowired
    private NaverLoginService naverLoginService;

    // 마커 개수를 저장할 변수 (서버에 저장된 마커 개수)
    private int markerCount = 0;

    @GetMapping("/email2")
    public ResponseEntity<String> getUserEmail() {
        // 최근에 저장된 네이버 프로필에서 이메일 가져오기
        NaverLoginProfile lastProfile = naverLoginService.getLastNaverProfile();

        if (lastProfile != null && lastProfile.getEmail() != null) {
            return ResponseEntity.ok(lastProfile.getEmail());
        } else {
            return ResponseEntity.badRequest().body("이메일 정보가 없습니다");
        }
    }

    // 클라이언트에서 마커 개수를 업데이트하는 API
    @PostMapping("/updateMarkerCount")
    public ResponseEntity<?> updateMarkerCount(@RequestBody int count) {
        // 클라이언트에서 받은 마커 개수를 저장
        this.markerCount = count;
        return ResponseEntity.ok("마커 개수 업데이트 성공");
    }

    // 마커 개수를 가져오는 API
    @GetMapping("/getMarkerCount")
    public ResponseEntity<Integer> getMarkerCount() {
        return ResponseEntity.ok(this.markerCount);
    }
}
