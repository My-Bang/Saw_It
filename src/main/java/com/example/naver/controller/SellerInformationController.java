package com.example.naver.controller;

import com.example.naver.entity.SellerInformation;
import com.example.naver.service.SellerInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class SellerInformationController {

    @Autowired
    private SellerInformationService sellerInformationService;

    @GetMapping("/sellerinformation/write")
    public String informationWriteForm() {
        return "sellerinformation";
    }

    @PostMapping("/seller/information/writedo")
    public String informationWriteDo(SellerInformation information,
                                     @RequestParam("latitude") String latitude,
                                     @RequestParam("longitude") String longitude,
                                     Model model,
                                     MultipartFile file) throws Exception {
        System.out.println("Latitude from request: " + latitude);
        System.out.println("Longitude from request: " + longitude);

        try {
            if (latitude != null && !latitude.trim().isEmpty()) {
                information.setLatitude(Double.parseDouble(latitude));
            } else {
                information.setLatitude(0.0);
            }
        } catch (NumberFormatException e) {
            information.setLatitude(0.0);
            System.err.println("Error parsing latitude: " + e.getMessage());
        }

        try {
            if (longitude != null && !longitude.trim().isEmpty()) {
                information.setLongitude(Double.parseDouble(longitude));
            } else {
                information.setLongitude(0.0);
            }
        } catch (NumberFormatException e) {
            information.setLongitude(0.0);
            System.err.println("Error parsing longitude: " + e.getMessage());
        }

        sellerInformationService.write(information, file);
        model.addAttribute("message", "글 작성이 완료되었습니다.");
        return "redirect:/board/map.html";
    }

}