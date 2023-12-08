package model;

public class ResidualEdge {
	private ResidualVertex source;
	private ResidualVertex destination;
	private double capacity;
	private double flow;
	private ResidualEdge forwardEdge;
	private ResidualEdge backwardEdge;

	/**
	 * Constructor to create a ResidualEdge between the given origin and destination
	 * vertices with a specified capacity.
	 *
	 * @param origin   The ResidualVertex representing the source vertex of the
	 *                 edge.
	 * @param dest     The ResidualVertex representing the destination vertex of the
	 *                 edge.
	 * @param capacity The capacity of the edge.
	 */
	public ResidualEdge(ResidualVertex origin, ResidualVertex dest, double capacity) {
		this.source = origin;
		this.destination = dest;
		this.capacity = capacity;
	}

	/**
	 * Update the flow on the edge by the given increment. Handles both forward and
	 * backward edge updates.
	 *
	 * @param increment The amount by which to update the flow on this edge.
	 * @throws Exception If the given increment violates capacity constraints.
	 */
	public void updateFlow(double increment) throws Exception {
		if (isBackwardEdge()) {
			handleBackwardEdgeUpdate(increment);
		} else {
			handleForwardEdgeUpdate(increment);
		}
	}

	/**
	 * Handle the update of a backward edge by reducing its capacity and adjusting
	 * the flow on the corresponding forward edge. If the capacity of the backward
	 * edge becomes zero, the backward edge is removed from the graph.
	 *
	 * @param increment The amount by which to update the flow on the backward edge.
	 * @throws Exception If the given increment violates capacity constraints.
	 */
	private void handleBackwardEdgeUpdate(double increment) throws Exception {
		if (increment > capacity) {
			throw new Exception("Increment of " + increment + " on backward edge of capacity " + this.capacity);
		}

		this.capacity -= increment;
		this.forwardEdge.flow -= increment;

		if (this.capacity == 0) {
			this.forwardEdge.backwardEdge = null;
			this.source.removeEdge(this);
		}
	}

	/**
	 * Handle the update of a forward edge by increasing its flow. If the backward
	 * edge does not exist, it is created. Otherwise, the capacity of the backward
	 * edge is updated to match the flow on the forward edge.
	 *
	 * @param increment The amount by which to update the flow on the forward edge.
	 * @throws Exception If the given increment violates capacity constraints.
	 */
	private void handleForwardEdgeUpdate(double increment) throws Exception {
		if (this.flow + increment > this.capacity) {
			throw new Exception("Increment of " + increment + " on forward edge of capacity " + this.capacity
					+ " and flow " + this.flow);
		}

		this.flow += increment;

		if (this.backwardEdge == null) {
			createBackwardEdge();
		} else {
			this.backwardEdge.setCapacity(this.flow);
		}
	}

	/**
	 * Create a backward edge corresponding to the current forward edge. The
	 * backward edge is created between the destination and source vertices with a
	 * capacity equal to the current flow on the forward edge.
	 *
	 * @throws Exception If an error occurs during the creation of the backward
	 *                   edge.
	 */
	private void createBackwardEdge() throws Exception {
		this.backwardEdge = new ResidualEdge(this.destination, this.source, this.flow);
		this.backwardEdge.forwardEdge = this;
		this.destination.addEdge(this.backwardEdge);
	}

	/**
	 * Update the flow on the edge by the given increment. Handles both forward and
	 * backward edge updates.
	 *
	 * @param increment The amount by which to update the flow on this edge.
	 * @throws Exception If the given increment violates capacity constraints.
	 */
	public String getIdentifier() {
		String identifier = this.source.getIdentifier() + "-" + this.destination.getIdentifier();
		if (isBackwardEdge()) {
			identifier += "-back";
		}
		return identifier;
	}

	/**
	 * Get the source vertex of the edge.
	 *
	 * @return The ResidualVertex representing the source vertex of the edge.
	 */
	public ResidualVertex getSource() {
		return source;
	}

	/**
	 * Get the destination vertex of the edge.
	 *
	 * @return The ResidualVertex representing the destination vertex of the edge.
	 */
	public ResidualVertex getDestination() {
		return destination;
	}

	/**
	 * Get the capacity of the edge.
	 *
	 * @return The capacity of the edge.
	 */
	public double getCapacity() {
		return capacity;
	}

	/**
	 * Get the current flow on the edge.
	 *
	 * @return The current flow on the edge.
	 */
	public double getFlow() {
		return flow;
	}

	/**
	 * Get the residual capacity of the edge. For backward edges, returns the
	 * remaining capacity; for forward edges, returns the remaining capacity by
	 * subtracting the current flow from the total capacity.
	 *
	 * @return The residual capacity of the edge.
	 */
	public double getResidualCapacity() {
		if (this.isBackwardEdge()) {
			return this.capacity;
		} else {
			return this.capacity - this.flow;
		}
	}

	/**
	 * Check if the edge is a backward edge.
	 *
	 * @return True if the edge is a backward edge; otherwise, false.
	 */
	public boolean isBackwardEdge() {
		return this.forwardEdge != null;
	}

	/**
	 * Sets the capacity of the edge.
	 *
	 */
	private void setCapacity(double capacity) {
		this.capacity = capacity;
	}

	/**
	 * Increase flow on the edge by given amount. Also takes care of
	 * adding/removing/updating corresponding backward edge if this is forward edge.
	 * If this is backward edge, it also updates the flow on corresponding forward
	 * edge, and removes this edge if necessary.
	 * 
	 * @param increment Amount by which to increment the flow on this edge
	 * @throws Exception If given increment violates capacity constraints
	 */
	public void increaseFlow(double increment) throws Exception {
		if (this.isBackwardEdge()) {
			if (increment > capacity) {
				throw new Exception("Increment of " + increment + " on backward edge of capacity " + this.capacity);
			}

			// Reduce the capacity of this edge, and reduce the flow in real forward edge
			this.capacity = this.capacity - increment;
			this.forwardEdge.flow -= increment;

			// Adjust excess in origin of real edge, decreasing outgoing flow from origin,
			// so excess increases in origin
			this.forwardEdge.source.increaseExcess(increment);

			// Adjust excess in dest of real edge, decreasing incoming flow into dest, so
			// excess decreases in dest
			this.forwardEdge.destination.increaseExcess(-increment);

			if (this.capacity == 0) {
				// no back edge for zero flow, remove edge from forward edge and vertex
				this.forwardEdge.backwardEdge = null;
				this.source.removeEdge(this);
			}
		} else {
			if (this.flow + increment > this.capacity) {
				throw new Exception("Increment of " + increment + " on forward edge of capacity " + this.capacity
						+ " and flow " + this.flow);
			}

			this.flow += increment;

			if (this.backwardEdge == null) {
				// add backward edge if not already there
				this.backwardEdge = new ResidualEdge(this.destination, this.source, this.flow);
				this.backwardEdge.forwardEdge = this;
				this.destination.addEdge(this.backwardEdge);
			} else {
				// backward edge already there, adjust it's capacity
				this.backwardEdge.setCapacity(this.flow);
			}

			// Adjust excess in origin, increasing outgoing flow from origin, so excess
			// decreases in origin
			this.source.increaseExcess(-increment);

			// Adjust excess in dest, increasing incoming flow into dest, so excess
			// increases in dest
			this.destination.increaseExcess(increment);
		}
	}
}
