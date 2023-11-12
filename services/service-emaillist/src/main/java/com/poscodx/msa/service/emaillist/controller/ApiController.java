package com.poscodx.msa.service.emaillist.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poscodx.msa.service.emaillist.dto.JsonResult;
import com.poscodx.msa.service.emaillist.repository.EmaillistRepository;
import com.poscodx.msa.service.emaillist.vo.EmaillistVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/")
public class ApiController {

	private EmaillistRepository emaillistRepository;
	
	public ApiController(EmaillistRepository emaillistRepository) {
		this.emaillistRepository = emaillistRepository;
	}
	
	
	@GetMapping
	public ResponseEntity<?> read(@RequestParam(value="kw", required=true, defaultValue="") String keyword) {		
		log.info("Request[GET /]:"+ keyword);
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(JsonResult.success(emaillistRepository.findAll(keyword)));
	}
	
	@PostMapping
	public ResponseEntity<?> create(@RequestBody EmaillistVo vo) {
		log.info("Request[POST /]:" + vo);
		
		emaillistRepository.insert(vo);
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(JsonResult.success(vo));
	}
}
