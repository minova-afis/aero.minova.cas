package aero.minova.core.application.system.setup.dependency;

import org.junit.Test;

import java.util.Arrays;
import java.util.Scanner;

import static aero.minova.core.application.system.setup.dependency.DependencyOrder.determineDependencyOrder;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class DependencyOrderTest {
	@Test
	public void testByExample() throws Exception {
		final var result = determineDependencyOrder(
				new Scanner(getClass()//
						.getClassLoader()//
						.getResourceAsStream("dependency-graph.json"), "UTF-8")//
						.useDelimiter("\\A")//
						.next());
		assertThat(result).isEqualTo(
				Arrays.asList("aero.minova.core.application.system.app"//
						, "aero.minova.app.i18n"//
						, "aero.minova.data.schema.app"//
						, "aero.minova.contact"//
						, "aero.minova.invoice"));
	}
}
