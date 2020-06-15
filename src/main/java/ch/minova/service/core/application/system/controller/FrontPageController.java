package ch.minova.service.core.application.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class FrontPageController {
	@GetMapping("/")
	public String root() {
		return "redirect:/index.html";
	}

	@GetMapping("/index")
	public String index() {
		return "/index.html";
	}
	
	@GetMapping("/index.html")
	public String indexFull() {
		return "/index.html";
	}

	@GetMapping("/login")
	public String login() {
		return "/index.html";
	}
}
