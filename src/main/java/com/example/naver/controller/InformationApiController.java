package com.example.naver.controller;

import com.example.naver.entity.Information;
import com.example.naver.service.InformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class InformationApiController {

    @Autowired
    private InformationService informationService;

    @GetMapping("/information")
    public List<Information> getInformation() {
        return informationService.getAllInformation();
    }
}

