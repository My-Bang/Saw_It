package com.example.naver.chat;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig1 implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/board/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/src/main/resources/static/board/")
                .setCachePeriod(0); // 캐싱 비활성화
    }
}
