package io.github.novanoid.artificialneuralnetwork.network;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single neuronal node in the network
 * 
 * @author Novanoid
 */
public class Node {
	/**
	 * The weights of each connection from previous nodes
	 */
	private List<Double> values;
	/**
	 * The bias of this node
	 */
	private double bias;

	private double lastOutput;

	/**
	 * Initiate a new node with random values
	 * 
	 * @param connections
	 *            Number of connections from previous nodes
	 */
	public Node(int connections) {
		this.values = new ArrayList<Double>();

		for (int i = 0; i < connections; i++) {
			this.values.add(Math.random() * 2 - 1);
		}

		this.bias = Math.random() * 4 - 2;
	}

	/**
	 * Input a set of values from previous nodes and calculate the output of
	 * this node using the sigmoid function
	 * 
	 * @param inputValues
	 *            Output values of all previous nodes
	 * @return Output value of this node
	 */
	public double input(List<Double> inputValues) {
		if (inputValues.size() != this.values.size()) {
			throw new IllegalArgumentException("The number of input values ("
					+ inputValues.size()
					+ ") must match the number of connections to this node ("
					+ this.values.size() + ")");
		}

		double sum = 0;
		for (int i = 0; i < inputValues.size(); i++) {
			sum += inputValues.get(i) * this.values.get(i);
		}
		sum += this.bias;

		this.lastOutput = 1.0 / (1 + Math.pow(Math.E, -sum));

		return this.lastOutput;
	}

	/**
	 * Get the value this neural node has last output
	 * 
	 * @return Last output of this neural node
	 */
	public double getLastOutput() {
		return this.lastOutput;
	}
}