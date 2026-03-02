package aero.minova.cas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontPageController {

	private static final String FRONTPAGE = "index.html";

	/** Redirect /cas/ to the embedded React UI. */
	@GetMapping("")
	public String root() {
		return "redirect:ui/";
	}

	/** Old CAS setup/login page — moved to /cas/setup. */
	@GetMapping({"setup", "index", "index.html"})
	public String setup() {
		return FRONTPAGE;
	}

	/** Spring Security login page (anonymous users landing here see the login form). */
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
