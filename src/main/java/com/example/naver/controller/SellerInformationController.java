package com.example.naver.controller;

import com.example.naver.chat.ChatRoomRepository;
import com.example.naver.entity.SellerInformation;
import com.example.naver.login.NaverLoginService;
import com.example.naver.login.vo.NaverLoginProfile;
import com.example.naver.service.SellerInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

@Controller
public class SellerInformationController {

    @Autowired
    private SellerInformationService sellerInformationService;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private NaverLoginService naverLoginService;

    @GetMapping("/sellerinformation/write")
    public String informationWriteForm() {
        return "sellerinformation";
    }

    @PostMapping("/seller/information/writedo")
    public String informationWriteDo(SellerInformation information,
                                     @RequestParam("latitude") String latitude,
                                     @RequestParam("longitude") String longitude,
                                     Model model,
                                     MultipartFile file,
                                     HttpSession session) throws Exception {
        // 로그인한 사용자의 이메일 가져오기
        String userEmail = naverLoginService.getLastNaverProfile().getEmail();
        NaverLoginProfile loginUser = (NaverLoginProfile) session.getAttribute("loginUser");
        Long userId = loginUser.getId();
        System.out.println("Latitude from request: " + latitude);
        System.out.println("Longitude from request: " + longitude);

        // 위도 및 경도 설정
        try {
            if (latitude != null && !latitude.trim().isEmpty()) {
                information.setLatitude(Double.parseDouble(latitude));
            } else {
                information.setLatitude(0.0);
            }
        } catch (NumberFormatException e) {
            information.setLatitude(0.0);
            System.err.println("Error parsing latitude: " + e.getMessage());
        }

        try {
            if (longitude != null && !longitude.trim().isEmpty()) {
                information.setLongitude(Double.parseDouble(longitude));
            } else {
                information.setLongitude(0.0);
            }
        } catch (NumberFormatException e) {
            information.setLongitude(0.0);
            System.err.println("Error parsing longitude: " + e.getMessage());
        }

        // 서비스에 이메일과 파일 정보를 전달 및 글 작성
        Integer id = sellerInformationService.write(information, userEmail, file);

        // 마크 ID 생성 및 채팅방 생성
        String markId = "seller_" + id;
        chatRoomRepository.createChatRoom(markId, String.valueOf(userId));

        model.addAttribute("markId", markId);
        model.addAttribute("message", "글 작성이 완료되었습니다.");
        return "redirect:/board/map.html";
    }
}
