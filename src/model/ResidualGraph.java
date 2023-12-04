package model;

import graph.Edge;
import graph.SimpleGraph;
import graph.Vertex;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

public class ResidualGraph {
	private Hashtable<String, ResidualVertex> vertices;
	private Hashtable<String, ResidualEdge> edges;

	// Constructor to create a residual graph from a given SimpleGraph
	public ResidualGraph(SimpleGraph graph) throws Exception {
		this.vertices = new Hashtable<>();
		this.edges = new Hashtable<>();

		// Iterate through the vertices of the original graph and create residual vertices
		Iterator vertexIterator = graph.vertices();
		while (vertexIterator.hasNext()) {
			Vertex vertex = (Vertex) vertexIterator.next();
			ResidualVertex flowVertex = new ResidualVertex((String) vertex.getName());
			this.addVertex(flowVertex);
		}

		// Iterate through the edges of the original graph and create residual edges
		Iterator edgeIterator = graph.edges();
		while (edgeIterator.hasNext()) {
			Edge edge = (Edge) edgeIterator.next();

			ResidualVertex origin = this.vertices.get(edge.getFirstEndpoint().getName());
			ResidualVertex dest = this.vertices.get(edge.getSecondEndpoint().getName());
			double capacity = (double) edge.getData();

			this.addEdge(origin, dest, capacity);
		}
	}

	// Getter for retrieving collection of vertices in the residual graph
	public Collection<ResidualVertex> getVertices() {
		return this.vertices.values();
	}

	// Method to add a residual vertex to the graph
	public void addVertex(ResidualVertex vertex) {
		this.vertices.put(vertex.getIdentifier(), vertex);
	}

	// Method to add a residual edge between given origin and destination vertices with a specified capacity
	public void addEdge(ResidualVertex origin, ResidualVertex dest, double capacity) throws Exception {
		ResidualEdge edge = new ResidualEdge(origin, dest, capacity);
		origin.addEdge(edge);
		this.edges.put(edge.getIdentifier(), edge);
	}

	// Getter to retrieve the source vertex of the graph
	public ResidualVertex getSource() {
		return this.getVertex("s");
	}

	// Getter to retrieve the sink vertex of the graph
	public ResidualVertex getSink() {
		return this.getVertex("t");
	}

	// Method to retrieve a specific vertex by its name
	public ResidualVertex getVertex(String name) {
		return this.vertices.get(name);
	}

	// Method to reset the visited status of all vertices in the graph
	public void clearVisitedStatus() {
		for (ResidualVertex vertex : this.vertices.values()) {
			vertex.clearVisitedStatus();
		}
	}
}

