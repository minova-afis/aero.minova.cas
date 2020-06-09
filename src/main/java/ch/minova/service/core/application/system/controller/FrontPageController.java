package ch.minova.service.core.application.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Hier sollte man den identischen Pfad zurückgeben oder einen Redirect durchführen. Alles andere ist nicht zulässig.
 * 
 * @author avots
 */
@Controller
public class FrontPageController {
	@GetMapping("/")
	public String root() {
		return "redirect:/index.html";
	}
	
	@GetMapping("/index.html")
	public String index() {
		return "/index.html";
	}
}
