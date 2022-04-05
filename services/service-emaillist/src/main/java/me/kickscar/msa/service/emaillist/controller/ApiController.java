package me.kickscar.msa.service.emaillist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import me.kickscar.msa.service.emaillist.repository.EmaillistRepository;
import me.kickscar.msa.service.emaillist.vo.EmaillistVo;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ApiController {
	
	@Autowired
	private EmaillistRepository emaillistRepository;
	
	@GetMapping
	public ResponseEntity<Object> read(@RequestParam(value="kw", required=true, defaultValue="") String keyword) {
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(emaillistRepository.findAll(keyword));
	}
	
	@PostMapping
	public ResponseEntity<Object> create(@RequestBody EmaillistVo vo) {
		emaillistRepository.insert(vo);
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(vo);
	}
}
