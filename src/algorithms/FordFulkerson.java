package algorithms;
import java.util.LinkedList;

import Utils.GraphUtility;
import graph.SimpleGraph;
import model.*;

/**
 * This class implements the Ford-Fulkerson algorithm for calculating the maximum flow associated with a flow network
 * It used residual graph to find augmenting paths and increase the flow.
 * The algorithm continuously finds paths from the source to the sink with available capacity and augments
 * the flow along these paths until no more paths can be found.
 *
 * @author Shriya Hireholi
 */
public class FordFulkerson {

	/**
	 * This method computes the maximum flow associated with a flow network represented by the instance of SimpleGraph
	 * It uses FordFulkerson algorithm to compute the maximum flow
	 * @param graph - an instance of simple graph
	 * @return max flow value associated with the given graph
	 *
	 * @throws Exception
	 */
	public double getMaxFlow(SimpleGraph graph) throws Exception {
		ResidualGraph residualGraph = new ResidualGraph(graph);
		ResidualVertex source = residualGraph.getSource();
		LinkedList<ResidualEdge> path;
		// Iterate until a path from source to destination no longer exists
		do {
			path = GraphUtility.findPathToSink(residualGraph, source,false,0);
			GraphUtility.resetGraphVisitedStatus(residualGraph);

			if (path != null) {
				// Calculate the bottleneck which is minimum capacity of the path
				double bottleneck = GraphUtility.calculateBottleneck(path);

				// Update the flow on the path with the bottleneck value
				GraphUtility.updateFlowOnPath(path, bottleneck);
			}
		} while (path != null);

		// Return the outgoing flow from the source vertex
		return source.calculateTotalOutgoingFlow();
	}
}
