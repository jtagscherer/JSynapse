package io.github.novanoid.artificialneuralnetwork.network;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a neural network consisting of an input layer, an output layer and
 * a number of hidden layers
 * 
 * @author Novanoid
 */
public class NeuralNetwork {
	/**
	 * The layers in this network
	 */
	private List<Layer> layers;

	/**
	 * Initialize a new neural network
	 * 
	 * @param inputNodes
	 *            Amount of input nodes in the input layer
	 * @param hiddenLayers
	 *            Amount of hidden layers
	 * @param outputNodes
	 *            Amount of output nodes in the output layer
	 */
	public NeuralNetwork(int inputNodes, int hiddenLayers, int outputNodes) {
		this.layers = new ArrayList<Layer>();

		double divisor = Math.pow((1.0 * outputNodes / inputNodes),
				(1.0 / (hiddenLayers + 1)));

		double nodeNumber = inputNodes;
		int connections = 1;
		for (int i = 0; i < hiddenLayers + 2; i++) {
			Layer layer = new Layer((int) Math.round(nodeNumber), connections);
			this.layers.add(layer);
			connections = (int) Math.round(nodeNumber);

			nodeNumber *= divisor;
		}
	}

	/**
	 * Get the total amount of neural nodes in this network
	 * 
	 * @return Amount of neural nodes in this network
	 */
	public int getAmountOfNeuralNodes() {
		int amount = 0;
		for (Layer layer : this.layers) {
			amount += layer.getNodes().size();
		}

		return amount;
	}

	/**
	 * Get the amount of neural nodes in the input layer of this network
	 * 
	 * @return Amount of neural nodes in the input layer
	 */
	public int getAmountOfNeuralNodesInInputLayer() {
		return this.layers.get(0).getNodes().size();
	}

	/**
	 * Feed the neural network a number of input values and calculate the values
	 * of the output layer
	 * 
	 * @param inputValues
	 *            Values for the neurons in the input layer, must match their
	 *            number
	 * @return Values of the neurons in the output layer
	 */
	public List<Double> input(List<Double> inputValues) {
		if (this.layers.get(0).getNodes().size() != inputValues.size()) {
			throw new IllegalArgumentException(
					"The number of input values ("
							+ inputValues.size()
							+ ") must match the number of neural nodes in the input layer ("
							+ this.layers.get(0).getNodes().size() + ")");
		}

		List<Double> processingInput = new ArrayList<Double>();
		List<Double> processingOutput = new ArrayList<Double>();

		/* Give values to the input layer */
		for (int i = 0; i < inputValues.size(); i++) {
			List<Double> inputList = new ArrayList<Double>();
			inputList.add(inputValues.get(i));
			processingInput.add(this.layers.get(0).getNodes().get(i)
					.input(inputList));
		}

		/* Continue giving all values to all nodes in the next layer */
		for (int i = 1; i < this.layers.size(); i++) {
			
			processingOutput.clear();
			Layer layer = this.layers.get(i);
			/* Save the output of each node in this layer */
			for (Node node : layer.getNodes()) {
				processingOutput.add(node.input(processingInput));
			}
			processingInput = new ArrayList<Double>(processingOutput);
		}

		return processingOutput;
	}
}
