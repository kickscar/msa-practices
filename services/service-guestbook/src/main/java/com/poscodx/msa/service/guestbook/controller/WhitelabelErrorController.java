package com.poscodx.msa.service.guestbook.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poscodx.msa.service.guestbook.dto.JsonResult;

@RestController
@RequestMapping("/error")
public class WhitelabelErrorController implements ErrorController {
	@GetMapping("")
	public ResponseEntity<?> handleError(HttpServletRequest request) {
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		return ResponseEntity.status(status.hashCode()).body(JsonResult.fail(status.toString()));
	}
}