package com.example.naver.repository;

import com.example.naver.entity.SellerInformation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellerInformationRepository extends JpaRepository<SellerInformation, Integer> {
    Page<SellerInformation> findByTitleContainingAndDeletedFalse(String searchKey, Pageable pageable);
    Page<SellerInformation> findByDeletedFalse(Pageable pageable);
    List<SellerInformation> findByDeletedFalse();
    boolean existsByEmail(String email);

}

