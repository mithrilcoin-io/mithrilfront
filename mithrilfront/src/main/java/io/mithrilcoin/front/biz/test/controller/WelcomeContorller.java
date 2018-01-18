package io.mithrilcoin.front.biz.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/welcome")
public class WelcomeContorller {

	@RequestMapping("/hello")
	public String getWelcome()
	{
		
		return "welcome";
	}
}
