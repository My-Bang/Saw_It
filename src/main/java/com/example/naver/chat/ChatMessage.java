package com.example.naver.chat;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter

public class ChatMessage {
    // 메시지 타입 : 입장 , 채탕
    public enum MessageType {

        ENTER, TALK, ERROR, FULL, FILE
    }
    private String fileName; // ✅ 이 필드 필수

    private MessageType type; //메시지 타입

    private String roomId; //

    private String sender; //메시지 보낸 사람

    private String message; //C|A|*|

    private String id;
}