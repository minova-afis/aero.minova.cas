package ch.minova.service.core.application.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class RootController {
	
	@GetMapping("/root/")
	public String adminIndexShortcut() {
		return "/root/index.html";
	}
	
	@GetMapping("/root/index.html")
	public String adminIndex() {
		return "/root/index.html";
	}

	@GetMapping("/root/login.html")
	public String login() {
		return "/root/login.html";
	}
}
