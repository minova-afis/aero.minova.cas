package aero.minova.core.application.system.covid.test.print.controller;

import java.util.Arrays;
import java.util.List;

import aero.minova.core.application.system.controller.SqlProcedureController;
import aero.minova.core.application.system.controller.SqlViewController;
import aero.minova.core.application.system.domain.*;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.util.stream.Collectors.toList;

@CrossOrigin
@RestController
public class TeststeckenController {

    @Autowired
    SqlViewController sqlViewController;

    @PostMapping(value = "testStrecken/keyTexts", produces = "application/json")
    public List<String> getTestStreckenKeyTexts() throws Exception {
        val param = new Table();
        param.setName("xtctsTestStrecke");
        param.addColumn(new Column("KeyText", DataType.STRING, OutputType.OUTPUT));

        // Hiermit wird der unsichere Zugriff ermöglicht.
        val requestingAuthority = new Row();
        requestingAuthority.addValue(new Value(false, "1"));
        requestingAuthority.addValue(new Value(false, "2"));
        requestingAuthority.addValue(new Value(false, "3"));
        return sqlViewController.unsecurelyGetIndexView (param, Arrays.asList(requestingAuthority))
                .getRows()
                .stream()
                .map(row -> row.getValues().get(0).getStringValue())
                .collect(toList());
    }
}
