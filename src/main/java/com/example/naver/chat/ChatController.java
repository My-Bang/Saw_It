package com.example.naver.chat;

import com.example.naver.login.vo.NaverLoginProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class ChatController {


    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatRoomRepository chatRoomRepository;

    @MessageMapping("/chat/message")
    public void message(ChatMessage message, SimpMessageHeaderAccessor headerAccessor) {
        HttpSession session = (HttpSession) headerAccessor.getSessionAttributes().get("HTTP_SESSION");
        if (session == null) {
            throw new RuntimeException("세션이 존재하지 않습니다.");
        }
        NaverLoginProfile loginUser = (NaverLoginProfile) session.getAttribute("loginUser");

        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            try {
                if (chatRoomRepository.isFull(message.getRoomId())) {
                    sendErrorMessage(message, "방이 꽉 찼습니다. 다른 방으로 이동하세요.");
                    return;
                }
                chatRoomRepository.enterRoom(message.getRoomId(), String.valueOf(loginUser.getId()));
                message.setMessage(message.getSender() + "님이 입장하셨습니다.");
            } catch (RuntimeException e) {
                sendErrorMessage(message, "존재하지 않는 방입니다.");
                throw new RuntimeException("존재하지 않는 방입니다.");
            }
        }
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    private void sendErrorMessage(ChatMessage message, String errorMsg) {
        ChatMessage errorMessage = new ChatMessage();
        errorMessage.setType(ChatMessage.MessageType.FULL);
        errorMessage.setRoomId(message.getRoomId());
        errorMessage.setId(message.getId());
        errorMessage.setMessage(errorMsg);
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), errorMessage);
    }

}