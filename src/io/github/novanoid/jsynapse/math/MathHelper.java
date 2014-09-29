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

package io.github.novanoid.jsynapse.math;

/**
 * A helper class including some constants and static helper functions
 * 
 * @author Novanoid
 */
public class MathHelper {
	/**
	 * The learning rate for error backpropagation
	 */
	public static final double ETAbak = 0.001;
	/**
	 * The momentum value for error backpropagation
	 */
	public static final double ALPHAbak = 0.0001;

	/**
	 * Calculate the sigmoid function
	 * 
	 * @param input
	 *            Value at which the function should be calculated
	 * @return Value of the function at the given input
	 */
	public static double sigmoid(double input) {
		return 1.0 / (1 + Math.pow(Math.E, -input));
	}

	/**
	 * Calculate the differential of the sigmoid function
	 * 
	 * @param input
	 *            Value at which the differential should be calculated
	 * @return Value of the differential at the given input
	 */
	public static double sigmoidDifferential(double input) {
		return MathHelper.sigmoid(input) * (1 - MathHelper.sigmoid(input));
	}
}
