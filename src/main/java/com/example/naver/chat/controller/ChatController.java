package com.example.naver.chat.controller;
import com.example.naver.chat.message.ChatMessage;
import com.example.naver.chat.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class ChatController {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(ChatMessage chatMessage) {
        // 메시지 타임스탬프 설정
        chatMessage.setTimestamp(LocalDateTime.now());

        // 메시지를 데이터베이스에 저장
        chatMessageRepository.save(chatMessage);

        // 저장된 메시지를 반환하여 클라이언트에 전송
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(ChatMessage chatMessage) {
        chatMessage.setTimestamp(LocalDateTime.now());
        return chatMessage;
    }
}
