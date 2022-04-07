package me.kickscar.msa.service.storage.controller;

import me.kickscar.msa.service.storage.service.FileUploadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final FileUploadService fileUploadService;

    public ApiController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping
    public ResponseEntity<?> upload(MultipartFile file) {
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("url", fileUploadService.restoreImage(file)));
    }
}
