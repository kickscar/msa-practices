package com.poscodx.emaillist.controller;

import java.util.Map;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.poscodx.emaillist.vo.EmaillistVo;


@SuppressWarnings("unchecked")
@RestController
@RequestMapping("/api/emaillist")
public class ApiController {
	
	private final RestTemplate restTemplate;
	
	public ApiController(@LoadBalanced RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	@GetMapping
	public ResponseEntity<?> read(@RequestParam(value="kw", required=true, defaultValue="") String keyword) {
		Map<String, Object> response = restTemplate.getForObject("http://service-emaillist/?kw="+keyword, Map.class);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PostMapping
	public ResponseEntity<?> create(@RequestBody EmaillistVo vo) {
		Map<String, Object> response = restTemplate.postForObject("http://service-emaillist/", vo, Map.class);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}