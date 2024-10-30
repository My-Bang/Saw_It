package com.example.naver.service;

import com.example.naver.entity.ConsumerInformation;
import com.example.naver.entity.Information;
import com.example.naver.login.NaverLoginProfileRepository;
import com.example.naver.repository.ConsumerInformationRepository;
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
import java.util.function.Consumer;

@Service
public class ConsumerInformationService {

    @Autowired
    private ConsumerInformationRepository consumerInformationRepository;
    @Autowired
    private NaverLoginProfileRepository naverLoginProfileRepository;
    public void writed(ConsumerInformation information, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";
            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(projectPath, fileName);

            if (Files.notExists(filePath.getParent())) {
                Files.createDirectories(filePath.getParent());
            }

            file.transferTo(filePath.toFile());

            information.setFilename(fileName);
            information.setFilepath("/files/" + fileName);
        }

        information.setPostedAt(LocalDateTime.now());
        consumerInformationRepository.save(information);
    }

    public void write(ConsumerInformation information) {
        information.setPostedAt(LocalDateTime.now());
        consumerInformationRepository.save(information);
    }

    public void informationDelete(Integer id) {
        ConsumerInformation information = consumerInformationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID: " + id));
        information.setDeleted(true);
        consumerInformationRepository.save(information);
    }

    public List<ConsumerInformation> getAllInformation() {
        return consumerInformationRepository.findByDeletedFalse(Pageable.unpaged()).getContent();
    }

    public void saveConsumerInformation(ConsumerInformation consumerInformation) {
        // 가장 최근의 이메일 값을 가져와 정보 객체에 설정
        String latestEmail = naverLoginProfileRepository.findFirstByOrderByIdDesc().getEmail();
        if (latestEmail != null) {
            consumerInformation.setEmail(latestEmail); // 이메일 필드에 값 설정
            consumerInformation.setPostedAt(LocalDateTime.now());
        } else {
            System.out.println("No email found in NaverLoginProfile!");
        }
        // information 테이블에 데이터 저장
        consumerInformationRepository.save(consumerInformation);
    }
}