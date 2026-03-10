package com.pccth_km_bot.backend.Controller;

import com.pccth_km_bot.backend.Service.FileExtractService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("file-extract")
public class FileExtractController {

    private final FileExtractService fileExtractService;

    public FileExtractController(FileExtractService fileExtractService) {
        this.fileExtractService = fileExtractService;
    }

    @PostMapping("/extract")
    public ResponseEntity<?> extractFile(@RequestParam("file") MultipartFile file) throws Exception {

        String extractedText = fileExtractService.extractText(file);

        return ResponseEntity.ok(
                Map.of(
                        "responseStatus", 200,
                        "responseMessage", "File extracted successfully",
                        "data", Map.of(
                                "fileName", file.getOriginalFilename(),
                                "text", extractedText
                        )
                )
        );
    }
}