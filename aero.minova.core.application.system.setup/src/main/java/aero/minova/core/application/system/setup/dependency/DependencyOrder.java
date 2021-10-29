package aero.minova.core.application.system.setup.dependency;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DependencyOrder {
	public static List<String> determineDependencyOrder(String json) throws Exception {
		final List<Dependency> dependencies = new Gson().fromJson(json, DependencyGraph.class)
				.getDependencies();
		final Map<String, Set<String>> dependencyMapping = new HashMap<>();
		dependencies.forEach(d -> {
			final String from = standardizeId(d.getFrom());
			final String to = standardizeId(d.getTo());
			if (!dependencyMapping.containsKey(from)) {
				dependencyMapping.put(from, new HashSet<>());
			}
			dependencyMapping.get(from).add(to);
		});
		final List<String> dependencyOrder = extractReadyDependencies(dependencyMapping);
		while (!dependencyMapping.isEmpty()) {
			final String nextDependency = getNextDependency(dependencyMapping);
			removeDependency(dependencyMapping, nextDependency);
			dependencyOrder.add(nextDependency);
		}
		return dependencyOrder;
	}

	private static List<String> extractReadyDependencies(Map<String, Set<String>> dependencyMapping) {
		final List<String> readyDepdenencies = new ArrayList<>();
		dependencyMapping.values().forEach(e -> {
			e.forEach(i -> {
				if (!dependencyMapping.containsKey(i) && !readyDepdenencies.contains(i)) {
					readyDepdenencies.add(i);
				}
			});
		});
		for (String readyDepdenency : readyDepdenencies) {
			dependencyMapping.values().forEach(e -> e.remove(readyDepdenency));
		}
		return readyDepdenencies;
	}

	private static void removeDependency(Map<String, Set<String>> dependencyMapping, String dependency) {
		dependencyMapping.remove(dependency);
		dependencyMapping.values().forEach(d -> d.remove(dependency));
	}

	private static String getNextDependency(Map<String, Set<String>> dependencyMapping) {
		for (String dependency : dependencyMapping.keySet()) {
			if (dependencyMapping.get(dependency).isEmpty()) {
				return dependency;
			}
		}
		throw new IllegalArgumentException("Dependency circle found: " + dependencyMapping);
	}

	private static String standardizeId(String id) {
		return id.substring(0, id.indexOf(":jar")).replace(":", ".").strip();
	}
}
