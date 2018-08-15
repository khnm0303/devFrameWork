package com.lyh.boot.frame.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FrameWorkController {
	@RequestMapping("/")
	public String index() {
		return "HelloWorld!";
	}
}
