package com.poscodx.mysite.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poscodx.mysite.dto.JsonResult;

@Controller
@RequestMapping("/error")
public class WhitelabelErrorController implements ErrorController {
	
	@RequestMapping("/404")
	public String _404() {
		return "index";
	}
	
	@RequestMapping("/500")
	public String _500() {
		return "errors/500";
	}

	@ResponseBody
	@RequestMapping("")
	public Object handleError(HttpServletRequest request) {
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		String errorMessage = "";
		
	    if(status != null) {
	        Integer statusCode = Integer.valueOf(status.toString());
	    
	        if(statusCode == HttpStatus.NOT_FOUND.value()) {
	            return "Not Found";
	        }
	        
	        if(statusCode == HttpStatus.BAD_REQUEST.value()) {
	            errorMessage = "400 BAD_REQUEST";
	        }
	        
	        if(statusCode == HttpStatus.FORBIDDEN.value()) {
	        	errorMessage = "403 FORBIDDEN";
	        }
	        
	        if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
	        	errorMessage = "500 INTERNAL SERVER ERROR";
	        } 
	    }
	    
	    return JsonResult.fail(errorMessage);
	}
}