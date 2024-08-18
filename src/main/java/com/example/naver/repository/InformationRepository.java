package com.example.naver.repository;

import com.example.naver.entity.Information;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface InformationRepository extends JpaRepository<Information, Integer>{
    Page<Information> findByTitleContaining(String searchKey, Pageable pageable);
}
