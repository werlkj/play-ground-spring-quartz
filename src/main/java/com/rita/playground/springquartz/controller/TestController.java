package com.rita.playground.springquartz.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {
	private static Logger logger = LoggerFactory.getLogger(TestController.class);


	@GetMapping(value = "/testLog")
	public String testLog() {
		logger.info("test log start");
		logger.debug("test log debug");
		logger.error("test log error");
		logger.info("test log  end");
		return "Finish";
	}

	@GetMapping(value = "/testException")
	public String testException() {
		logger.info("test Exception start");
		@SuppressWarnings("unused")
		int i = 3 / 0;
		logger.info("test Exception  end");
		return "Finish";
	}

}
