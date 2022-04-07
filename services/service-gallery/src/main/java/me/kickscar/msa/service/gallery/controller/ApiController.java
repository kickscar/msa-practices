package me.kickscar.msa.service.gallery.controller;

import me.kickscar.msa.service.gallery.service.GalleryService;
import me.kickscar.msa.service.gallery.vo.GalleryVo;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {
	private final GalleryService galleryService;

	public ApiController(GalleryService galleryService) {
		this.galleryService = galleryService;
	}

	@GetMapping("")
	public ResponseEntity<?> get() {
		return ResponseEntity.status(HttpStatus.OK).body(galleryService.getGalleryImages());
	}
	
	@PostMapping
	public ResponseEntity<?> post(@RequestBody GalleryVo galleyVo) {
		galleryService.addGalleryImage(galleyVo);
		return ResponseEntity.status(HttpStatus.OK).body(galleyVo);
	}
	
	@DeleteMapping(value="/{no}")
	public ResponseEntity<?> delete(@PathVariable("no") Long no) {
		galleryService.deleteGalleryImage(no);
		return ResponseEntity.status(HttpStatus.OK).body(Map.of("no", no));
	}
}
