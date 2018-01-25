package io.mithrilcoin.front.biz.test.controller;

import javax.servlet.http.HttpSession;

import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import io.mithril.vo.member.Account;
import io.mithrilcoin.front.response.MithrilResponseEntity;

@Controller
@RequestMapping("/welcome")
public class WelcomeContorller {

	@RequestMapping("/hello")
	public String getWelcome(Device device)
	{
		
		return "welcome";
	}
	
	@RequestMapping("/test")
	public MithrilResponseEntity<Account> getTestMessage(HttpSession session)
	{
		Account acc = new Account();
		acc.setAlias("ffsdfds");
		acc.setMember_idx(234342234);
		acc.setType("sd9f8sd98f");
	//	return new MithrilResponseEntity<Account>(acc, HttpStatus.OK, session);
		return null;
	}
}
