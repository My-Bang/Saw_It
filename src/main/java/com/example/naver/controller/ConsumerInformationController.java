package com.example.naver.controller;

import com.example.naver.chat.ChatRoomRepository;
import com.example.naver.entity.ConsumerInformation;
import com.example.naver.login.NaverLoginService;
import com.example.naver.login.vo.NaverLoginProfile;
import com.example.naver.service.ConsumerInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class ConsumerInformationController {

    private final ConsumerInformationService consumerInformationService;
    private final ChatRoomRepository chatRoomRepository;

    @Autowired
    private NaverLoginService naverLoginService;

    @GetMapping("/consumerinformation/write")
    public String informationWriteForm() {
        return "consumerinformation";
    }

    @PostMapping("/consumer/information/writedo")
    public String informationWriteDo(ConsumerInformation information,
                                     @RequestParam("latitude") String latitude,
                                     @RequestParam("longitude") String longitude,
                                     MultipartFile file,
                                     Model model,
                                     HttpSession session) throws Exception {
        // 로그인한 사용자의 이메일 가져오기
        String userEmail = naverLoginService.getLastNaverProfile().getEmail();
        NaverLoginProfile loginUser = (NaverLoginProfile) session.getAttribute("loginUser");
        Long userId = loginUser.getId();

        // 위도 및 경도 설정
        try {
            information.setLatitude(latitude != null && !latitude.trim().isEmpty() ? Double.parseDouble(latitude) : 0.0);
        } catch (NumberFormatException e) {
            information.setLatitude(0.0);
            System.err.println("Error parsing latitude: " + e.getMessage());
        }

        try {
            information.setLongitude(longitude != null && !longitude.trim().isEmpty() ? Double.parseDouble(longitude) : 0.0);
        } catch (NumberFormatException e) {
            information.setLongitude(0.0);
            System.err.println("Error parsing longitude: " + e.getMessage());
        }

        // Consumer 정보 저장 및 ChatRoom 생성
        Integer id = consumerInformationService.write(information, userEmail, file);
        String markId = "consumer_" + id;
        model.addAttribute("markId", markId);
        chatRoomRepository.createChatRoom(markId, String.valueOf(userId));
        model.addAttribute("message", "정보가 성공적으로 등록되었습니다.");
        return "redirect:/board/map.html";
    }
}