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
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a layer of neural nodes
 * 
 * @author Novanoid
 */
public class Layer implements Serializable {
	private static final long serialVersionUID = 6452578214550979461L;

	/**
	 * List of nodes in this layer
	 */
	private List<Node> nodes;

	/**
	 * Initialize a new layer of nodes
	 * 
	 * @param amountOfNodes
	 *            Amount of nodes in this layer
	 * @param connections
	 *            Amount of nodes in the previous layer
	 */
	public Layer(int amountOfNodes, int connections) {
		this.nodes = new ArrayList<Node>();

		for (int i = 0; i < amountOfNodes; i++) {
			Node node = new Node(connections);
			this.nodes.add(node);
		}
	}

	/**
	 * Get the nodes in this layer
	 * 
	 * @return A list of all nodes in this layer
	 */
	public List<Node> getNodes() {
		return this.nodes;
	}
}
