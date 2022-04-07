package me.kickscar.msa.service.guestbook.controller;

import me.kickscar.msa.service.guestbook.service.GuestbookService;
import me.kickscar.msa.service.guestbook.vo.GuestbookVo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {
	private final GuestbookService guestbookService;

	public ApiController(GuestbookService guestbookService) {
		this.guestbookService = guestbookService;
	}

	@GetMapping("")
	public ResponseEntity<?> read(@RequestParam(value="no", required=true, defaultValue="0") Long startNo) {
		return ResponseEntity.status(HttpStatus.OK).body(guestbookService.getMessageList(startNo));
	}

	@PostMapping("")
	public ResponseEntity<?> create(@RequestBody GuestbookVo vo) {
		guestbookService.addMessage(vo);
		vo.setPassword("");		
		return ResponseEntity.status(HttpStatus.OK).body(vo);
	}

	@DeleteMapping("/{no}")
	public ResponseEntity<?> delete(@PathVariable("no") Long no, @RequestParam(value="password", required=true, defaultValue="") String password) {
		Boolean result = guestbookService.deleteMessage(no, password);		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of("no", result ? no : null));
	}
}