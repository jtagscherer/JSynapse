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

import java.util.List;

/**
 * A training data set containing both the input and the desired output
 * 
 * @author Novanoid
 */
public class TrainingDataSet {
	private List<Double> input, desiredOutput;

	/**
	 * Initialize a new training data set
	 * 
	 * @param input2
	 *            The input for this data set
	 * @param desiredOutput2
	 *            The output the neural network should produce
	 */
	public TrainingDataSet(List<Double> input2, List<Double> desiredOutput2) {
		this.input = input2;
		this.desiredOutput = desiredOutput2;
	}

	/**
	 * Get the input of this training set
	 * 
	 * @return The input of this training set
	 */
	public List<Double> getInput() {
		return input;
	}

	/**
	 * Get the desired of this training set
	 * 
	 * @return The output the neural network should produce
	 */
	public List<Double> getDesiredOutput() {
		return desiredOutput;
	}
}
