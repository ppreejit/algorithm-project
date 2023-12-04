import algorithms.*;
import graph.*;

public class AlgorithmExecutor {

	public static void main(String[] args) throws Exception {
		SimpleGraph graph = new SimpleGraph();
		String filePath = "/Users/prudhvikishan/Niharika/Masters/Q1/Algorithms/Algo-Check-Modified/src/Max200.txt";
		String file = args[0];
		GraphInput.LoadSimpleGraph(graph, file);
		System.out.println("Vertices: " + graph.numVertices());
		System.out.println("Edges: " + graph.numEdges());


		{
			System.out.println("Ford Fulkerson");
			FordFulkerson fordFulkerson = new FordFulkerson();
			long startTime = System.currentTimeMillis();
			double maxFlow = fordFulkerson.getMaxFlow(graph);
			long endTime = System.currentTimeMillis();

			long duration = endTime - startTime;

			System.out.println(" Max flow: " + maxFlow + " in " + duration + " ms");

		}
		{
			System.out.println("Scaling Ford Fulkerson");
			ScalingFordFulkerson scalingFordFulkerson = new ScalingFordFulkerson();
			long startTime = System.currentTimeMillis();
			double maxFlow = scalingFordFulkerson.getMaxFlow(graph);
			long endTime = System.currentTimeMillis();

			long duration = endTime - startTime;

			System.out.println(" Max flow: " + maxFlow + " in " + duration + " ms");

		}
	}
}
