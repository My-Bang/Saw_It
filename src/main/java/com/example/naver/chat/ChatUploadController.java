package com.example.naver.chat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/chat")
public class ChatUploadController {

    private static final String FIXED_UPLOAD_PATH = "C:/Users/smb70/OneDrive - bu.ac.kr/바탕 화면/saw-it 3 - 복사본/saw-it/saw-it/src/main/resources/static/files";

    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<Map<String, String>> handleFileUpload(@RequestParam("file") MultipartFile file,
                                                                @RequestParam("roomId") String roomId) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "파일이 없습니다."));
        }

        String originalFilename = file.getOriginalFilename();
        String fileExtension = StringUtils.getFilenameExtension(originalFilename);
        String storedFileName = UUID.randomUUID().toString() + "." + fileExtension;

        File saveDir = new File(FIXED_UPLOAD_PATH);
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }

        File saveFile = new File(saveDir, storedFileName);
        file.transferTo(saveFile);

        Map<String, String> result = new HashMap<>();
        result.put("fileName", storedFileName);
        return ResponseEntity.ok(result);
    }
}
