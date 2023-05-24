package aero.minova.cas.setup.dependency;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import org.jgrapht.Graph;
import org.jgrapht.alg.cycle.CycleDetector;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.EdgeReversedGraph;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class DirectedGraphDemo2 {
	/**
	 * The starting point for the demo.
	 *
	 * @param args ignored.
	 */

	public static void main(String args[]) throws IOException {
		// @example:main:begin
		// constructs a directed graph with the specified vertices and edges
		Graph<String, DefaultEdge> directedGraph =
				new DefaultDirectedGraph<>(DefaultEdge.class);
		directedGraph.addVertex("data.schema.app");
		directedGraph.addVertex("app.i18n");
		directedGraph.addVertex("contact");
		directedGraph.addVertex("invoice");
		directedGraph.addVertex("cas.app");
		directedGraph.addVertex("oiltanking.swb");
		directedGraph.addVertex("build");

		directedGraph.addEdge("data.schema.app", "app.i18n");
		directedGraph.addEdge("data.schema.app", "build");
		directedGraph.addEdge("data.schema.app", "cas.app");

		directedGraph.addEdge("app.i18n", "build");

		directedGraph.addEdge("cas.app", "build");

		directedGraph.addEdge("contact", "build");
		directedGraph.addEdge("contact", "data.schema.app");

		directedGraph.addEdge("invoice", "contact");
		directedGraph.addEdge("invoice", "build");
		directedGraph.addEdge("invoice", "cas.app");

		directedGraph.addEdge("oiltanking.swb", "build");
		directedGraph.addEdge("oiltanking.swb", "app.i18n");
		directedGraph.addEdge("oiltanking.swb", "data.schema.app");
		directedGraph.addEdge("oiltanking.swb", "invoice");
		directedGraph.addEdge("oiltanking.swb", "cas.app");

		directedGraph = new EdgeReversedGraph<>(directedGraph);
		detectCycles(directedGraph);

		dumpPicture(directedGraph, 0);
		directedGraph.removeVertex("build");

		List<String> orderedDeps = orderDependencies(directedGraph);
		System.err.println("---------------");

		for (String dep : orderedDeps)
			System.err.println(dep);
	}

	private static List<String> orderDependencies(Graph<String, DefaultEdge> graph) throws IOException {
		List<String> orderedDependencies = new ArrayList<>(graph.vertexSet().size());
		int i = 0;

		while (!graph.vertexSet().isEmpty()) {
			dumpPicture(graph, ++i);
			List<String> dependenciesWithoutFurtherDependencies = findVerticesWithoutIncomingEdges(graph);

			if (dependenciesWithoutFurtherDependencies.isEmpty())
				throw new RuntimeException("Cyclic link?");
			Collections.sort(dependenciesWithoutFurtherDependencies);
			orderedDependencies.addAll(dependenciesWithoutFurtherDependencies);
			graph.removeAllVertices(dependenciesWithoutFurtherDependencies);
		}
		return orderedDependencies;
	}

	private static void dumpPicture(Graph<String, DefaultEdge> graph, int index) throws IOException {
		JGraphXAdapter<String, DefaultEdge> graphAdapter =
				new JGraphXAdapter<>(new EdgeReversedGraph<>(graph));
		mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
		layout.execute(graphAdapter.getDefaultParent());

		BufferedImage image =
				mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
		File imgFile = new File("/Users/scholz/Downloads/000graph-" + index + ".png");
		ImageIO.write(image, "PNG", imgFile);
	}

	private static List<String> findVerticesWithoutIncomingEdges(Graph<String, DefaultEdge> graph) {
		List<String> verticesWithoutIncomingEdges = new ArrayList<>();

		// Iterate over all vertices in the graph
		Set<String> vertices = graph.vertexSet();
		for (String vertex : vertices) {
			// Check if there are any Incoming edges from the vertex
			if (graph.incomingEdgesOf(vertex).isEmpty()) {
				verticesWithoutIncomingEdges.add(vertex);
			}
		}

		return verticesWithoutIncomingEdges;
	}

	private static boolean detectCycles(Graph<String, DefaultEdge> graph) {
		CycleDetector<String, DefaultEdge> cycleDetector = new CycleDetector<>(graph);

		boolean cyclesDetected = cycleDetector.detectCycles();
		if (cycleDetector.detectCycles()) {
			Set<String> cycleVertices = cycleDetector.findCycles();

			System.err.println("Cycles within vertices detected: " + String.join(", ", cycleVertices));
		}
		return cyclesDetected;
	}
}
