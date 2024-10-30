package com.example.naver.login.vo;

import com.example.naver.login.vo.NaverLoginProfile;
import lombok.Data;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
public class NaverLoginProfileResponse {

    // API 호출 결과 코드
    private String resultcode;

    // 호출 결과 메시지
    private String message;

    // Profile 상세
    private Response response;

    @Data
    public static class Response {
        private String id;
        private String name;
        @ManyToOne
        @JoinColumn(name = "created_by", referencedColumnName = "email")
        private String email;
        private String gender;
        private String birthday;
        private String birthyear;
        private String mobile;
    }
}
