package aero.minova.core.application.system.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import aero.minova.core.application.system.domain.Table;

@RestController
public class SqlController {
	@GetMapping(value = "data/index", produces = "application/json")
	public Table getMovements(Model model) {
		Table movementTable = new Table();
		return movementTable;
	}
}
