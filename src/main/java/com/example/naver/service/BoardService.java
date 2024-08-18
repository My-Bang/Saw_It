package com.example.naver.service;

import com.example.naver.entity.Board;
import com.example.naver.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;

    // 게시글 작성 (파일 포함)
    public void write(Board board, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\board";

            // 디렉토리가 존재하지 않으면 생성
            File directory = new File(projectPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "_" + file.getOriginalFilename();

            File saveFile = new File(projectPath, fileName);
            file.transferTo(saveFile);
            board.setPostedAt(LocalDateTime.now());

            // 파일 관련 필드 설정 (필요하다면)
             board.setFilename(fileName);

             board.setFilepath("/static/board/" + fileName);
        }
        boardRepository.save(board);
    }


    // 게시글 작성 (파일 없음)
    public void write(Board board) {
        board.setPostedAt(LocalDateTime.now());
        boardRepository.save(board);
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
}
