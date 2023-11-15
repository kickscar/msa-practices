package com.poscodx.msa.service.storage.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.poscodx.msa.service.storage.dto.JsonResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ResponseBody
	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<JsonResult> NoHandlerFoundException(Exception e) {
		// 응답
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(JsonResult.fail(e.toString()));
	}
	
	@ResponseBody
	@ExceptionHandler(Exception.class)
	public ResponseEntity<JsonResult> handlerException(Exception e) {		
		// 로깅(Logging)
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		log.error(errors.toString());

		// 응답
		return ResponseEntity.status(HttpStatus.OK).body(JsonResult.fail(errors.toString()));
	}
}

