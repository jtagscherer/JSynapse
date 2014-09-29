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

import java.io.Serializable;

/**
 * The weight of a neural connection. This is used as a wrapper class to be
 * stored and manipulated in an ArrayList
 * 
 * @author Novanoid
 */
public class Weight implements Serializable {
	private static final long serialVersionUID = -4192293458832696691L;

	private double value;

	/**
	 * Initialize a new object of the type weight
	 * 
	 * @param value
	 *            New value for this weight
	 */
	public Weight(double value) {
		this.value = value;
	}

	/**
	 * Get the value of this weight
	 * 
	 * @return Value of this weight
	 */
	public double getValue() {
		return this.value;
	}

	/**
	 * Add a value to this weight
	 * 
	 * @param add
	 *            Value to add
	 */
	public void addValue(double add) {
		if (!Double.isNaN(this.value + add)) {
			this.value += add;
		}
	}
}
