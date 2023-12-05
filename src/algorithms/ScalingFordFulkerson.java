package algorithms;

import java.util.*;

import Utils.GraphUtility;
import graph.SimpleGraph;
import model.*;

/**
 * This class implements the Scaling-Ford-Fulkerson algorithm for calculating the maximum flow associated
 * with a flow network. It operates on a residual graph
 * and uses delta-scaling to determine the minimum residual capacity.
 * The algorithm repeatedly finds augmenting paths with decreasing minResidualCapacity
 * until no more paths can be found, thereby calculating the maximum flow in the network.
 *
 * @author Niharika Nasam
 */
 public class ScalingFordFulkerson{

	/**
	 * Computes the maximum flow in the given SimpleGraph using the scaling Ford-Fulkerson algorithm.
	 * This method constructs a residual graph based on the input graph, determines the initial
	 * minimum residual capacity as a power of 2 (delta in the scaling Ford-Fulkerson algorithm),
	 * and performs the scaling Ford-Fulkerson algorithm by decreasing minResidualCapacity by 2 in each iteration.
	 *
	 * @param graph The input graph for which maximum flow needs to be calculated
	 * @return The maximum flow value in the graph
	 * @throws Exception If an error occurs during the computation
	 */
	public double getMaxFlow(SimpleGraph graph) throws Exception {
		// Create a residual graph based on the input graph
		ResidualGraph residualGraph = new ResidualGraph(graph);

		// Get the source vertex from the residual graph
		ResidualVertex sourceVertex = residualGraph.getSource();

		// Calculate the total capacity of outgoing edges from the source vertex
		double srcOutCapTotal = sourceVertex.calculateTotalOutgoingCapacity();

		// Determine the initial minimum residual capacity as a power of 2 which is delta in scaling ford fulkerson algorithm
		double minResidualCapacity = Math.pow(2, Math.floor(Math.log(srcOutCapTotal) / Math.log(2)));

		// Perform scaling Ford-Fulkerson algorithm with decreasing minResidualCapacity
		while (minResidualCapacity >= 1) {
			// Calculate maximum flow using the current minResidualCapacity
			computeMaxFlowWithMinimumCapacity(residualGraph, minResidualCapacity);

			// Decrease minResidualCapacity for the next iteration
			minResidualCapacity /= 2;
		}

		// Return the total outgoing flow from the source vertex
		return sourceVertex.calculateTotalOutgoingFlow();
	}

	/**
	 * Computes the maximum flow in the given residual graph using the scaling Ford-Fulkerson algorithm
	 * with a specified minimum residual capacity constraint.
	 *
	 * @param graph               The residual graph for which maximum flow needs to be computed
	 * @param minResidualCapacity The minimum residual capacity for augmenting paths
	 * @throws Exception          If an error occurs during the computation
	 */
	private void computeMaxFlowWithMinimumCapacity(ResidualGraph graph, double minResidualCapacity) throws Exception {

		LinkedList<ResidualEdge> path;

		// Iteratively find augmenting paths with a minimum residual capacity
		do {
			// Find an augmenting path with a minimum residual capacity
			path = GraphUtility.findPathToSink(graph,graph.getSource(),true,minResidualCapacity);

			// Clear visited status of the graph to prepare for the next path search
			GraphUtility.resetGraphVisitedStatus(graph);

			// If an augmenting path is found, update the flow along the path
			if(path!=null){
				// Calculate the bottleneck which is minimum capacity of the path
				double bottleneck = GraphUtility.calculateBottleneck(path);

				// Increase flow on the path with the bottleneck value
				GraphUtility.updateFlowOnPath(path, bottleneck);
			}

		} while (path != null);
	}
}