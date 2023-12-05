package algorithms;

import java.util.HashSet;
import java.util.LinkedList;

import model.ResidualEdge;
import model.ResidualGraph;
import model.ResidualVertex;
import graph.SimpleGraph;

/**
 * <h3>Pre Flow Push Algorithm</h3>
 * This class implements the pre-flow push algorithm to find the maximum flow in a graph.
 * The algorithm is used to calculate the maximum flow from a source to a sink in a flow network.
 * It works by iteratively pushing flow along augmenting paths in the residual graph until no such paths exist.
 *
 * @author Preethika Pradeep, Malavika Suresh
 * @version 1.0
 * @since 2017-12-01
 */
public class PreFlowPush {

    /**
     * Finds the maximum flow in the given graph using the pre-flow push algorithm.
     *
     * @param graph Flow network represented by a graph.
     * @return Maximum flow in the given graph.
     * @throws Exception If an error occurs during the flow calculation.
     */
    public double findMaxFlow(SimpleGraph graph) throws Exception {
        // Convert the input graph to a residual graph
        ResidualGraph residualGraph = new ResidualGraph(graph);
        ResidualVertex source = residualGraph.getSource();

        // Data structures to track active vertices and visited vertices during the algorithm
        LinkedList<ResidualVertex> activeVertices = new LinkedList<>();
        HashSet<String> visitedVertices = new HashSet<>();

        // Initialize the source vertex and enqueue its neighbors
        initializeSourceVertex(residualGraph, source, activeVertices, visitedVertices);

        // Main loop of the pre-flow push algorithm
        while (!activeVertices.isEmpty()) {
            // Get the next active vertex
            ResidualVertex currentVertex = pollActiveVertex(activeVertices, visitedVertices);

            // Find an edge with less height in the residual graph
            ResidualEdge lessHeightNeighborEdge = currentVertex.getLessHeightNeighborEdge();

            if (lessHeightNeighborEdge == null) {
                // No neighbor with less height, relabel the current vertex
                relabelVertex(currentVertex, activeVertices, visitedVertices);
            } else {
                // Push flow along the found augmenting path
                double flowIncrement = Math.min(lessHeightNeighborEdge.getResidualCapacity(),
                        currentVertex.getExcess());
                pushFlow(lessHeightNeighborEdge, currentVertex, flowIncrement, activeVertices, visitedVertices);
            }
        }

        // Return the total outgoing capacity from the source vertex as the maximum flow
        return source.calculateTotalOutgoingCapacity();
    }

    /**
     * Initializes the source vertex by setting its height and pushing flow to its neighbors.
     *
     * @param graph           Residual graph representing the flow network.
     * @param source          Source vertex of the flow network.
     * @param activeVertices  List of active vertices in the algorithm.
     * @param visitedVertices Set of vertices that have been visited during the algorithm.
     */
    private void initializeSourceVertex(ResidualGraph graph, ResidualVertex source,
            LinkedList<ResidualVertex> activeVertices, HashSet<String> visitedVertices) {
        // Set the height of the source vertex to the number of vertices in the graph
        source.setHeight(graph.numberOfVertices());

        // Push flow to the neighbors of the source vertex and enqueue them if not visited
        for (ResidualEdge edge : source.getEdges()) {
            try {
                edge.increaseFlow(edge.getResidualCapacity());
            } catch (Exception e) {
                e.printStackTrace();
            }
            enqueueActiveVertexIfNotVisited(edge.getDestination(), activeVertices, visitedVertices);
        }
    }

    /**
     * Relabels the vertex by incrementing its height and enqueues it if not visited.
     *
     * @param vertex           Vertex to be relabeled.
     * @param activeVertices  List of active vertices in the algorithm.
     * @param visitedVertices Set of vertices that have been visited during the algorithm.
     */
    private void relabelVertex(ResidualVertex vertex, LinkedList<ResidualVertex> activeVertices,
            HashSet<String> visitedVertices) {
        // Increment the height of the vertex
        vertex.incrementHeight();
        // Enqueue the vertex if not visited
        enqueueActiveVertexIfNotVisited(vertex, activeVertices, visitedVertices);
    }

    /**
     * Pushes flow along the given edge and updates excess, enqueuing vertices if necessary.
     *
     * @param edge             Residual edge through which flow is pushed.
     * @param vertex           Vertex from which flow is pushed.
     * @param increment        Amount of flow to be pushed.
     * @param activeVertices  List of active vertices in the algorithm.
     * @param visitedVertices Set of vertices that have been visited during the algorithm.
     */
    private void pushFlow(ResidualEdge edge, ResidualVertex vertex, double increment,
            LinkedList<ResidualVertex> activeVertices, HashSet<String> visitedVertices) {
        try {
            // Increase the flow along the edge by the specified increment
            edge.increaseFlow(increment);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Enqueue the source vertex of the edge if there is still excess flow
        if (edge.getSource().getExcess() > 0) {
            enqueueActiveVertexIfNotVisited(edge.getSource(), activeVertices, visitedVertices);
        }

        // Enqueue the destination vertex of the edge if there is still excess flow
        if (edge.getDestination().getExcess() > 0) {
            enqueueActiveVertexIfNotVisited(edge.getDestination(), activeVertices, visitedVertices);
        }
    }

    /**
     * Enqueues the vertex if it is not visited and not a source or sink vertex.
     *
     * @param vertex           Vertex to be enqueued.
     * @param activeVertices  List of active vertices in the algorithm.
     * @param visitedVertices Set of vertices that have been visited during the algorithm.
     */
    private void enqueueActiveVertexIfNotVisited(ResidualVertex vertex,
            LinkedList<ResidualVertex> activeVertices, HashSet<String> visitedVertices) {
        // Enqueue the vertex if it is not a source or sink and not visited
        if (!vertex.isSourceOrSink() && !visitedVertices.contains(vertex.getIdentifier())) {
            visitedVertices.add(vertex.getIdentifier());
            activeVertices.addLast(vertex);
        }
    }

    /**
     * Polls the next active vertex from the list of active vertices and marks it as visited.
     *
     * @param activeVertices  List of active vertices in the algorithm.
     * @param visitedVertices Set of vertices that have been visited during the algorithm.
     * @return The next active vertex.
     */
    private ResidualVertex pollActiveVertex(LinkedList<ResidualVertex> activeVertices,
            HashSet<String> visitedVertices) {
        // Poll the next active vertex and mark it as visited
        ResidualVertex vertex = activeVertices.poll();
        visitedVertices.add(vertex.getIdentifier());
        return vertex;
    }
}
