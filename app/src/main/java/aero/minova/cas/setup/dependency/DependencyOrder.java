package aero.minova.cas.setup.dependency;

import aero.minova.cas.setup.dependency.model.Dependency;
import aero.minova.cas.setup.dependency.model.DependencyGraph;
import com.google.gson.Gson;
import org.jgrapht.Graph;
import org.jgrapht.alg.cycle.CycleDetector;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.EdgeReversedGraph;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DependencyOrder {
    private DependencyOrder() {
        throw new IllegalStateException(DependencyOrder.class.getSimpleName());
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
     *             wird nicht mit ausgegeben.
     * @return Liste der Abhängigkeit aufsteigend nach der Installations-Reihenfolge sortiert.
     */
    public static List<String> determineDependencyOrder(String json) {
        Map<String, Set<String>> dependencyRelations = extractDependencyRelationsFromJson(json);

        Graph<String, DefaultEdge> directedGraph = convertRelationsToDirectedGraph(dependencyRelations);

        checkForCycles(directedGraph);

        directedGraph.removeVertex("aero.minova.app.build");

        List<String> dependencyOrder = orderDependencies(directedGraph);

        stripOffThirdPartyDependencies(dependencyOrder);

        // Das Modul entfernen, für welche die Abhängigkeiten bestimmt werden.
        dependencyOrder.remove(dependencyOrder.size() - 1);
        return dependencyOrder;
    }

    private static List<String> orderDependencies(Graph<String, DefaultEdge> graph) {
        List<String> orderedDependencies = new ArrayList<>(graph.vertexSet().size());

        while (!graph.vertexSet().isEmpty()) {
            List<String> dependenciesWithoutFurtherDependencies = findVerticesWithoutIncomingEdges(graph);

            if (dependenciesWithoutFurtherDependencies.isEmpty())
                throw new RuntimeException("INTERNAL: Cyclic link?");
            Collections.sort(dependenciesWithoutFurtherDependencies);
            orderedDependencies.addAll(dependenciesWithoutFurtherDependencies);
            graph.removeAllVertices(dependenciesWithoutFurtherDependencies);
        }
        return orderedDependencies;
    }

    private static List<String> findVerticesWithoutIncomingEdges(Graph<String, DefaultEdge> graph) {
        List<String> verticesWithoutIncomingEdges = new ArrayList<>();

        Set<String> vertices = graph.vertexSet();
        for (String vertex : vertices) {
            if (graph.incomingEdgesOf(vertex).isEmpty()) {
                verticesWithoutIncomingEdges.add(vertex);
            }
        }

        return verticesWithoutIncomingEdges;
    }

    private static boolean checkForCycles(Graph<String, DefaultEdge> graph) {
        CycleDetector<String, DefaultEdge> cycleDetector = new CycleDetector<>(graph);

        boolean cyclesDetected = cycleDetector.detectCycles();
        if (cyclesDetected) {
            throw new RuntimeException("Cycles within following vertices detected: " + String.join(", ", cycleDetector.findCycles()));
        }
        return cyclesDetected;
    }

    private static Graph<String, DefaultEdge> convertRelationsToDirectedGraph(Map<String, Set<String>> dependencyRelations) {
        final Graph<String, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);

        for (Map.Entry<String, Set<String>> entry : dependencyRelations.entrySet()) {
            graph.addVertex(entry.getKey());
            entry.getValue().forEach(value -> {
                graph.addVertex(value);
                graph.addEdge(entry.getKey(), value);
            });
        }

        return new EdgeReversedGraph<>(graph);
    }

    private static void stripOffThirdPartyDependencies(List<String> dependencyOrder) {
        List<String> removableKeys = dependencyOrder.stream()
                .filter(d -> isThirdPartyDependency(d) || isCasInternDependency(d))
                .toList();
        dependencyOrder.removeAll(removableKeys);
    }

    private static boolean isCasInternDependency(String dependency) {
        return dependency.startsWith("aero.minova.cas.api") || dependency.startsWith("aero.minova.cas.service");
    }

    private static boolean isThirdPartyDependency(String dependency) {
        return !dependency.contains(".minova.");
    }

    private static Map<String, Set<String>> extractDependencyRelationsFromJson(String json) {
        final List<Dependency> dependencies = new Gson().fromJson(json, DependencyGraph.class)
                .getDependencies();

        final Map<String, Set<String>> dependencyMapping = new HashMap<>();
        dependencies.forEach(d -> {
            final String from = standardizeId(d.getFrom());
            final String to = standardizeId(d.getTo());

            dependencyMapping.putIfAbsent(from, new HashSet<>());
            dependencyMapping.get(from).add(to);
        });
        return dependencyMapping;
    }

    /**
     * Konvertiert einen String der Form "<groupId>:<artifactId>:<packaging>:<classifier>" zu der Form "<groupId>.<artifactId>".
     *
     * @param id Der zu konvertierende String.
     * @return Der konvertierte String.
     */
    private static String standardizeId(String id) {
        try {
            if (!id.contains(":jar")) {
                return "third-party.dependency";
            }
            return id.substring(0, id.indexOf(":jar")).replace(":", ".").strip();
        } catch (RuntimeException ex) {
            throw new RuntimeException("Could not standardize id in dependency graph for setup extension: " + id, ex);
        }
    }
}
