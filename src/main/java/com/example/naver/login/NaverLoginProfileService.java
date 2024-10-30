package com.example.naver.login;

import com.example.naver.login.vo.NaverLoginProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class NaverLoginProfileService {

    @Autowired
    private NaverLoginProfileRepository naverLoginProfileRepository;

    public NaverLoginProfile saveOrUpdateProfile(NaverLoginProfile profile) {
        Optional<NaverLoginProfile> existingProfile = Optional.ofNullable(naverLoginProfileRepository.findByEmail(profile.getEmail()));

        if (existingProfile.isPresent()) {
            // 이메일이 중복된 경우
            throw new IllegalArgumentException("Email already exists");
        } else {
            // 중복되지 않은 경우 저장
            return naverLoginProfileRepository.save(profile);
        }
    }
}
