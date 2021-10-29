package aero.minova.core.application.system.setup.dependency;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DependencyGraph implements Serializable {
	private List<Dependency> dependencies = new ArrayList<>();

	public List<Dependency> getDependencies() {
		return dependencies;
	}
}
