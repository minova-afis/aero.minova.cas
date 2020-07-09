package aero.minova.core.application.system.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aero.minova.core.application.system.domain.Table;

@RestController
public class SqlController {
	@GetMapping(value = "data/index", produces = "application/json")
	public Table getIndexView(@RequestParam String indexViewName) {
		Table movementTable = new Table();
		movementTable.setName(indexViewName);
		return movementTable;
	}
}
