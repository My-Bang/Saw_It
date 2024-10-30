package com.example.naver.chat;

import lombok. Getter;
import lombok. Setter;

@Getter
@Setter
public class ChatMessage {
// 메시지 타입 : 입장 , 채탕
    public enum MessageType {

    ENTER, TALK
}
        private MessageType type; //메시지 타입

        private String roomId; //

        private String sender; //메시지 보낸 사람

        private String message; //C|A|*|
        }