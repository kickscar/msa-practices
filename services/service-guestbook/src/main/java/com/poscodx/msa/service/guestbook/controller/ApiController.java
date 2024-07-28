package com.poscodx.msa.service.guestbook.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poscodx.msa.service.guestbook.dto.JsonResult;
import com.poscodx.msa.service.guestbook.service.GuestbookService;
import com.poscodx.msa.service.guestbook.vo.GuestbookVo;

@RestController
@RequestMapping("/")
public class ApiController {
	private final GuestbookService guestbookService;

	public ApiController(GuestbookService guestbookService) {
		this.guestbookService = guestbookService;
	}

	@GetMapping
	public ResponseEntity<JsonResult> list(@RequestParam(value="no", required=true, defaultValue="0") Long startNo) {
		return ResponseEntity.status(HttpStatus.OK).body(JsonResult.success(guestbookService.getContentsList(startNo)));
	}

	@PostMapping
	public ResponseEntity<JsonResult> add(@RequestBody GuestbookVo vo) {
		guestbookService.addContents(vo);
		vo.setPassword("");		
		return ResponseEntity.status(HttpStatus.OK).body(JsonResult.success(vo));
	}

	@DeleteMapping("/{no}")
	public ResponseEntity<JsonResult> delete(@PathVariable("no") Long no, @RequestParam(value="password", required=true, defaultValue="") String password) {
		Boolean result = guestbookService.deleteContents(no, password);
		return ResponseEntity.status(HttpStatus.OK).body(JsonResult.success(result ? no : null));
	}
}