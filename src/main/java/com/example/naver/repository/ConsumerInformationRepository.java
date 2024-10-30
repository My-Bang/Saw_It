package com.example.naver.repository;

import com.example.naver.entity.ConsumerInformation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsumerInformationRepository extends JpaRepository<ConsumerInformation, Integer> {
    Page<ConsumerInformation> findByTitleContainingAndDeletedFalse(String searchKey, Pageable pageable);
    Page<ConsumerInformation> findByDeletedFalse(Pageable pageable);
    List<ConsumerInformation> findByDeletedFalse();
}
