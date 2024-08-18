package com.example.naver.controller;

import com.example.naver.entity.Board;
import com.example.naver.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/board/write")
    public String boardWriteForm() {
        return "boardwrite";
    }

    @GetMapping("/board/chat")
    public String boardChat() {
        return "chat";
    }

    @PostMapping("/board/writedo")
    public String boardWriteDo(Board board, @RequestParam("file") MultipartFile file, Model model) throws Exception {
        boardService.write(board, file);
        model.addAttribute("message", "글 작성이 완료되었습니다.");
        model.addAttribute("url", "/board/list");
        return "message";
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id, Board board,
                              @RequestParam(name="file", required = false) MultipartFile file,
                              @RequestParam(name="userName", required = false) String userName,
                              RedirectAttributes redirectAttributes) throws Exception {
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
        redirectAttributes.addFlashAttribute("url", "/board/content?id=" + id);
        return "redirect:/board/content?id=" + id;
    }

    @GetMapping("/board/delete")
    public String boardDelete(@RequestParam(name="id") Integer id, RedirectAttributes redirectAttributes) {
        boardService.boardDelete(id);
        redirectAttributes.addFlashAttribute("message", "게시물이 삭제되었습니다.");
        redirectAttributes.addFlashAttribute("url", "/board/list");
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
        model.addAttribute("board", boardService.boardContent(id));
        return "boardcontent";
    }

    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("board", boardService.boardContent(id));
        return "boardmodify";
    }

    @GetMapping("/board/map.html")
    public String showMapPage() {
        return "map";
    }
}
