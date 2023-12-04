package com.poscodx.msa.service.oauth2client.emaillist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LandingController {
	
	@GetMapping
	public String index() {
		return "index";
	}
}
