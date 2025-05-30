package com.example.naver.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class ChatRoom {

    private String markId;

    public static ChatRoom create(String markId) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.markId = markId;
        return chatRoom;
    }
}
