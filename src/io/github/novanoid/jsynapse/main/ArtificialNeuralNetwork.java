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

package io.github.novanoid.jsynapse.main;

import java.text.DecimalFormat;

import io.github.novanoid.jsynapse.file.FileManager;
import io.github.novanoid.jsynapse.network.NeuralNetwork;
import io.github.novanoid.jsynapse.training.digitrecognition.DigitRecognitionTraining;

/**
 * Example class that instances a new neural network
 * 
 * @author Novanoid
 */
public class ArtificialNeuralNetwork {
	/**
	 * Entry point for this example class
	 * 
	 * @param Console
	 *            parameters, not used
	 */
	public static void main(String[] args) {
		NeuralNetwork net = new NeuralNetwork(256, 2, 10);
		net.configure(0.001, 0.0001);
		System.out.println("Simulating " + net.getAmountOfNeuralNodes()
				+ " neural nodes...");
		System.out.println();

		DigitRecognitionTraining training = new DigitRecognitionTraining(net);

		for (int i = 0; i < 1000; i++) {
			training.startTraining(100, false);
			System.out.println((i * 100) + " training iterations performed");
			double accuracy = training.startTesting(100, false);

			DecimalFormat decimalFormat = new DecimalFormat("###");
			System.out.println("Accuracy: "
					+ decimalFormat.format(accuracy * 100) + "\u0025");
		}

		FileManager.saveNetwork(net, "trained-digit-recognition-net.ser");
	}
}