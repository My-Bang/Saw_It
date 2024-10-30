package com.example.naver.controller;


import com.example.naver.entity.ConsumerInformation;
import com.example.naver.service.ConsumerInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ConsumerInformationController {

    @Autowired
    private ConsumerInformationService consumerInformationService;

    @GetMapping("/consumerinformation/write")
    public String informationWriteForm() {
        return "consumerinformation";
    }

    @PostMapping("/consumer/information/writedo")
    public String informationWriteDo(ConsumerInformation information,
                                     @RequestParam("latitude") String latitude,
                                     @RequestParam("longitude") String longitude, MultipartFile file) throws Exception {
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
        consumerInformationService.writed(information,file);
        return "redirect:/board/map.html";
    }

}


