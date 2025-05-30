package com.example.naver.service;

import com.example.naver.entity.Board;
import com.example.naver.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 게시글 작성 (파일 포함)
    public void write(Board board, MultipartFile file) throws IOException {
        board.setPostedAt(LocalDateTime.now());

        if (file != null && !file.isEmpty()) {
            String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/board";

            // 디렉토리가 존재하지 않으면 생성
            File directory = new File(projectPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "_" + file.getOriginalFilename();

            File saveFile = new File(projectPath, fileName);
            file.transferTo(saveFile);

            // 파일 관련 필드 설정
            board.setFilename(fileName);
            board.setFilepath("/board/" + fileName);
        }

        // 최신 이메일로 user_email 설정
        String latestEmail = getLatestEmailFromNaverLoginProfile();
        if (latestEmail != null) {
            board.setUserEmail(latestEmail);
        }

        boardRepository.save(board);
    }


    // 게시글 작성 (파일 없음)
    public void write(Board board) {
        board.setPostedAt(LocalDateTime.now());

        // 최신 이메일로 user_email 설정
        String latestEmail = getLatestEmailFromNaverLoginProfile();
        if (latestEmail != null) {
            board.setUserEmail(latestEmail); // Board 엔티티의 user_email 속성에 최신 이메일 저장
        }

        boardRepository.save(board);
    }

    // naver_login_profile 테이블에서 최신 email 값 가져오기
    public  String getLatestEmailFromNaverLoginProfile() {
        try {
            String selectQuery = "SELECT email FROM naver_login_profile ORDER BY id DESC LIMIT 1";
            return jdbcTemplate.queryForObject(selectQuery, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 예외 발생 시 null 반환
        }
    }

    public Page<Board> boardList(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    public List<Board> boardList() {
        return boardRepository.findAll();
    }

    public Board boardContent(Integer id) {
        return boardRepository.findById(id).orElse(null);
    }

    public void boardDelete(Integer id) {
        boardRepository.deleteById(id);
    }

    public Page<Board> boardSearchList(String searchKey, Pageable pageable) {
        return boardRepository.findByTitleContainingOrContentContaining(searchKey, searchKey, pageable);
    }

    public boolean isAuthorizedToModifyOrDelete(Integer boardId) {
        String latestEmail = getLatestEmailFromNaverLoginProfile();
        Board board = boardContent(boardId);

        return board != null && latestEmail != null && latestEmail.equals(board.getUserEmail());
    }

}
