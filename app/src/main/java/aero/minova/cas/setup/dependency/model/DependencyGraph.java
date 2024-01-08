package aero.minova.cas.setup.dependency.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON-Format für die `dependency-graph.json`,
 * welche alle Abhängigkeiten eines Moduls enthält.
 */
public class DependencyGraph implements Serializable {
	private List<Dependency> dependencies = new ArrayList<>();

	public List<Dependency> getDependencies() {
		return dependencies;
	}
}
