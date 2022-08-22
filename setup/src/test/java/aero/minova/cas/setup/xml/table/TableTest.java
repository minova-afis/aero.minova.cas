package aero.minova.cas.setup.xml.table;

import aero.minova.cas.setup.xml.setup.Setup;
import org.junit.Test;

import java.util.Objects;
import java.util.Scanner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TableTest {
    @Test
    public void testParsing() {
        final String tFuellingType = new Scanner(Objects.requireNonNull(getClass()//
                .getClassLoader()//
                .getResourceAsStream("tFuellingType.table.xml")), "UTF-8")//
                .useDelimiter("\\A")//
                .next();
        final var testSubject = Setup.parse(tFuellingType);
    }
}
