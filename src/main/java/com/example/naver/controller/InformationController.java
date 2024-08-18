package com.example.naver.controller;

import com.example.naver.entity.Information;
import com.example.naver.service.InformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class InformationController {

    @Autowired
    private InformationService informationService;

    @GetMapping("/information/write")
    public String informationWriteForm() {
        return "information";
    }

    @PostMapping("/information/writedo")
    public String informationWriteDo(Information information,
                                     @RequestParam("latitude") String latitude,
                                     @RequestParam("longitude") String longitude,
                                     Model model,
                                     MultipartFile file) throws Exception {
        // 로그를 통한 데이터 확인
        System.out.println("Latitude from request: " + latitude);
        System.out.println("Longitude from request: " + longitude);

        // Latitude 처리
        try {
            if (latitude != null && !latitude.trim().isEmpty()) {
                information.setLatitude(Double.parseDouble(latitude));
            } else {
                information.setLatitude(0.0); // 기본값 설정
            }
        } catch (NumberFormatException e) {
            information.setLatitude(0.0); // 기본값 설정
            System.err.println("Error parsing latitude: " + e.getMessage());
        }

        // Longitude 처리
        try {
            if (longitude != null && !longitude.trim().isEmpty()) {
                information.setLongitude(Double.parseDouble(longitude));
            } else {
                information.setLongitude(0.0); // 기본값 설정
            }
        } catch (NumberFormatException e) {
            information.setLongitude(0.0); // 기본값 설정
            System.err.println("Error parsing longitude: " + e.getMessage());
        }

        // 정보 저장
        informationService.write(information, file);
        model.addAttribute("message", "글 작성이 완료되었습니다.");
        return "redirect:/information/list";  // 작성 완료 후 목록 페이지로 리다이렉트
    }

    @PostMapping("/information/update/{id}")
    public String informationUpdate(@PathVariable("id") Integer id,
                                    Information information,
                                    @RequestParam(name="file", required = false) MultipartFile file,
                                    @RequestParam(name="userName", required = false) String userName,
                                    RedirectAttributes redirectAttributes) throws Exception {
        Information informationTemp = informationService.informationContent(id);
        informationTemp.setTitle(information.getTitle());
        informationTemp.setContent(information.getContent());

        if (userName != null && !userName.isEmpty()) {
            informationTemp.setUserName(userName);
        }

        if (file != null && !file.isEmpty()) {
            informationService.write(informationTemp, file);
        } else {
            informationService.write(informationTemp);
        }

        redirectAttributes.addFlashAttribute("message", "게시물이 수정되었습니다.");
        redirectAttributes.addFlashAttribute("url", "/information/content?id=" + id);
        return "redirect:/information/content?id=" + id;
    }

    @GetMapping("/information/delete")
    public String informationDelete(@RequestParam(name="id") Integer id, RedirectAttributes redirectAttributes) {
        informationService.informationDelete(id);
        redirectAttributes.addFlashAttribute("message", "게시물이 삭제되었습니다.");
        redirectAttributes.addFlashAttribute("url", "/information/list");
        return "redirect:/information/list";
    }

    @GetMapping("/information/list")
    public String information(Model model, @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                              @RequestParam(name="searchKey", defaultValue = "") String searchKey,
                              @ModelAttribute("message") String message) {
        Page<Information> list;
        if (searchKey == null || searchKey.isEmpty()) {
            list = informationService.informationList(pageable);
        } else {
            list = informationService.informationSearchList(searchKey, pageable);
        }

        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("message", message);
        return "informationList";
    }

    @GetMapping("/information/content")
    public String informationContent(Model model, @RequestParam(name="id") Integer id) {
        model.addAttribute("information", informationService.informationContent(id));
        return "informationcontent";
    }

    @GetMapping("/information/modify/{id}")
    public String informationModify(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("information", informationService.informationContent(id));
        return "informationmodify";
    }

    @GetMapping("/information/map.html")
    public String IshowMappage() {
        return "map";
    }
}
