package algorithms;

import java.util.*;
import java.io.*;
import graph.*;
import model.*;

/**
 * <h3>Pre Flow Push algorithm</h3> This class implements pre flow push
 * algorithm to find maximum flow in a graph.
 * 
 * @author Preethika Pradeep, Malavika Suresh
 * @version 1.0
 * @since 2017-12-01
 */
public class PreFlowPush {
	/**
	 * Calculates max flow in given graph using pre flow push algorithm.
	 * 
	 * @param simpleGraph Graph in which to find max flow.
	 * @return Value of max flow in the given graph.
	 */
	public double findMaxFlow(SimpleGraph simpleGraph) throws Exception {
		ResidualGraph graph = new ResidualGraph(simpleGraph);
		ResidualVertex source = graph.getSource();
		ResidualVertex sink = graph.getSink();

		LinkedList<ResidualVertex> positiveExcessVertices = new LinkedList<ResidualVertex>();
		HashSet<String> verticesInQueue = new HashSet<String>();

		// Initial conditions
		source.setHeight(graph.numberOfVertices());
		for (ResidualEdge edge : source.getEdges()) {
			edge.increaseFlow(edge.getResidualCapacity());
			this.addVertexIfNotPresent(positiveExcessVertices, verticesInQueue, edge.getDestination());
		}

		while (!positiveExcessVertices.isEmpty()) {
			ResidualVertex vertex = this.remove(positiveExcessVertices, verticesInQueue);
			ResidualEdge edge = vertex.getLessHeightNeighborEdge();
			if (edge == null) {
				// No neighbor with less height, relabel
				vertex.incrementHeight();

				// Add vertex back
				this.addVertexIfNotPresent(positiveExcessVertices, verticesInQueue, vertex);
			} else {
				double increment = Math.min(edge.getResidualCapacity(), vertex.getExcess());
				edge.increaseFlow(increment);

				// Add origin if there is still excess
				if (edge.getSource().getExcess() > 0) {
					this.addVertexIfNotPresent(positiveExcessVertices, verticesInQueue, edge.getSource());
				}

				// Add dest if excess > 0
				if (edge.getDestination().getExcess() > 0) {
					this.addVertexIfNotPresent(positiveExcessVertices, verticesInQueue, edge.getDestination());
				}
			}
		}

		return source.calculateTotalOutgoingFlow();
	}

	/**
	 * Add vertex to the list containing vertices with positive excess if vertex is
	 * not already present.
	 * 
	 * @param positiveExcessVertices List of vertices with positive excess
	 * @param verticesInQueue        Names of vertices present in list containing
	 *                               vertices with positive excess.
	 * @param vertex                 Vertox to add in the list.
	 */
	private void addVertexIfNotPresent(LinkedList<ResidualVertex> positiveExcessVertices,
			HashSet<String> verticesInQueue, ResidualVertex vertex) {

		if (vertex.isSourceOrSink()) {
			return;
		}

		if (verticesInQueue.add(vertex.getIdentifier())) {
			positiveExcessVertices.addLast(vertex);
		}
	}

	private ResidualVertex remove(LinkedList<ResidualVertex> positiveExcessVertices, HashSet<String> verticesInQueue) {
		ResidualVertex vertex = positiveExcessVertices.remove();
		verticesInQueue.remove(vertex.getIdentifier());
		return vertex;
	}
}
