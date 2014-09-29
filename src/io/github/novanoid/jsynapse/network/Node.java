// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.
//
// Copyright 2014 Novanoid

package io.github.novanoid.jsynapse.network;

import io.github.novanoid.jsynapse.math.MathHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single neuronal node in the network
 * 
 * @author Novanoid
 */
public class Node implements Serializable {
	private static final long serialVersionUID = -2661485184836047931L;

	/**
	 * The weights of each connection from previous nodes
	 */
	private List<Weight> weights;
	/**
	 * The bias of this node
	 */
	private double bias;
	/**
	 * The last local output of this node
	 */
	private double lastOutput;
	/**
	 * The last local input this node received
	 */
	private double lastInput;

	/**
	 * The gradient of this node for error backpropagation
	 */
	private double gradient;
	/**
	 * The previous weight deltas of this node for error backpropagation
	 */
	private List<Double> previousWeightDeltas;
	/**
	 * The previous bias delta of this node for error backpropagation
	 */
	private double previousBiasDelta;

	/**
	 * Initiate a new node with random values
	 * 
	 * @param connections
	 *            Number of connections from previous nodes
	 */
	public Node(int connections) {
		this.weights = new ArrayList<Weight>();
		this.previousWeightDeltas = new ArrayList<Double>();

		for (int i = 0; i < connections; i++) {
			this.weights.add(new Weight(Math.random() * 6 - 3));
			this.previousWeightDeltas.add(0.0);
		}

		this.bias = Math.random() * 6 - 3;
		this.bias = 0.0;
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
		if (inputValues.size() != this.weights.size()) {
			throw new IllegalArgumentException("The number of input values ("
					+ inputValues.size()
					+ ") must match the number of connections to this node ("
					+ this.weights.size() + ")");
		}

		double sum = 0;
		for (int i = 0; i < inputValues.size(); i++) {
			sum += inputValues.get(i) * this.weights.get(i).getValue();
		}

		sum += this.bias;

		this.lastInput = sum;
		this.lastOutput = MathHelper.sigmoid(sum);

		return this.lastOutput;
	}

	/**
	 * Get a list of previously computed weight deltas of the connections to
	 * this node
	 * 
	 * @return List of previously computed weight deltas
	 */
	public List<Double> getPreviousWeightDeltas() {
		return previousWeightDeltas;
	}

	/**
	 * Get the last computed gradient of this node
	 * 
	 * @return Gradient of this node
	 */
	public double getGradient() {
		return gradient;
	}

	/**
	 * Set the gradient for this node
	 * 
	 * @param gradient
	 *            New gradient for this node
	 */
	public void setGradient(double gradient) {
		this.gradient = gradient;
	}

	/**
	 * Get the previous delta of the bias of this node
	 * 
	 * @return Previous bias delta of this node
	 */
	public double getPreviousBiasDelta() {
		return previousBiasDelta;
	}

	/**
	 * Get the value this neural node has last output
	 * 
	 * @return Last output of this neural node
	 */
	public double getLastLocalOutput() {
		return this.lastOutput;
	}

	/**
	 * Get the value of the input this neural node hast last received
	 * 
	 * @return Last input of this neural node
	 */
	public double getLastLocalInput() {
		return this.lastInput;
	}

	/**
	 * Get the weights of all connections from this node to nodes to the left
	 * 
	 * @return Weights of all connections to nodes to the left
	 */
	public List<Weight> getWeights() {
		return this.weights;
	}

	/**
	 * Update all weights of this node
	 * 
	 * @param weightDeltas
	 *            Weight deltas to update the weights with
	 */
	public void updateWeights(List<Double> weightDeltas) {
		if (weightDeltas.size() != this.weights.size()) {
			throw new IllegalArgumentException(
					"To update the weight values, the number of items in the list of weight deltas ("
							+ weightDeltas.size()
							+ ") must match the number of items in the list of weights this node has ("
							+ this.weights.size() + ")");
		}

		this.previousWeightDeltas = weightDeltas;

		for (int i = 0; i < this.weights.size(); i++) {
			this.weights.get(i).addValue(weightDeltas.get(i));
		}
	}

	/**
	 * Update the bias of this node
	 * 
	 * @param biasDelta
	 *            Bias delta to update the bias with
	 */
	public void updateBias(double biasDelta) {
		this.previousBiasDelta = biasDelta;

		if (!Double.isNaN(this.bias + biasDelta)) {
			this.bias += biasDelta;
		}
	}
}