package model;


public class ResidualEdge {
	private ResidualVertex source;
	private ResidualVertex destination;
	private double capacity;
	private double flow;
	private ResidualEdge forwardEdge;
	private ResidualEdge backwardEdge;

	public ResidualEdge(ResidualVertex origin, ResidualVertex dest, double capacity) {
		this.source = origin;
		this.destination = dest;
		this.capacity = capacity;
	}

	public void updateFlow(double increment) throws Exception {
		if (isBackwardEdge()) {
			handleBackwardEdgeUpdate(increment);
		} else {
			handleForwardEdgeUpdate(increment);
		}
	}

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

	private void handleForwardEdgeUpdate(double increment) throws Exception {
		if (this.flow + increment > this.capacity) {
			throw new Exception("Increment of " + increment + " on forward edge of capacity " + this.capacity + " and flow " + this.flow);
		}

		this.flow += increment;

		if (this.backwardEdge == null) {
			createBackwardEdge();
		} else {
			this.backwardEdge.setCapacity(this.flow);
		}
	}

	private void createBackwardEdge() throws Exception {
		this.backwardEdge = new ResidualEdge(this.destination, this.source, this.flow);
		this.backwardEdge.forwardEdge = this;
		this.destination.addEdge(this.backwardEdge);
	}

	public String getIdentifier() {
		String identifier = this.source.getIdentifier() + "-" + this.destination.getIdentifier();
		if (isBackwardEdge()) {
			identifier += "-back";
		}
		return identifier;
	}

	public ResidualVertex getSource() {
		return source;
	}

	public ResidualVertex getDestination() {
		return destination;
	}

	public double getCapacity() {
		return capacity;
	}

	public double getFlow() {
		return flow;
	}

	public double getResidualCapacity() {
		if (this.isBackwardEdge()) {
			return this.capacity;
		} else {
			return this.capacity - this.flow;
		}
	}

	public boolean isBackwardEdge() {
		return this.forwardEdge != null;
	}

	private void setCapacity(double capacity) {
		this.capacity = capacity;
	}
}

