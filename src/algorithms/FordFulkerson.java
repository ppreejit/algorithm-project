package algorithms;
import java.util.LinkedList; // Using the LinkedList class

import graph.SimpleGraph; // necessary imports
import model.*; // importing the utils

public class FordFulkerson {

	// Finds the maximum flow in the given SimpleGraph using the Ford-Fulkerson algorithm
	public double getMaxFlow(SimpleGraph simpleGraph) throws Exception {
		ResidualGraph graph = new ResidualGraph(simpleGraph);
		ResidualVertex source = graph.getSource();

		// Iterate until no path from source to sink exists
		while (true) {
			LinkedList<ResidualEdge> path = findPathToSink(graph, source);
			graph.clearVisitedStatus();
			if (path == null) {
				break;
			}

			// Calculate the bottleneck (minimum capacity) of the path
			double bottleneck = calculateBottleneck(path);

			// Update the flow on the path with the bottleneck value
			updateFlowOnPath(path, bottleneck);
		}

		// Return the outgoing flow from the source vertex
		return source.calculateTotalOutgoingFlow();
	}

	// Calculate the bottleneck (minimum capacity) of a given path
	private double calculateBottleneck(LinkedList<ResidualEdge> path) {
		double bottleneck = Double.MAX_VALUE;
		for (ResidualEdge edge : path) {
			double residualCapacity = edge.getResidualCapacity();
			if (residualCapacity < bottleneck) {
				bottleneck = residualCapacity;
			}
		}
		return bottleneck;
	}

	// Find a path from the given origin vertex to the sink vertex in the ResidualGraph
	private LinkedList<ResidualEdge> findPathToSink(ResidualGraph graph, ResidualVertex source) {
		source.markVisited();

		for (ResidualEdge edge : source.getEdges()) {
			double residualCapacity = edge.getResidualCapacity();
			ResidualVertex destination = edge.getDestination();

			if (residualCapacity > 0) {
				// Check if the destination is the sink vertex, return the path if found
				if (destination.getIdentifier().equals(graph.getSink().getIdentifier())) {
					return createPathList(edge);
				} else if (!destination.isVisited()) {
					// Recursively explore paths to the sink vertex
					LinkedList<ResidualEdge> path = findPathToSink(graph, destination);
					if (path != null) {
						path.addFirst(edge);
						return path;
					}
				}
			}
		}
		return null;
	}

	// Create a linked list with a single edge (used for initializing a path)
	private LinkedList<ResidualEdge> createPathList(ResidualEdge edge) {
		LinkedList<ResidualEdge> path = new LinkedList<>();
		path.add(edge);
		return path;
	}

	// Increase the flow on each edge of the given path by the specified bottleneck value
	private void updateFlowOnPath(LinkedList<ResidualEdge> path, double bottleneck) throws Exception {
		for (ResidualEdge edge : path) {
			edge.updateFlow(bottleneck);
		}
	}
}
