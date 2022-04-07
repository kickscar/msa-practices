package me.kickscar.mysite.controller;

import me.kickscar.mysite.dto.JsonResult;
import me.kickscar.mysite.vo.GalleryVo;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/gallery")
public class GalleryController {

	private final RestTemplate restTemplate;

	public GalleryController(@LoadBalanced RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@GetMapping("")
	public ResponseEntity<?> readAll() {
		GalleryVo[] response = restTemplate.getForObject("http://service-gallery/api", GalleryVo[].class);
		return ResponseEntity.status(HttpStatus.OK).body(JsonResult.success(Arrays.asList(response)));
	}
	
	@PostMapping
	public ResponseEntity<?> create(MultipartFile file, GalleryVo galleryVo) {
		GalleryVo response = null;
		try {
			// parts
			HttpHeaders parts = new HttpHeaders();
			parts.setContentType(MediaType.TEXT_PLAIN);
			final ByteArrayResource byteArrayResource = new ByteArrayResource(file.getBytes()) {
				@Override
				public String getFilename() {
					return file.getOriginalFilename();
				}
			};

			// Multipart Body
			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("file", new HttpEntity<>(byteArrayResource, parts));

			// Multipart Header
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);

			// Multipart Request
			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
			Map<String, String> responseUpload = restTemplate.postForObject("http://service-storage/api", requestEntity, HashMap.class);

			galleryVo.setUrl(responseUpload.get("url"));
			response = restTemplate.postForObject("http://service-gallery/api", galleryVo, GalleryVo.class);
		} catch(Exception ex) {
			throw new RuntimeException(ex);
		}

		return ResponseEntity.status(HttpStatus.OK).body(JsonResult.success(response));
	}
	
	@DeleteMapping(value="/{no}")
	public ResponseEntity<?> delete(@PathVariable("no") Long no) {
		restTemplate.delete("http://service-gallery/api/" + no);
		return ResponseEntity.status(HttpStatus.OK).body(JsonResult.success(Map.of("no", no)));
	}
}
