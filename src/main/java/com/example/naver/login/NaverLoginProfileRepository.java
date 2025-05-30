package com.example.naver.login;

import com.example.naver.login.vo.NaverLoginProfile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NaverLoginProfileRepository extends JpaRepository<NaverLoginProfile, Long> {
    NaverLoginProfile findByEmail(String email);
    @Query(value = "SELECT email FROM naver_login_profile WHERE id = (SELECT MAX(id) FROM naver_login_profile WHERE userId = :userId)", nativeQuery = true)
    String findLatestEmailByUserId(@Param("userId") Long userId);
    @Query(value = "SELECT email FROM naver_login_profile ORDER BY id DESC LIMIT 1", nativeQuery = true)
    String findLatestEmail();

    @Query("SELECT n.email FROM NaverLoginProfile n ORDER BY n.id DESC")
    List<String> findLatestEmail(Pageable pageable);
    @Query("SELECT p FROM NaverLoginProfile p ORDER BY p.loginTime DESC") // 실제 필드명에 맞게 수정
    List<NaverLoginProfile> findLatestProfiles();

    NaverLoginProfile findFirstByOrderByIdDesc();

    default String findFirstLatestEmail() {
        return findLatestEmail(PageRequest.of(0, 1)).stream().findFirst().orElse(null);
    }

    @Query("SELECT n FROM NaverLoginProfile n ORDER BY n.id DESC")
    List<NaverLoginProfile> findLatestProfiles(Pageable pageable);

    // 메서드가 제대로 정의되었는지 확인
    @Query("SELECT n FROM NaverLoginProfile n ORDER BY n.id DESC")
    Optional<NaverLoginProfile> findLatestProfile(); // Optional로 정의된 가장 최근 프로필
}
