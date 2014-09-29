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

package io.github.novanoid.jsynapse.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import io.github.novanoid.jsynapse.network.NeuralNetwork;

/**
 * Contains static methods to save and load networks
 * 
 * @author Novanoid
 */
public class FileManager {
	/**
	 * Save a neural network as a new file
	 * 
	 * @param net
	 *            Neural network to be saved
	 * @param path
	 *            Valid path and file name to be saved to
	 */
	public static void saveNetwork(NeuralNetwork net, String path) {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(path);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(
					fileOutputStream);

			objectOutputStream.writeObject(net);
			objectOutputStream.close();
		} catch (Exception e) {
			System.err
					.println("Could not save the neural network to the file '"
							+ path + "':");
			e.printStackTrace();
		}
	}

	/**
	 * Load a neural network from a file
	 * 
	 * @param path
	 *            Path and file name of the saved neural network
	 * @return Loaded neural network from the file
	 */
	public static NeuralNetwork loadNetwork(String path) {
		NeuralNetwork net = null;

		try {
			FileInputStream saveFile = new FileInputStream(path);
			ObjectInputStream save = new ObjectInputStream(saveFile);

			net = (NeuralNetwork) save.readObject();
			save.close();
		} catch (Exception e) {
			System.err
					.println("Could not load the neural network from the file '"
							+ path + "':");
			e.printStackTrace();
		}

		return net;
	}
}
