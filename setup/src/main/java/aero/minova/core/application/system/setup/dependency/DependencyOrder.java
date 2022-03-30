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
	private DependencyOrder() {

	}

	/**
	 * Bestimmt die Reihenfolge in der die Abhängigkeiten eines APP-Projektes installiert werden können.
	 * Kreis-Abhängigkeiten werden nicht unterstützt.
	 * Es werden nur APP-Projekte als Abhängigkeiten gelistet,
	 * weswegen `aero.minova.app.build` aus der Abhängigkeits-Liste entfernt wird.
	 *
	 * @param json Dies ist ein JSON-Dokument, welche die Abhängigkeiten eines Maven-APP-Projektes darstellt.
	 *             Dabei müssen Konflikte der Abhängigkeiten in dem Dokument vorhanden sein,
	 *             da sonst in dem Dokument für ein Modul nicht alle transitiven Abhängigkeiten stehen.
	 *             Das eigentliche Modul, für das die Installtions-Reihenfolge bestimmt wird,
	 * 	           wird nicht mit ausgegeben.
	 * @return Liste der Abhängigkeit aufsteigend nach der Installations-Reihenfolge sortiert.
	 */
	public static List<String> determineDependencyOrder(String json) {
		try {
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
				if (!"aero.minova.app.build".equals(nextDependency)) {
					dependencyOrder.add(nextDependency);
				}
			}
			// Das Modul entfernen, für welche die Abhängigkeiten bestimmt werden.
			dependencyOrder.remove(dependencyOrder.size() - 1);
			return dependencyOrder;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Entfernt Abhängigkeiten aus der Liste,
	 * welche nur Abhängigkeiten enthalten,
	 * die ihrerseits keine Abhängigkeiten haben.
	 *
	 * @param dependencyMapping Mapping von Modulen zu ihren Abhängigkeiten.
	 * @return Die entfernten Abhängigkeiten.
	 */
	private static List<String> extractReadyDependencies(Map<String, Set<String>> dependencyMapping) {
		final List<String> readyDependencies = new ArrayList<>();
		dependencyMapping.values().forEach(e -> {
			e.forEach(i -> {
				if (!dependencyMapping.containsKey(i) //
						&& !readyDependencies.contains(i) //
						&& !"aero.minova.app.build".equals(i)) {
					readyDependencies.add(i);
				}
			});
		});
		for (String readyDepdenency : readyDependencies) {
			dependencyMapping.values().forEach(e -> e.remove(readyDepdenency));
		}
		dependencyMapping.values().forEach(e -> e.remove("aero.minova.app.build"));
		return readyDependencies;
	}

	/**
	 * Entfernt eine Abhängigkeit aus der Map vollständig.
	 *
	 * @param dependencyMapping Mapping von Modulen zu ihren Abhängigkeiten.
	 * @param dependency Das Modul, welches entfernt werden soll.
	 */
	private static void removeDependency(Map<String, Set<String>> dependencyMapping, String dependency) {
		dependencyMapping.remove(dependency);
		dependencyMapping.values().forEach(d -> d.remove(dependency));
	}

	/**
	 * Bestimmt die nächste Abhängigkeit,
	 * die ihrerseits keine Abhängigkeit enthält.
	 *
	 * @param dependencyMapping Mapping von Modulen zu ihren Abhängigkeiten.
	 * @return Die nächste freihe Abhängigkeit.
	 */
	private static String getNextDependency(Map<String, Set<String>> dependencyMapping) {
		for (String dependency : dependencyMapping.keySet()) {
			if (dependencyMapping.get(dependency).isEmpty()) {
				return dependency;
			}
		}
		throw new IllegalArgumentException("Dependency circle found: " + dependencyMapping);
	}

	/**
	 * Konvertiert einen String der Form "<groupId>:<artifactId>:<packaging>:<classifier>" zu der Form "<groupId>.<artifactId>".
	 *
	 * @param id Der zu konvertierende String.
	 * @return Der konvertierte String.
	 */
	private static String standardizeId(String id) {
		return id.substring(0, id.indexOf(":jar")).replace(":", ".").strip();
	}
}
