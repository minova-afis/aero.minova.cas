package aero.minova.covid.test.print.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class TeststeckenController {
	@GetMapping(value = "testStrecken/keyTexts", produces = "application/json")
	public List<String> getTestStreckenKeyTexts() throws Exception {
		return Arrays.asList("Eibelstadt", "Frickenhausen am Main", "Sommerhausen", "Winterhausen");
	}
}
