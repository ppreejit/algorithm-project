import algorithms.*;
import graph.*;

import java.io.File;

/**
 * This class executes Ford-Fulkerson, Scaling-Ford-Fulkerson and Preflow-Push algorithms on a given graph file.
 */
public class AlgorithmExecutor {

	/**
	 * Executes Ford-Fulkerson, Scaling-Ford-Fulkerson and Preflow-Push algorithms on the specified file path.
	 *
	 * @param filePath The path of the file containing the graph data.
	 * @throws Exception If there's an issue while executing the algorithms.
	 */
	private static void executeAlgorithm(String filePath) throws Exception {
		SimpleGraph graph = new SimpleGraph();
		GraphInput.LoadSimpleGraph(graph, filePath);
		System.out.println(filePath);
		System.out.println("Vertices: " + graph.numVertices());
		System.out.println("Edges: " + graph.numEdges());

		System.out.println("Executing Ford Fulkerson");
		FordFulkerson fordFulkerson = new FordFulkerson();
		long ff_startTime = System.currentTimeMillis();
		double ff_maxFlow = fordFulkerson.getMaxFlow(graph);
		long ff_endTime = System.currentTimeMillis();
		long ff_duration = ff_endTime - ff_startTime;
		System.out.println(" Max flow value is: " + ff_maxFlow + " computed in " + ff_duration + " ms");

		System.out.println("Executing Scaling Ford Fulkerson");
		ScalingFordFulkerson scalingFordFulkerson = new ScalingFordFulkerson();
		long sff_startTime = System.currentTimeMillis();
		double sff_maxFlow = scalingFordFulkerson.getMaxFlow(graph);
		long sff_endTime = System.currentTimeMillis();
		long sff_duration = sff_endTime - sff_startTime;
		System.out.println(" Max flow value is: " + sff_maxFlow + " computed in " + sff_duration + " ms");
		
		System.out.println("Executing PreFlowPush");
		PreFlowPush preFlowPush = new PreFlowPush();
		long pfp_startTime = System.currentTimeMillis();
		double pfp_maxFlow = preFlowPush.calculateMaxFlow(graph);
		long pfp_endTime = System.currentTimeMillis();
		long pfp_duration = pfp_endTime - pfp_startTime;
		System.out.println(" Max flow value is: " + pfp_maxFlow + " computed in " + pfp_duration + " ms");
	}

	/**
	 * Main method that executes the algorithms based on the input file path.
	 *
	 * @param args Command-line arguments. Expects the file path as the first argument.
	 * @throws Exception If there's an issue while executing the algorithms.
	 */
	public static void main(String[] args) throws Exception {
		String filePath = args[0];
		if(null!= filePath){
			executeAlgorithm(filePath);
		}else{
			System.err.println("please enter filePath");
		}
	}
}
