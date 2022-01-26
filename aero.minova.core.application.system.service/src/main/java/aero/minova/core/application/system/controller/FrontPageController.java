package aero.minova.core.application.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontPageController {
	@GetMapping("")
	public String root() {
		return "index.html";
	}

	@GetMapping("index")
	public String index() {
		return "index.html";
	}

	@GetMapping("index.html")
	public String indexFull() {
		return "index.html";
	}

	@GetMapping("login")
	public String login() {
		return "index.html";
	}

	@GetMapping("setupError")
	public String setupError() {
		return "setupError.html";
	}

	@GetMapping("setupSuccess")
	public String setupSuccess() {
		return "setupSuccess.html";
	}
}
