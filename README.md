## Emperical Study of Network Flow Algorithms

This project encompasses the implementation of Ford-Fulkerson, Scaling Ford-Fulkerson, and Preflow-push algorithms using Java to determine the maximum flow within a network. 
Additionally, it involves an empirical analysis conducted on various types of graphs including Random, Bipartite, Fixed Degree, and Mesh graphs. 
The primary objective was to explore and study the influence of different parameters affecting graph generation on the runtime performance of the implemented algorithms.

### Algorithms Implemented

Implemented the below algorithms:

- Ford-Fulkerson: A method for computing the maximum flow in a flow network.
- Scaling Ford-Fulkerson: An optimization of the Ford-Fulkerson algorithm.
- Preflow-push: An approach using preflows to compute maximum flow efficiently.

### Code Execution

<u>Individual Graph Execution</u>

AlgorithmExecutor.java takes in one input parameter which is file path.
It executes FordFulkerson, ScalingFordFulkerson and PreFlowPush algorithms on the graph present in the specified input file path

- navigate to src folder
- run javac AlgorithmExecutor.java
- run -Xss8m AlgorithmExecutor &lt;file_path&gt;

example: java -Xss8m BulkAlgorithmExecutor /Users/username/Algorithms/input-graphs/graph1.txt

<u>Bulk Graph Execution</u>

BulkAlgorithmExecutor.java takes in one input parameter which is folder path.
It executes FordFulkerson, ScalingFordFulkerson and PreFlowPush algorithms on the graphs present in the all the files specified in the input folder path

- navigate to src folder
- run javac BulkAlgorithmExecutor.java
- run -Xss8m BulkAlgorithmExecutor &lt;folder_path&gt;

example: java -Xss8m BulkAlgorithmExecutor /Users/username/Algorithms/input-graphs
