package com.example.naver.service;

import com.example.naver.entity.SellerInformation;
import com.example.naver.repository.SellerInformationRepository;
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
public class SellerInformationService {

    @Autowired
    private SellerInformationRepository sellerInformationRepository;

    public void write(SellerInformation information, MultipartFile file) throws IOException {
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
        sellerInformationRepository.save(information);
    }

    public void write(SellerInformation information) {
        information.setPostedAt(LocalDateTime.now());
        sellerInformationRepository.save(information);
    }

    public void informationDelete(Integer id) {
        SellerInformation information = sellerInformationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID: " + id));
        information.setDeleted(true);
        sellerInformationRepository.save(information);
    }

    public List<SellerInformation> getAllInformation() {
        return sellerInformationRepository.findByDeletedFalse(Pageable.unpaged()).getContent();
    }
}