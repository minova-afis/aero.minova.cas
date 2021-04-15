package aero.minova.core.application.system.covid.test.print.controller;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import aero.minova.core.application.system.controller.SqlViewController;
import aero.minova.core.application.system.covid.test.print.domain.TestStrecke;
import aero.minova.core.application.system.domain.Column;
import aero.minova.core.application.system.domain.DataType;
import aero.minova.core.application.system.domain.OutputType;
import aero.minova.core.application.system.domain.Row;
import aero.minova.core.application.system.domain.Table;
import aero.minova.core.application.system.domain.Value;
import lombok.val;

@RestController
public class TeststeckenController {

	@Autowired
	SqlViewController sqlViewController;

	@CrossOrigin
	@GetMapping(value = "testStrecken/keyTexts", produces = "application/json")
	public List<TestStrecke> getTestStreckenKeyTexts() throws Exception {
		val sqlRequest = new Table();
		sqlRequest.setName("xvctsTestStreckeIndex");
		sqlRequest.addColumn(new Column("KeyLong", DataType.INTEGER, OutputType.OUTPUT));
		sqlRequest.addColumn(new Column("KeyText", DataType.STRING, OutputType.OUTPUT));

		// Hiermit wird der unsichere Zugriff ermöglicht.
		val requestingAuthority = new Row();
		requestingAuthority.addValue(new Value(false, "1"));
		requestingAuthority.addValue(new Value(false, "2"));
		requestingAuthority.addValue(new Value(false, "3"));
		return sqlViewController.unsecurelyGetIndexView(sqlRequest, Arrays.asList(requestingAuthority)).getRows().stream()
				.map(row -> new TestStrecke(row.getValues().get(1).getStringValue(), row.getValues().get(0).getIntegerValue())).collect(toList());
	}
}
