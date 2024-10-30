package com.example.naver.login;

import com.example.naver.login.vo.NaverLoginProfile;
import com.example.naver.login.vo.NaverLoginProfileResponse;
import com.example.naver.login.vo.NaverLoginVo;
import com.example.naver.repository.InformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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



    @Value("${api.naver.client_id}")
    private String clientId;

    @Value("${api.naver.client_secret}")
    private String clientSecret;

    @Autowired
    private InformationRepository informationRepository; // InformationRepository 주입


    @Transactional
    public NaverLoginProfile processNaverLogin(Map<String, String> callbackParams) {
        NaverLoginVo naverLoginVo = requestNaverLoginAccessToken(callbackParams);
        return requestAndSaveNaverLoginProfile(naverLoginVo);
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
        Pageable pageable = PageRequest.of(0, 1); // 한 개의 프로필만 요청
        List<NaverLoginProfile> profiles = naverLoginProfileRepository.findLatestProfiles(pageable);
        if (profiles.isEmpty()) {
            throw new ProfileNotFoundException("No profiles found.");
        }
        return profiles.get(0);
    }

    public void someMethod() {
        List<NaverLoginProfile> profiles = naverLoginProfileRepository.findLatestProfiles(PageRequest.of(0, 5)); // 최근 5개 프로필 가져오기
        for (NaverLoginProfile profile : profiles) {
            System.out.println("Latest Profile: " + profile.getEmail());
        }
    }

    public List<NaverLoginProfile> getLatestProfiles() {
        Pageable pageable = PageRequest.of(0, 10); // 예: 첫 번째 페이지, 10개 항목
        return naverLoginProfileRepository.findLatestProfiles(pageable);
    }



}
