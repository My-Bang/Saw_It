package com.example.naver.service;

import com.example.naver.entity.ConsumerInformation;
import com.example.naver.login.NaverLoginProfileRepository;
import com.example.naver.repository.ConsumerInformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ConsumerInformationService {

    @Autowired
    private ConsumerInformationRepository consumerInformationRepository;

    @Autowired
    private NaverLoginProfileRepository naverLoginProfileRepository;

    public Integer write(ConsumerInformation information, String userEmail, MultipartFile file) throws IOException {
        information.setEmail(userEmail);
        information.setPostedAt(LocalDateTime.now());

        // 파일 처리
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

        return consumerInformationRepository.save(information).getId();
    }

    public void informationDelete(Integer id) {
        ConsumerInformation information = consumerInformationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid ID: " + id));
        information.setDeleted(true);
        consumerInformationRepository.save(information);
    }

    public List<ConsumerInformation> getAllInformation() {
        return consumerInformationRepository.findByDeletedFalse(Pageable.unpaged()).getContent();
    }

    public void saveConsumerInformation(ConsumerInformation consumerInformation) {
        String latestEmail = naverLoginProfileRepository.findFirstByOrderByIdDesc().getEmail();
        if (latestEmail != null) {
            consumerInformation.setEmail(latestEmail);
            consumerInformation.setPostedAt(LocalDateTime.now());
            consumerInformationRepository.save(consumerInformation);
        } else {
            System.out.println("No email found in NaverLoginProfile!");
        }
    }
}
