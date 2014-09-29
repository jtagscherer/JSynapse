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

package io.github.novanoid.jsynapse.training.catrecognition;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import io.github.novanoid.jsynapse.network.NeuralNetwork;
import io.github.novanoid.jsynapse.training.Training;
import io.github.novanoid.jsynapse.training.TrainingDataSet;

/**
 * A specific training that teaches the neural network to recognize cat pictures
 * 
 * @author Novanoid
 */
public class CatRecognitionTraining extends Training {
	private int imageSize, randomCounter = 1;

	/**
	 * Create a new cat recognizing training
	 * 
	 * @param net
	 *            Neural network to be trained
	 * @param imageSize
	 *            Width and height of the quadratic images to be used
	 */
	public CatRecognitionTraining(NeuralNetwork net, int imageSize) {
		super(net);
		this.imageSize = imageSize;
		if (net.getAmountOfNeuralNodesInInputLayer() != Math.pow(imageSize, 2)) {
			throw new IllegalArgumentException(
					"For the specified image size of " + this.imageSize
							+ " the neural network must have "
							+ Math.round(Math.pow(this.imageSize, 2))
							+ " neural nodes in the input layer, yet it has "
							+ net.getAmountOfNeuralNodesInInputLayer() + ".");
		}
	}

	@Override
	public TrainingDataSet getDataSet(boolean verbose) {
		boolean cat = false;
		List<Double> desiredOutput = new ArrayList<Double>();

		if (Math.random() < 0.5) {
			cat = true;
			desiredOutput.add(1.0);
		} else {
			desiredOutput.add(0.0);
		}

		BufferedImage image = this.downloadThumbnail(cat, false);
		List<Double> pixelArray = new ArrayList<Double>();

		for (int y = 0; y < this.imageSize; y++) {
			for (int x = 0; x < this.imageSize; x++) {
				pixelArray
						.add((double) ((image.getRGB(x, y) & 0x000000ff) / 255.0));
			}
		}

		return new TrainingDataSet(pixelArray, desiredOutput);
	}

	/**
	 * Download a thumbnail picture from reddit
	 * 
	 * @param cat
	 *            Downloads a cat picture if true and a random picture otherwise
	 * @param verbose
	 *            True, if relevant values should be output, false otherwise
	 */
	private BufferedImage downloadThumbnail(boolean cat, boolean verbose) {
		URL websiteUrl = null;
		InputStream inputStream = null;
		BufferedReader bufferedReader;
		String line = "", urlString = "", urlThumbnail = "";

		BufferedImage grayScaleImage = null;

		try {
			while (!line.contains("thumbs.redditmedia.com")) {
				/*
				 * Workaround for network caching occuring while connecting to
				 * reddit returning the same random post for a minute
				 */
				this.randomCounter = (int) (Math.random() * 100000);

				if (cat) {
					urlString = "http://www.reddit.com/r/cats/random/.json?limit="
							+ randomCounter;
				} else {
					urlString = "http://www.reddit.com/r/pics/random/.json?limit="
							+ randomCounter;
				}

				websiteUrl = new URL(urlString);
				try {
					inputStream = websiteUrl.openStream();
					bufferedReader = new BufferedReader(new InputStreamReader(
							inputStream));
					line = bufferedReader.readLine();
				} catch (Exception e) {
					System.err
							.println("Could not download thumbnail, skipping...");
					line = "";
				}
			}

			/* Download the thumbnail as a buffered image */
			urlThumbnail = line.substring(line.indexOf("\"thumbnail\":") + 14,
					line.indexOf("subreddit_id") - 4);
			BufferedImage image = null;
			if (verbose) {
				System.out.println("Downloading " + urlThumbnail + "...");
			}
			image = ImageIO.read(new URL(urlThumbnail));

			/* Scale the image */
			BufferedImage scaledImage = new BufferedImage(this.imageSize,
					this.imageSize, BufferedImage.TYPE_INT_RGB);
			Graphics scaledGraphics = scaledImage.createGraphics();
			scaledGraphics.drawImage(image, 0, 0, this.imageSize,
					this.imageSize, null);
			scaledGraphics.dispose();

			/* Convert the image to gray scale colors */
			grayScaleImage = new BufferedImage(this.imageSize, this.imageSize,
					BufferedImage.TYPE_BYTE_GRAY);
			Graphics grayGraphics = grayScaleImage.getGraphics();
			grayGraphics.drawImage(scaledImage, 0, 0, null);
			grayGraphics.dispose();
		} catch (Exception e) {
			System.err.println("Could not download an image:");
			e.printStackTrace();
		}

		return grayScaleImage;
	}

	@Override
	public boolean categorizeData(TrainingDataSet dataSet) {
		List<Double> output = this.net.input(dataSet.getInput());

		if (dataSet.getDesiredOutput().get(0) == 1.0) {
			if (output.get(0) >= 0.5) {
				return true;
			}
		} else if (dataSet.getDesiredOutput().get(0) == 0.0) {
			if (output.get(0) <= 0.5) {
				return true;
			}
		}

		return false;
	}
}
