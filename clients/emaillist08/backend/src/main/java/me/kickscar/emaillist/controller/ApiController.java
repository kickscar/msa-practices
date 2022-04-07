package me.kickscar.emaillist.controller;

import java.util.Arrays;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import me.kickscar.emaillist.dto.JsonResult;
import me.kickscar.emaillist.vo.EmaillistVo;

@RestController
public class ApiController {
	
	private final RestTemplate restTemplate;

	public ApiController(@LoadBalanced RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@GetMapping("/api")
	public ResponseEntity<JsonResult> read(@RequestParam(value="kw", required=true, defaultValue="") String keyword) {		
		EmaillistVo[] response = restTemplate.getForObject("http://service-emaillist/api?kw="+keyword, EmaillistVo[].class);
		return ResponseEntity.status(HttpStatus.OK).body(JsonResult.success(Arrays.asList(response)));
	}
	
	@PostMapping("/api")
	public ResponseEntity<JsonResult> create(@RequestBody EmaillistVo vo) {
		EmaillistVo response = restTemplate.postForObject("http://service-emaillist/api", vo, EmaillistVo.class);
		return ResponseEntity.status(HttpStatus.OK).body(JsonResult.success(response));
	}
}
