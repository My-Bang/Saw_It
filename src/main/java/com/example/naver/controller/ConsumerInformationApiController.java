package com.example.naver.controller;

import com.example.naver.entity.ConsumerInformation;
import com.example.naver.service.ConsumerInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/consumer")
public class ConsumerInformationApiController {

    @Autowired
    private ConsumerInformationService consumerInformationService;

    // 정보 가져오는 API (필터링 추가)
    @GetMapping("/information")
    public ResponseEntity<List<ConsumerInformation>> getInformation(@RequestParam(required = false) String email) {
        List<ConsumerInformation> infoList = consumerInformationService.getAllInformation();

        if (email != null) {
            // 요청한 이메일과 일치하는 소비자 정보만 필터링
            infoList = infoList.stream()
                    .filter(info -> info.getEmail().equals(email))
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(infoList);
    }

    // 마우스 오른쪽 클릭 시 호출되는 삭제 API
    @DeleteMapping("/information/{id}")
    public ResponseEntity<String> deleteMarker(@PathVariable Integer id) {
        try {
            consumerInformationService.informationDelete(id); // 마커 삭제 메서드 호출
            return ResponseEntity.ok("Marker deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}