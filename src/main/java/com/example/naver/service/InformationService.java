package com.example.naver.service;


import com.example.naver.entity.Information;
import com.example.naver.login.NaverLoginProfileRepository;
import com.example.naver.repository.InformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class InformationService {

    @Autowired
    private InformationRepository informationRepository;
    @Autowired
    private NaverLoginProfileRepository naverLoginProfileRepository;
    // 게시글 작성 (파일 포함)
    public void write(Information information, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";
            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(projectPath, fileName);

            // Ensure the directory exists
            if (Files.notExists(filePath.getParent())) {
                Files.createDirectories(filePath.getParent());
            }

            file.transferTo(filePath.toFile());

            information.setFilename(fileName);
            information.setFilepath("/files/" + fileName);
        }

        information.setPostedAt(LocalDateTime.now());
        informationRepository.save(information);
    }

    // 파일 없이 게시글 저장
    public void write(Information information) {
        information.setPostedAt(LocalDateTime.now());
        informationRepository.save(information);
    }

    // 게시글 리스트 처리
    public Page<Information> informationList(Pageable pageable) {
        return informationRepository.findAll(pageable);
    }

    // 게시글 리스트 가져오기 (전체)
    public List<Information> informationList() {
        return informationRepository.findAll();
    }

    // 게시글 내용 가져오기
    public Information informationContent(Integer id) {
        return informationRepository.findById(id).orElse(null);
    }

    // 게시글 삭제
    public void informationDelete(Integer id) {
        informationRepository.deleteById(id);
    }

    // 게시글 검색 리스트 처리
    public Page<Information> informationSearchList(String searchKey, Pageable pageable) {
        return informationRepository.findByTitleContaining(searchKey, pageable);
    }



    public List<Information> getAllInformation() {
        return informationRepository.findAll();
    }
    public void saveInformation(Information information) {
        // 가장 최근의 이메일 값을 가져와 정보 객체에 설정
        String latestEmail = naverLoginProfileRepository.findFirstByOrderByIdDesc().getEmail();
        if (latestEmail != null) {
            information.setEmail(latestEmail); // 이메일 필드에 값 설정
            information.setPostedAt(LocalDateTime.now());
        } else {
            System.out.println("No email found in NaverLoginProfile!");
        }
        // information 테이블에 데이터 저장
        informationRepository.save(information);
    }


}