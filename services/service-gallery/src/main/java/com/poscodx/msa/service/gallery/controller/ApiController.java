package com.poscodx.msa.service.gallery.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poscodx.msa.service.gallery.dto.JsonResult;
import com.poscodx.msa.service.gallery.service.GalleryService;
import com.poscodx.msa.service.gallery.vo.GalleryVo;

@RestController
@RequestMapping("/")
public class ApiController {
	private final GalleryService galleryService;

	public ApiController(GalleryService galleryService) {
		this.galleryService = galleryService;
	}

	@GetMapping
	public ResponseEntity<JsonResult> get() {
		return ResponseEntity.status(HttpStatus.OK).body(JsonResult.success(galleryService.getGalleryImages()));
	}
	
	@PostMapping
	public ResponseEntity<JsonResult> post(@RequestBody GalleryVo galleyVo) {
		galleryService.addGalleryImage(galleyVo);
		return ResponseEntity.status(HttpStatus.OK).body(JsonResult.success(galleyVo));
	}
	
	@DeleteMapping(value="/{no}")
	public ResponseEntity<JsonResult> delete(@PathVariable("no") Long no) {
		galleryService.deleteGalleryImage(no);
		return ResponseEntity.status(HttpStatus.OK).body(JsonResult.success(no));
	}
}
