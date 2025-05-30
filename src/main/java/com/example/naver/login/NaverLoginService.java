package com.example.naver.login;

import com.example.naver.entity.SellerInformation;
import com.example.naver.login.vo.NaverLoginProfile;
import com.example.naver.login.vo.NaverLoginProfileResponse;
import com.example.naver.login.vo.NaverLoginVo;
import com.example.naver.repository.SellerInformationRepository;
import com.example.naver.login.NaverLoginProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class NaverLoginService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private NaverLoginProfileRepository naverLoginProfileRepository;

    @Autowired
    private SellerInformationRepository sellerInformationRepository;

    @Value("${api.naver.client_id}")
    private String clientId;

    @Value("${api.naver.client_secret}")
    private String clientSecret;

    @Transactional
    public NaverLoginProfile processNaverLogin(Map<String, String> callbackParams) {
        NaverLoginVo naverLoginVo = requestNaverLoginAccessToken(callbackParams);
        NaverLoginProfile naverLoginProfile = requestAndSaveNaverLoginProfile(naverLoginVo);
        createSellerInformation(naverLoginProfile);
        return naverLoginProfile;
    }

    private void createSellerInformation(NaverLoginProfile naverLoginProfile) {
        // 이미 존재하는 이메일인지 확인
        if (sellerInformationRepository.existsByEmail(naverLoginProfile.getEmail())) {
            return;
        }

        SellerInformation sellerInformation = new SellerInformation();
        sellerInformation.setUserName(naverLoginProfile.getName());
        sellerInformation.setEmail(naverLoginProfile.getEmail());
        sellerInformation.setPostedAt(LocalDateTime.now());

        sellerInformationRepository.save(sellerInformation);
    }

    public NaverLoginVo requestNaverLoginAccessToken(Map<String, String> callbackParams) {
        final String code = callbackParams.get("code");
        final String state = callbackParams.get("state");

        String uri = UriComponentsBuilder.fromUriString("https://nid.naver.com/oauth2.0/token")
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", clientId)
                .queryParam("client_secret", clientSecret)
                .queryParam("code", code)
                .queryParam("state", state)
                .build().encode().toUriString();

        return webClient.get().uri(uri)
                .retrieve()
                .bodyToMono(NaverLoginVo.class)
                .block();
    }

    public NaverLoginProfile requestAndSaveNaverLoginProfile(NaverLoginVo naverLoginVo) {
        String profileUri = "https://openapi.naver.com/v1/nid/me";

        NaverLoginProfileResponse profileResponse = webClient.get().uri(profileUri)
                .headers(headers -> headers.setBearerAuth(naverLoginVo.getAccess_token()))
                .retrieve()
                .bodyToMono(NaverLoginProfileResponse.class)
                .block();

        NaverLoginProfileResponse.Response profileData = profileResponse.getResponse();

        LocalDateTime loginTime = LocalDateTime.now();

        // 중복 체크
        NaverLoginProfile existingProfile = naverLoginProfileRepository.findByEmail(profileData.getEmail());
        if (existingProfile != null) {
            return existingProfile;
        }

        NaverLoginProfile naverLoginProfile = NaverLoginProfile.builder()
                .name(profileData.getName())
                .email(profileData.getEmail())
                .gender(profileData.getGender())
                .birthday(profileData.getBirthday())
                .birthyear(profileData.getBirthyear())
                .mobile(profileData.getMobile())
                .loginTime(loginTime)
                .build();

        return naverLoginProfileRepository.save(naverLoginProfile);
    }

    @Transactional(readOnly = true)
    public NaverLoginProfile getLastNaverProfile() {
        List<NaverLoginProfile> profiles = naverLoginProfileRepository.findLatestProfiles();
        if (profiles.isEmpty()) {
            throw new ProfileNotFoundException("No profiles found.");
        } else if (profiles.size() > 1) {
            System.err.println("Warning: Multiple profiles found, returning the first one.");
        }
        return profiles.get(0);
    }
}
