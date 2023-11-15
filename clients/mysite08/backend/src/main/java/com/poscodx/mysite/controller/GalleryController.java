package com.poscodx.mysite.controller;

import java.util.Map;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.poscodx.mysite.vo.GalleryVo;

@SuppressWarnings("unchecked")
@RestController
@RequestMapping("/api/gallery")
public class GalleryController {
	
	private final RestTemplate restTemplate;

	public GalleryController(@LoadBalanced RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@GetMapping
	public ResponseEntity<?> readAll() {
		Map<String, Object> response = restTemplate.getForObject("http://service-gallery/", Map.class);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PostMapping
	public ResponseEntity<?> create(MultipartFile file, GalleryVo galleryVo) {
		Map<String, Object> response = null;
		
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
			Map<String, String> responseUpload = restTemplate.postForObject("http://service-storage/", requestEntity, Map.class);

			galleryVo.setImageUrl(responseUpload.get("data"));
			response = restTemplate.postForObject("http://service-gallery/", galleryVo, Map.class);
		} catch(Exception ex) {
			throw new RuntimeException(ex);
		}

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@DeleteMapping(value="/{no}")
	public ResponseEntity<?> delete(@PathVariable("no") Long no) {
		// Header
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		// Body
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
		
		ResponseEntity<?> response = restTemplate.exchange(String.format("http://service-gallery/%d", no), HttpMethod.DELETE, requestEntity, Map.class);
		return ResponseEntity.status(HttpStatus.OK).body(response.getBody());
	}
}