package aero.minova.cas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontPageController {

	private static final String FRONTPAGE = "index.html";

	@GetMapping("")
	public String root() {
		return FRONTPAGE;
	}

	@GetMapping("index")
	public String index() {
		return FRONTPAGE;
	}

	@GetMapping("index.html")
	public String indexFull() {
		return FRONTPAGE;
	}

	@GetMapping("login")
	public String login() {
		return FRONTPAGE;
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
