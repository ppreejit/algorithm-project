package algorithms;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;

import graph.SimpleGraph;
import model.ResidualEdge;
import model.ResidualGraph;
import model.ResidualVertex;

/**
 * <h3>PreFlowPush Algorithm</h3> Implements the Preflow Push algorithm to find
 * the maximum flow in a graph. The algorithm calculates the maximum flow from a
 * source to a sink in a flow network. It works by iteratively pushing flow
 * along augmenting paths in the residual graph until no such paths exist.
 *
 * @author Preethika Pradeep, Malavika Suresh
 * @version 1.0
 * @since 12-05-2023
 */
public class PreFlowPush {

	// Data structures to track vertices with excess flow and visited vertices
	// Using ArrayDeque for efficient adding/removing from both ends
	Deque<ResidualVertex> verticesWithExcess = new ArrayDeque<>();

	// Use HashSet for quick lookup time
	HashSet<String> visitedVertices = new HashSet<>();

	/**
	 * Calculates the maximum flow in the given graph using the Preflow Push
	 * algorithm.
	 *
	 * @param graph Input graph representing the flow network.
	 * @return Maximum flow value in the given graph.
	 * @throws Exception If an error occurs during the flow calculation.
	 */
	public double calculateMaxFlow(SimpleGraph graph) throws Exception {

		// Convert the input graph to a residual graph
		// Residual graph tracks available capacity
		ResidualGraph residualGraph = new ResidualGraph(graph);
		ResidualVertex sourceVertex = residualGraph.getSource();

		// Initialize the flow and add adjacent vertices of the source
		initializeMaxFlow(residualGraph, sourceVertex, verticesWithExcess, visitedVertices);

		// Main loop of the Preflow Push algorithm
		while (!verticesWithExcess.isEmpty()) {
			ResidualVertex currentVertex = verticesWithExcess.poll();
			visitedVertices.remove(currentVertex.getIdentifier());

			ResidualEdge adjacentEdge = currentVertex.getLowerHeightNeighborEdge();

			if (adjacentEdge == null) {
				// No eligible edges, relabel vertex
				relabelVertex(currentVertex, verticesWithExcess, visitedVertices);
			} else {
				// Push flow along edge
				pushFlow(currentVertex, adjacentEdge);
				// Add updated vertices
				addExcessVertex(verticesWithExcess, visitedVertices, adjacentEdge.getSource());
				addExcessVertex(verticesWithExcess, visitedVertices, adjacentEdge.getDestination());
			}
		}

		// Return the total outgoing flow from the source vertex as the maximum flow
		return sourceVertex.calculateTotalOutgoingFlow();
	}

	/**
	 * Initializes the flow and adds the adjacent vertices of the source.
	 *
	 * @param residualGraph  Residual graph representing the flow network.
	 * @param sourceVertex   Source vertex of the flow network.
	 * @param excessVertices List of vertices with excess flow.
	 * @param visitedSet     Set of visited vertices during the algorithm.
	 * @throws Exception If an error occurs during the initialization.
	 */
	private void initializeMaxFlow(ResidualGraph residualGraph, ResidualVertex sourceVertex,
			Deque<ResidualVertex> excessVertices, HashSet<String> visitedSet) throws Exception {
		// Initialize source height
		sourceVertex.setHeight(residualGraph.numberOfVertices());
		// Saturate all edges from source
		for (ResidualEdge edge : sourceVertex.getEdges()) {
			edge.increaseFlow(edge.getResidualCapacity());
			addExcessVertex(excessVertices, visitedSet, edge.getDestination());
		}
	}

	/**
	 * Relabels the vertex by incrementing its height.
	 *
	 * @param vertex         Vertex to be relabeled.
	 * @param excessVertices List of vertices with excess flow.
	 * @param visitedSet     Set of visited vertices during the algorithm.
	 */
	private void relabelVertex(ResidualVertex vertex, Deque<ResidualVertex> excessVertices,
			HashSet<String> visitedSet) {
		vertex.incrementHeight();
		addUnvisitedVertex(excessVertices, visitedSet, vertex);
	}

	/**
	 * Pushes flow along the given edge.
	 *
	 * @param vertex Vertex from which flow is pushed.
	 * @param edge   Residual edge through which flow is pushed.
	 * @throws Exception If an error occurs during the flow push.
	 */
	private void pushFlow(ResidualVertex vertex, ResidualEdge edge) throws Exception {
		double flowIncrement = Math.min(edge.getResidualCapacity(), vertex.getExcess());
		edge.increaseFlow(flowIncrement);
	}

	/**
	 * Adds the vertex if it is not already visited.
	 *
	 * @param excessVertices List of vertices with excess flow.
	 * @param visitedSet     Set of visited vertices during the algorithm.
	 * @param vertex         Vertex to be enqueued.
	 */
	private void addUnvisitedVertex(Deque<ResidualVertex> excessVertices, HashSet<String> visitedSet,
			ResidualVertex vertex) {
		if (!vertex.isSourceOrSink() && visitedSet.add(vertex.getIdentifier())) {
			excessVertices.addLast(vertex);
		}
	}

	/**
	 * Adds the vertex if it has excess flow.
	 *
	 * @param excessVertices List of vertices with excess flow.
	 * @param visitedSet     Set of visited vertices during the algorithm.
	 * @param vertex         Vertex to be added if it has excess flow.
	 */
	private void addExcessVertex(Deque<ResidualVertex> excessVertices, HashSet<String> visitedSet,
			ResidualVertex vertex) {
		if (vertex.getExcess() > 0) {
			addUnvisitedVertex(excessVertices, visitedSet, vertex);
		}
	}
}
