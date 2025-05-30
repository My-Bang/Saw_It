package com.example.naver.chat;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;

@Repository
public class ChatRoomRepository {

    private Map<String, ChatRoom> chatRoomMap;
    private Map<String, RoomInfo> roomInfo;


    @PostConstruct
    private void init() {
        chatRoomMap = Collections.synchronizedMap(new LinkedHashMap<>());
        roomInfo = Collections.synchronizedMap(new LinkedHashMap<>());
    }

    public ChatRoom createChatRoom(String markId, String sellerId) {
        ChatRoom chatRoom = new ChatRoom().create(markId);
        chatRoomMap.put(markId, chatRoom);
        roomInfo.put(markId, new RoomInfo(sellerId));
        return chatRoom;
    }

    public boolean isFull(String markId) {
        if (!chatRoomMap.containsKey(markId)) {
            throw new RuntimeException("존재하지 않는 방입니다.");
        }
        if (roomInfo.containsKey(markId)) {
            RoomInfo rooms = roomInfo.get(markId);
            if (rooms.getSellerId() != null && rooms.getBuyerId() != null) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void enterRoom(String markId, String buyerId) {
        if (!chatRoomMap.containsKey(markId)) {
            throw new RuntimeException("존재하지 않는 방입니다.");
        }
        RoomInfo rooms = roomInfo.get(markId);
        if (rooms == null || rooms.getSellerId() == null) {
            throw new RuntimeException("판매자가 없습니다.");
        }
        if (rooms.getSellerId().equals(buyerId)) {
            return;
        }
        if (rooms.getBuyerId() == null) {
            rooms.setBuyerId(buyerId);
        } else if (!rooms.getBuyerId().equals(buyerId)) {
            throw new RuntimeException("방이 꽉 찼습니다.");
        }
    }
    @Getter
    @Setter
    private static final class RoomInfo {
        private final String sellerId;
        private String buyerId;

        private RoomInfo(String sellerId) {
            this.sellerId = sellerId;
        }
    }
}