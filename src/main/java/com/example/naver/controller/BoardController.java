package com.example.naver.controller;

import com.example.naver.entity.Board;
import com.example.naver.login.vo.NaverLoginProfile;
import com.example.naver.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.xml.stream.Location;

@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/board/write")
    public String boardWriteForm() {
        return "boardwrite";
    }


    @PostMapping("/board/writedo")
    public String boardWriteDo(Board board, @RequestParam("file") MultipartFile file, Model model) throws Exception {
        boardService.write(board, file);
        model.addAttribute("message", "글 작성이 완료되었습니다.");
        model.addAttribute("url", "/board/list");
        return "message";
    }

    // 게시글 수정
    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id, Board board,
                              @RequestParam(name="file", required = false) MultipartFile file,
                              @RequestParam(name="userName", required = false) String userName,
                              RedirectAttributes redirectAttributes) throws Exception {
        // 권한 확인
        if (!boardService.isAuthorizedToModifyOrDelete(id)) {
            redirectAttributes.addFlashAttribute("message", "수정 권한이 없습니다.");
            return "redirect:/board/list";
        }

        Board boardTemp = boardService.boardContent(id);
        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());

        if (userName != null && !userName.isEmpty()) {
            boardTemp.setUserName(userName);
        }

        if (file != null && !file.isEmpty()) {
            boardService.write(boardTemp, file);
        } else {
            boardService.write(boardTemp);
        }

        redirectAttributes.addFlashAttribute("message", "게시물이 수정되었습니다.");
        return "redirect:/board/content?id=" + id;
    }

    // 게시글 삭제
    @GetMapping("/board/delete")
    public String boardDelete(@RequestParam(name="id") Integer id, RedirectAttributes redirectAttributes) {
        // 권한 확인
        if (!boardService.isAuthorizedToModifyOrDelete(id)) {
            redirectAttributes.addFlashAttribute("message", "삭제 권한이 없습니다.");
            return "redirect:/board/list";
        }

        boardService.boardDelete(id);
        redirectAttributes.addFlashAttribute("message", "게시물이 삭제되었습니다.");
        return "redirect:/board/list";
    }


    @GetMapping("/board/list")
    public String boardList(Model model, @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                            @RequestParam(name="searchKey", defaultValue = "") String searchKey,
                            @ModelAttribute("message") String message) {
        Page<Board> list;
        if(searchKey == null) {
            list = boardService.boardList(pageable);
        } else {
            list = boardService.boardSearchList(searchKey, pageable);
        }

        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("message", message);
        return "boardList";
    }

    @GetMapping("/board/content")
    public String boardContent(Model model, @RequestParam(name="id") Integer id) {
        Board board = boardService.boardContent(id);
        model.addAttribute("board", board);

        // 현재 로그인한 사용자의 이메일과 게시글 작성자의 이메일을 비교
        String latestEmail = boardService.getLatestEmailFromNaverLoginProfile();
        boolean isAuthor = latestEmail != null && latestEmail.equals(board.getUserEmail());

        model.addAttribute("isAuthor", isAuthor);
        return "boardcontent";
    }


    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("board", boardService.boardContent(id));
        return "boardmodify";
    }

    @GetMapping("/board/map.html")
    public String showMapPage(HttpSession session) {
        NaverLoginProfile loginUser = (NaverLoginProfile) session.getAttribute("loginUser");
        System.out.println("session.getAttribute(\"loginUser\") = " + loginUser.getId());
        System.out.println("session.getAttribute(\"loginUser\") = " + loginUser.getEmail());
        return "map";
    }

    @GetMapping("/board/tutorial")
    public String showtutorialPage() {
        return "tutorial";
    }

    @GetMapping("/board/Q&A")
    public String showQNAPage() {
        return "Q&A";
    }

}
