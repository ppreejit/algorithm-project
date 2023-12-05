import algorithms.*;
import graph.*;

import java.io.File;

public class AlgorithmExecutor {

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
	}

	public static void main(String[] args) throws Exception {
		String filePath = args[0];
		if(null!= filePath){
			executeAlgorithm(filePath);
		}else{
			System.err.println("please enter filePath");
		}
	}
}
