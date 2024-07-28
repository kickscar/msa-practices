package com.poscodx.msa.service.storage.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.poscodx.msa.service.storage.dto.JsonResult;
import com.poscodx.msa.service.storage.service.FileUploadService;

@RestController
@RequestMapping("/")
public class ApiController {

    private final FileUploadService fileUploadService;

    public ApiController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping
    public ResponseEntity<JsonResult> upload(MultipartFile file) {
        return ResponseEntity.status(HttpStatus.OK).body(JsonResult.success(fileUploadService.restoreImage(file)));
    }
}
