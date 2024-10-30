package com.example.naver.controller;

import com.example.naver.entity.SellerInformation;
import com.example.naver.service.SellerInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seller")
public class SellerInformationApiController {

    @Autowired
    private SellerInformationService sellerInformationService;


    @GetMapping("/information")
    public ResponseEntity<List<SellerInformation>> getInformation() {
        List<SellerInformation> infoList = sellerInformationService.getAllInformation();
        return ResponseEntity.ok(infoList);
    }

    // 마우스 오른쪽 클릭 시 호출되는 삭제 API
    @DeleteMapping("/information/{id}")
    public ResponseEntity<String> deleteMarker(@PathVariable Integer id) {
        try {
            sellerInformationService.informationDelete(id); // 마커 삭제 메서드 호출
            return ResponseEntity.ok("Marker deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}