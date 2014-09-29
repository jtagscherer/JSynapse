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

package io.github.novanoid.jsynapse.training;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.github.novanoid.jsynapse.math.MathHelper;
import io.github.novanoid.jsynapse.network.Layer;
import io.github.novanoid.jsynapse.network.NeuralNetwork;
import io.github.novanoid.jsynapse.network.Node;

/**
 * Base class for all types of training using error backpropagation
 * 
 * @author Novanoid
 */
public abstract class Training {
	protected NeuralNetwork net;

	/**
	 * Initialize a new training object
	 * 
	 * @param net
	 *            Neural network to be trained
	 */
	protected Training(NeuralNetwork net) {
		this.net = net;
	}

	/**
	 * Get a random training set for this specific task
	 * 
	 * @param verbose
	 *            True, if relevant values should be output, false otherwise
	 * @return A random training set
	 */
	protected abstract TrainingDataSet getDataSet(boolean verbose);

	/**
	 * Try to categorize a data set using the neural network
	 * 
	 * @param dataSet
	 *            Training set to be used
	 * @return True, if the data was correctly classified, false otherwise
	 */
	protected abstract boolean categorizeData(TrainingDataSet dataSet);

	/**
	 * Test the neural network for the specified amount of times with random
	 * data
	 * 
	 * @param iterations
	 *            Test iterations to perform
	 * @param verbose
	 *            True, if relevant values should be output, false otherwise
	 * @return The accuracy of the guesses of the neural network, ranging from 0
	 *         to 1
	 */
	public double startTesting(final int iterations, final boolean verbose) {
		int correctClassifications = 0;
		for (int i = 0; i < iterations; i++) {
			TrainingDataSet dataSet = getDataSet(verbose);
			if (categorizeData(dataSet)) {
				correctClassifications++;
			}
		}

		if (verbose) {
			DecimalFormat decimalFormat = new DecimalFormat("###.##");
			System.out
					.println("Accuracy: "
							+ decimalFormat
									.format(((correctClassifications * 1.0) / iterations) * 100)
							+ "%");
		}

		return (correctClassifications * 1.0) / iterations;
	}

	/**
	 * Train the neural network for the specified amount of times
	 * 
	 * @param iterations
	 *            Training iterations to perform
	 * @param verbose
	 *            True, if relevant values should be output, false otherwise
	 */
	public void startTraining(final int iterations, final boolean verbose) {
		for (int i = 0; i < iterations; i++) {
			if (verbose) {
				System.out.println("Training iteration #" + i + ":");
			}

			TrainingDataSet dataSet = getDataSet(verbose);
			trainIteration(dataSet.getInput(), dataSet.getDesiredOutput(),
					verbose);
		}
	}

	/**
	 * Train the neural network using the error backpropagation algorithm
	 * 
	 * @param input
	 *            Input values for the neural net
	 * @param desiredOutput
	 *            Desired output values for this set of input values
	 * @param verbose
	 *            True, if relevant values should be output, false otherwise
	 */
	protected void trainIteration(List<Double> input,
			List<Double> desiredOutput, boolean verbose) {
		/* Run the input through the neural net */
		List<Double> actualOutput = this.net.input(input);

		if (verbose) {
			this.printOutputInformation(actualOutput, desiredOutput);
		}

		/* Calculate the gradients */
		List<Layer> layers = this.net.getNodeLayers();
		this.calculateGradients(desiredOutput, actualOutput, layers);

		/*
		 * Calculate the deltas using the previously calculated gradients while
		 * immediately adding momentum and update the neural network accordingly
		 */
		calculateDeltas(layers);
	}

	/**
	 * Calculate the weight and bias deltas using previously calculated
	 * gradients
	 * 
	 * @param layers
	 *            A list containing all layers of neural nodes in this network
	 */
	private void calculateDeltas(List<Layer> layers) {
		List<Double> weightDeltas = new ArrayList<Double>();
		for (int layerNumber = layers.size() - 1; layerNumber > 0; layerNumber--) {
			for (int nodeNumber = 0; nodeNumber < layers.get(layerNumber)
					.getNodes().size(); nodeNumber++) {
				Node node = layers.get(layerNumber).getNodes().get(nodeNumber);
				List<Node> nodesToLeft = layers.get(layerNumber - 1).getNodes();
				/* First for all weights of this node */
				weightDeltas.clear();
				for (int weightNumber = 0; weightNumber < node.getWeights()
						.size(); weightNumber++) {
					weightDeltas.add(net.ETA * node.getGradient()
							* nodesToLeft.get(weightNumber).getLastLocalInput()
							+ net.ALPHA
							* node.getPreviousWeightDeltas().get(weightNumber));
				}
				node.updateWeights(weightDeltas);
				/* And afterwards for the bias of this node */
				node.updateBias(net.ETA * node.getGradient() + net.ALPHA
						* node.getPreviousBiasDelta());
			}
		}
	}

	/**
	 * Calculate the node gradients for this training iteration
	 * 
	 * @param desiredOutput
	 *            The optimal output the neural network should produce
	 * @param actualOutput
	 *            The computed output of the neural network
	 * @param layers
	 *            A list of all layers of neural nodes in this network
	 */
	private void calculateGradients(List<Double> desiredOutput,
			List<Double> actualOutput, List<Layer> layers) {
		/* First for neurons in the output layer */
		for (int i = 0; i < layers.get(layers.size() - 1).getNodes().size(); i++) {
			Node node = layers.get(layers.size() - 1).getNodes().get(i);
			node.setGradient((desiredOutput.get(i) - actualOutput.get(i))
					* MathHelper.sigmoidDifferential(actualOutput.get(i)));
		}
		/*
		 * And afterwards for all neurons in the hidden and output layer from
		 * right to left
		 */
		for (int layerNumber = layers.size() - 2; layerNumber >= 0; layerNumber--) {
			for (int nodeNumber = 0; nodeNumber < layers.get(layerNumber)
					.getNodes().size(); nodeNumber++) {
				Node node = layers.get(layerNumber).getNodes().get(nodeNumber);
				List<Node> nodesToRight = layers.get(layerNumber + 1)
						.getNodes();
				double sumOfWeightedGradients = 0.0;
				for (Node neighborNode : nodesToRight) {
					sumOfWeightedGradients += neighborNode.getWeights()
							.get(nodeNumber).getValue()
							* neighborNode.getGradient();
					node.setGradient(MathHelper.sigmoidDifferential(node
							.getLastLocalOutput()) * sumOfWeightedGradients);
				}
			}
		}
	}

	/**
	 * Print information regarding the output of a neural network
	 * 
	 * @param actualOutput
	 *            Actual computed output
	 * @param desiredOutput
	 *            Desired output of the network
	 */
	private void printOutputInformation(List<Double> actualOutput,
			List<Double> desiredOutput) {
		System.out.println("Desired output: " + desiredOutput);
		System.out.println("Actual output: " + actualOutput);

		double certainty = 0.0;
		for (int i = 0; i < desiredOutput.size(); i++) {
			if (desiredOutput.get(i) == 0) {
				certainty += 1.0 - actualOutput.get(i);
			} else if (desiredOutput.get(i) == 1) {
				certainty += actualOutput.get(i);
			} else {
				throw new IllegalArgumentException(
						"Only desired outputs of 0 or 1 are supported.");
			}
		}
		certainty /= desiredOutput.size();
		certainty *= 100;

		DecimalFormat decimalFormat = new DecimalFormat("###");
		System.out.println("-> Certainty: " + decimalFormat.format(certainty)
				+ "\u0025");
		System.out.println();
	}
}
