package aero.minova.cas.setup.xml.setup;

import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.Scanner;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class SetupTest {
    @Test
    public void testParsing() {
        final String setupXml = new Scanner(Objects.requireNonNull(getClass()//
                .getClassLoader()//
                .getResourceAsStream("Setup.xml")), "UTF-8")//
                .useDelimiter("\\A")//
                .next();
        final var testSubject = SetupType.parse(setupXml);
        assertThat(testSubject.getName()).isEqualTo("aero.minova.cas.app");
        assertThat(testSubject.getSchema().size()).isEqualTo(2);
        assertThat(testSubject.getSchema().get(0).getName()).isEqualTo("xtcasError");
        assertThat(testSubject.getSchema().get(0).getType()).isEqualTo("table");
        assertThat(testSubject.getSqlCode().size()).isEqualTo(2);
        assertThat(testSubject.getSqlCode().get(0).getName()).isEqualTo("xtcasAuthoritiesConstraints");
        assertThat(testSubject.getSqlCode().get(0).getType()).isEqualTo("script");
    }
}
