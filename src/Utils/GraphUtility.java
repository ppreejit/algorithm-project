package Utils;

import model.ResidualEdge;
import model.ResidualGraph;
import model.ResidualVertex;

import java.util.LinkedList;

/**
 * This class provides utility methods for Graph related operations
 *
 * @author Niharika Nasam, Shriya Hireholi
 */
public class GraphUtility {

    /**
     * Calculates the bottleneck which is minimum capacity along the given path of ResidualEdges.
     *
     * @param path The LinkedList of ResidualEdges representing a path
     * @return The bottleneck value, which is the lowest capacity in the given path
     */
    public static double calculateBottleneck(LinkedList<ResidualEdge> path) {
        double bottleneck = path.stream()
                .mapToDouble(ResidualEdge::getResidualCapacity)
                .min()
                .orElse(Double.MAX_VALUE);

        return bottleneck;
    }

    /**
     * Finds a path from the given source vertex to the sink vertex in the ResidualGraph.
     *
     * @param graph                 The ResidualGraph
     * @param source                The source ResidualVertex from which path needs to be found
     * @param isScalingFF           A boolean flag indicating if it's using scaling Ford-Fulkerson algorithm
     * @param minimumResidualCapacity The minimum residual capacity for edges in the path (for scaling FF)
     * @return                      A LinkedList of ResidualEdges representing the found path,
     *                              or null if no path exists from the source to the sink
     */
    public static LinkedList<ResidualEdge> findPathToSink(ResidualGraph graph, ResidualVertex source,boolean isScalingFF,double minimumResidualCapacity) {
        // Mark the source vertex as visited to avoid revisiting
        source.markVisited();

        // Check if the source and sink vertices are the same and return an empty path in this case
        if (!isScalingFF && isVertexSink(graph,source)) {
            LinkedList<ResidualEdge> path = new LinkedList<>();
            return path;
        }

        // Iterate through edges of the source vertex
        for (ResidualEdge edge : source.getEdges()) {
            double residualCapacity = edge.getResidualCapacity();
            ResidualVertex destination = edge.getDestination();

            if(!isScalingFF){
                // Check if the edge has residual capacity and if the destination vertex is not visited
                if (residualCapacity > 0 && !destination.isVisited()) {
                    // Recursively find a path to the sink starting from the destination vertex
                    LinkedList<ResidualEdge> path = findPathToSink(graph, destination,false,0);
                    // If a path is found, add the current edge to the path and return it
                    if (path != null) {
                        path.addFirst(edge);
                        return path;
                    }
                }
            }else{
                /* Check if the residual capacity of the edge satisfies the minimum required residual capacity
                this is required for scaling ford fulkerson algorithm */
                if (residualCapacity >= minimumResidualCapacity) {

                    // If the destination vertex is the sink, create a path with the current edge
                    if (isVertexSink(graph, destination)) {
                        return createPathWithSingleEdge(edge);
                    } else if (!destination.isVisited()) {
                        // If the destination is not the sink and hasn't been visited yet
                        // Recursively find a path to the sink starting from the destination vertex
                        LinkedList<ResidualEdge> path = findPathToSink(graph, destination, true, minimumResidualCapacity);

                        // If a path is found, add the current edge to the path and return it
                        if (path != null) {
                            path.addFirst(edge);
                            return path;
                        }
                    }
                }
            }

        }

        // If no path is found from this vertex, return null
        return null;
    }

    /**
     * Checks if the given ResidualVertex is the sink vertex in the ResidualGraph.
     *
     * @param graph       The ResidualGraph containing the sink vertex
     * @param destination The ResidualVertex to check if it is the sink vertex
     * @return True if the provided ResidualVertex is the sink vertex, false otherwise
     */
    public static boolean isVertexSink(ResidualGraph graph, ResidualVertex destination) {
        return destination.getIdentifier().equals(graph.getSink().getIdentifier());
    }

    /**
     * Creates a path containing a single ResidualEdge.
     *
     * @param edge The ResidualEdge to be added to the path
     * @return A LinkedList containing the provided ResidualEdge as a single-path
     */
    public static LinkedList<ResidualEdge> createPathWithSingleEdge(ResidualEdge edge) {
        LinkedList<ResidualEdge> path = new LinkedList<>();
        path.add(edge);
        return path;
    }

    /**
     * Increases the flow on each edge present in the given path by the given bottleneck value
     *
     * @param path - the path consisting edges on which flow value needs to be updated
     * @param bottleneck
     *
     * @throws Exception
     */
    public static void updateFlowOnPath(LinkedList<ResidualEdge> path, double bottleneck) throws Exception {
        for (ResidualEdge edge : path) {
            edge.updateFlow(bottleneck);
        }
    }

    /**
     * Resets the visited status of nodes or edges within the given residual graph.
     *
     * @param residualGraph The residual graph whose visited status needs to be reset
     */
    public static void resetGraphVisitedStatus(ResidualGraph residualGraph){
        residualGraph.clearVisitedStatus();
    }
}
