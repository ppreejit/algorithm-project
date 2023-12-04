/*
 * Scaling -- Ford Fulkerson
 */
package algorithms;

import java.util.*;

import graph.SimpleGraph;
import model.*;

public class ScalingFordFulkerson{

	public double getMaxFlow(SimpleGraph simpleGraph) throws Exception {
		ResidualGraph graph = new ResidualGraph(simpleGraph);
		ResidualVertex source = graph.getSource();

		// Calculate the outgoing capacity of the source vertex
		double sourceOutgoingCapacity = source.calculateTotalOutgoingCapacity();

		// Determine the initial minimum residual capacity as a power of 2
		double minResidualCapacity = 1;
		while (minResidualCapacity * 2 < sourceOutgoingCapacity) {
			minResidualCapacity *= 2;
		}

		// Perform scaling Ford-Fulkerson algorithm with decreasing minResidualCapacity
		while (minResidualCapacity >= 1) {
			calculateMaxFlowWithMinCapacity(graph, minResidualCapacity);
			minResidualCapacity /= 2;
		}

		return source.calculateTotalOutgoingFlow();
	}

	// calculate the maximum flow with minimum residual capacity
	private void calculateMaxFlowWithMinCapacity(ResidualGraph graph, double minResidualCapacity) throws Exception {
		// Iteratively find augmenting paths and increase flow
		while (true) {
			LinkedList<ResidualEdge> path = findAugmentingPathWithMinCapacity(graph, graph.getSource(), minResidualCapacity);
			graph.clearVisitedStatus();
			if (path == null) {
				break;
			}

			double bottleneck = getBottleneck(path);
			increaseFlowOnPath(path, bottleneck);
		}
	}

	// Calculate the bottleneck (minimum capacity) of a given path
	private double getBottleneck(LinkedList<ResidualEdge> path) {
		double bottleneck = Double.MAX_VALUE;
		for (ResidualEdge edge : path) {
			double residualCapacity = edge.getResidualCapacity();
			if (residualCapacity < bottleneck) {
				bottleneck = residualCapacity;
			}
		}
		return bottleneck;
	}

	// Find a path to the sink vertex with a minimum residual capacity
	private LinkedList<ResidualEdge> findAugmentingPathWithMinCapacity(
			ResidualGraph graph,
			ResidualVertex origin,
			double minResidualCapacity) {

		origin.markVisited();

		// Traverse edges to find a path with the required minimum residual capacity
		for (ResidualEdge edge : origin.getEdges()) {
			double residualCapacity = edge.getResidualCapacity();
			ResidualVertex destination = edge.getDestination();

			if (residualCapacity >= minResidualCapacity) {
				if (destination.getIdentifier().equals(graph.getSink().getIdentifier())) {
					LinkedList<ResidualEdge> path = new LinkedList<>();
					path.add(edge);
					return path;
				} else if (!destination.isVisited()) {
					LinkedList<ResidualEdge> path = findAugmentingPathWithMinCapacity(graph, destination, minResidualCapacity);
					if (path != null) {
						path.addFirst(edge);
						return path;
					}
				}
			}
		}

		return null;
	}

	// Increase the flow on each edge of the given path by the specified bottleneck value
	private void increaseFlowOnPath(LinkedList<ResidualEdge> path, double bottleneck) throws Exception {
		for (ResidualEdge edge : path) {
			edge.updateFlow(bottleneck);
		}
	}
}