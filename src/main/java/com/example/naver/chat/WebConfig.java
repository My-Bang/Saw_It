package com.example.naver.chat;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:/C:/Users/smb70/OneDrive - bu.ac.kr/바탕 화면/saw-it 3 - 복사본/saw-it/saw-it/src/main/resources/static/files/");
    }


}
