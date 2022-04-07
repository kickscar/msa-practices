package me.kickscar.msa.service.gallery.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;

@Slf4j
@SuppressWarnings("serial")
@ControllerAdvice
public class GlobalExceptionHandler {	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseEntity<Object> handlerException(Exception e) {
		// 1. 로깅(logging)
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		log.error(errors.toString());
		
		// 2. JSON 응답		
		return ResponseEntity.status(HttpStatus.OK).body(new HashMap<String, String>(){{
			put("error", errors.toString());
		}});
	}
}

