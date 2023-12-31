package model;

import java.util.Hashtable;

public class ResidualVertex {
	private int height;
	private String identifier; // Unique identifier for the vertex
	private Hashtable<String, ResidualEdge> edges; // Stores edges connected to this vertex
	private boolean visited; // Flag to mark if the vertex has been visited
	// excess flow, useful for preflow algorithm. For Ford Fulkerson type algorithm,
	// excess = 0
	private double excess;

	/**
	 * Constructor initializes a ResidualVertex with a unique identifier.
	 *
	 * @param identifier Unique identifier for the vertex.
	 */
	public ResidualVertex(String identifier) {
		this.identifier = identifier;
		this.edges = new Hashtable<>();
	}

	/**
	 * Method to add an edge to this vertex.
	 *
	 * @param edge The ResidualEdge to be added.
	 * @throws Exception If the source of the edge does not match the identifier of
	 *                   this vertex.
	 */
	public void addEdge(ResidualEdge edge) throws Exception {
		// Check if the source of the edge matches the identifier of this vertex
		if (!edge.getSource().getIdentifier().equals(this.identifier)) {
			throw new Exception("Adding edge " + edge.getIdentifier() + " with source vertex "
					+ edge.getSource().getIdentifier() + " on " + this.identifier);
		}

		this.edges.put(edge.getIdentifier(), edge);
	}

	/**
	 * Method to remove an edge from this vertex.
	 *
	 * @param edge The ResidualEdge to be removed.
	 */
	public void removeEdge(ResidualEdge edge) {
		this.edges.remove(edge.getIdentifier());
	}

	/**
	 * Getter method to retrieve the identifier of the vertex.
	 *
	 * @return The unique identifier of the vertex.
	 */
	public String getIdentifier() {
		return this.identifier;
	}

	/**
	 * Getter method to check if the vertex has been visited.
	 *
	 * @return True if the vertex has been visited; otherwise, false.
	 */
	public boolean isVisited() {
		return this.visited;
	}

	/**
	 * Method to mark the vertex as visited.
	 */
	public void markVisited() {
		this.visited = true;
	}

	/**
	 * Method to reset the visited status of the vertex.
	 */
	public void clearVisitedStatus() {
		this.visited = false;
	}

	/**
	 * Method to calculate the total outgoing flow from this vertex.
	 *
	 * @return The total outgoing flow from this vertex.
	 */
	public double calculateTotalOutgoingFlow() {
		double flow = 0;
		for (ResidualEdge edge : this.edges.values()) {
			if (!edge.isBackwardEdge()) {
				flow += edge.getFlow();
			}
		}
		return flow;
	}

	/**
	 * Method to calculate the total outgoing capacity from this vertex.
	 *
	 * @return The total outgoing capacity from this vertex.
	 */
	public double calculateTotalOutgoingCapacity() {
		double capacity = 0;
		for (ResidualEdge edge : this.edges.values()) {
			if (!edge.isBackwardEdge()) {
				capacity += edge.getCapacity();
			}
		}
		return capacity;
	}

	/**
	 * Method to get an array of edges connected to this vertex.
	 *
	 * @return An array of ResidualEdge objects connected to this vertex.
	 */
	public ResidualEdge[] getEdges() {
		ResidualEdge[] edgeArray = new ResidualEdge[this.edges.size()];
		if (this.edges.size() > 0) {
			this.edges.values().toArray(edgeArray);
		}
		return edgeArray;
	}

	/**
	 * Get height label of this vertex.
	 *
	 * @return Height label of this vertex.
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * Set height label of this vertex.
	 *
	 * @param height Height of the vertex.
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Get excess of this vertex.
	 *
	 * @return Excess of this vertex.
	 */
	public double getExcess() {
		return this.excess;
	}

	/**
	 * Increase excess on this vertex by given amount.
	 *
	 * @param increment Amount by which to increase excess.
	 */
	public void increaseExcess(double increment) {
		this.excess += increment;
	}

	/**
	 * Increment height label of this vertex by 1.
	 */
	public void incrementHeight() {
		this.height += 1;
	}

	/**
	 * Get edge outgoing from this vertex such that the other endpoint of that edge
	 * has height less than this vertex.
	 *
	 * @return Edge if there is a neighboring vertex with height less than this
	 *         vertex; otherwise, null.
	 */
	public ResidualEdge getLowerHeightNeighborEdge() {
		for (ResidualEdge edge : this.edges.values()) {
			if (edge.getResidualCapacity() > 0 && edge.getDestination().height < this.height) {
				return edge;
			}
		}

		return null;
	}

	/**
	 * Whether the vertex is a source or sink.
	 *
	 * @return True if the vertex is either a source or sink; otherwise, false.
	 */
	public boolean isSourceOrSink() {
		return this.identifier.equals("s") || this.identifier.equals("t");
	}

	/**
	 * Whether the vertex is a source.
	 *
	 * @return True if the vertex is a source; otherwise, false.
	 */
	public boolean isSource() {
		return this.identifier.equals("s");
	}

}
