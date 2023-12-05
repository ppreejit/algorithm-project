import algorithms.FordFulkerson;
import algorithms.PreFlowPush;
import algorithms.ScalingFordFulkerson;
import graph.GraphInput;
import graph.SimpleGraph;

import java.io.File;

public class BulkAlgorithmExecutor {
    private static void searchTxtFiles(File folder) throws Exception {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    searchTxtFiles(file); // Recursively search subfolders
                } else if (file.isFile() && file.getName().toLowerCase().endsWith(".txt")) {
                    executeAlgorithm(file.getAbsolutePath());
                }
            }
        }
    }

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
		double pfp_maxFlow = preFlowPush.findMaxFlow(graph);
		long pfp_endTime = System.currentTimeMillis();
		long pfp_duration = pfp_endTime - pfp_startTime;
		System.out.println(" Max flow value is: " + pfp_maxFlow + " computed in " + pfp_duration + " ms");

    }
    public static void main(String[] args) throws Exception {
        String folderPath = args[0];
        File folder = new File(folderPath);
        if(folder.exists() && folder.isDirectory()){
            searchTxtFiles(folder);
        }else{
            System.err.println("folder path is invalid: "+folderPath);
        }
    }
}
