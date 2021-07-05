package aero.minova.core.application.system.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

@Service
public class SetupService {

	public List<String> parseDependencyList(String arg) {
		List<String> dependencies = new ArrayList<>();
		// Die obere Zeile und die die Zeilen mit den nicht resolvten Dateien abschneiden.
		arg = arg.substring(arg.indexOf("\n") + 1);
		arg = arg.substring(0, arg.indexOf("The following files have NOT been resolved:"));

		// Am Zeilenumbruch trennen und störende Leerzeichen entfernen.
		dependencies = Stream.of(arg.split("\n")).map(s -> s.strip()).collect(Collectors.toList());
		return dependencies;
	}
}
