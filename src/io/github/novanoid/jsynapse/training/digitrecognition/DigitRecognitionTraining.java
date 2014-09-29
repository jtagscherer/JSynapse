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

package io.github.novanoid.jsynapse.training.digitrecognition;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.novanoid.jsynapse.network.NeuralNetwork;
import io.github.novanoid.jsynapse.training.Training;
import io.github.novanoid.jsynapse.training.TrainingDataSet;

/**
 * A specific training that teaches the neural network to recognize handwritten
 * from 0 to 9
 * 
 * @author Novanoid
 */
public class DigitRecognitionTraining extends Training {
	private List<DigitRecognitionDataSet> dataSets;
	private final int NUMBER_OF_DATA_SETS = 1593;

	/**
	 * Create a new digit recognizing training
	 * 
	 * @param net
	 *            Neural network to be trained
	 * @param imageSize
	 *            Width and height of the quadratic images to be used
	 */
	public DigitRecognitionTraining(NeuralNetwork net) {
		super(net);
		if (net.getAmountOfNeuralNodesInInputLayer() != 256) {
			throw new IllegalArgumentException(
					"For the specified image size of 16x16px the "
							+ "neural network must have 256 neural nodes "
							+ "in the input layer, yet it has "
							+ net.getAmountOfNeuralNodesInInputLayer() + ".");
		} else if (net.getAmountOfNeuralNodesInOutputLayer() != 10) {
			throw new IllegalArgumentException(
					"For this task the neural net should have 10 neural "
							+ "nodes in the output layer, yet it has "
							+ net.getAmountOfNeuralNodesInOutputLayer() + ".");
		}

		System.out.println("Downloading and parsing the training data...");
		this.dataSets = new ArrayList<DigitRecognitionDataSet>();
		this.downloadTrainingSet();
	}

	@Override
	public TrainingDataSet getDataSet(boolean verbose) {
		int randomSetNumber = (int) (Math.random() * this.NUMBER_OF_DATA_SETS);

		List<Double> input = Arrays.asList(this.dataSets.get(randomSetNumber)
				.getImageData());
		List<Double> desiredOutput = Arrays.asList(this.dataSets.get(
				randomSetNumber).getNumber());

		return new TrainingDataSet(input, desiredOutput);
	}

	/**
	 * Download a training set of handwritten digits from the Semeion Research
	 * Center of Sciences of Communication, Rome, Italy. It contains 1593
	 * black-white images of the digits 0 to 9 with 16 x 16 pixels.
	 * 
	 * Reference:
	 * http://archive.ics.uci.edu/ml/machine-learning-databases/semeion/
	 */
	private void downloadTrainingSet() {
		URL websiteUrl = null;
		InputStream inputStream = null;
		BufferedReader bufferedReader;
		String line = null;

		try {
			websiteUrl = new URL(
					"http://archive.ics.uci.edu/ml/machine-learning-databases/"
							+ "semeion/semeion.data");
			inputStream = websiteUrl.openStream();
			bufferedReader = new BufferedReader(new InputStreamReader(
					inputStream));

			int progress = 0;
			DecimalFormat decimalFormat = new DecimalFormat("###");
			while ((line = bufferedReader.readLine()) != null) {
				this.dataSets.add(new DigitRecognitionDataSet(line));
				progress++;
				if (progress % 100 == 0) {
					System.out
							.println(decimalFormat
									.format((progress / (this.NUMBER_OF_DATA_SETS * 1.0)) * 100)
									+ "%");
				}
			}

			System.out.println("Successfully downloaded all training sets.");
			System.out.println();
		} catch (Exception e) {
			System.err.println("Could not download the training set, maybe it "
					+ "has been moved or deleted or there is no "
					+ "internet connection available.");
		}
	}

	@Override
	public boolean categorizeData(TrainingDataSet dataSet) {
		List<Double> output = this.net.input(dataSet.getInput());

		int indexOfMaximumOutput = 0;
		for (int i = 0; i < output.size(); i++) {
			if (output.get(i).doubleValue() > output.get(indexOfMaximumOutput)
					.doubleValue()) {
				indexOfMaximumOutput = i;
			}
		}

		if (dataSet.getDesiredOutput().get(indexOfMaximumOutput) == 1.0) {
			return true;
		}

		return false;
	}
}
