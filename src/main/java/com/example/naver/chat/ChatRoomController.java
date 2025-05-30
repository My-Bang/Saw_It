package com.example.naver.chat;

import com.example.naver.login.vo.NaverLoginProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Log4j2
@Controller
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;

    @PostMapping("/room")
    @ResponseBody
    public ChatRoom createRoom(@RequestParam String markId, HttpSession session) {
        NaverLoginProfile loginUser = (NaverLoginProfile) session.getAttribute("loginUser");
        return chatRoomRepository.createChatRoom(markId, String.valueOf(loginUser.getId()));
    }

    @GetMapping("/room/enter/{markId}")
    public String roomDetail(Model model, @PathVariable String markId) {
        model.addAttribute("markId", markId);
        return "chat/roomdetail";
    }


    @GetMapping("/chat/room/{markId}")
    public String showChatPage(@PathVariable String markId, Model model) {
        model.addAttribute("markId", markId);
        return "chat";
    }
}