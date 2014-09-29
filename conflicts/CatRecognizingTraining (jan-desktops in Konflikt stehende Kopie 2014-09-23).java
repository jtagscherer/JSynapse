package io.github.novanoid.artificialneuralnetwork.training;

import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import io.github.novanoid.artificialneuralnetwork.network.NeuralNetwork;

/**
 * A specific training that teaches the neural network to recognize cat pictures
 * 
 * @author Novanoid
 */
public class CatRecognizingTraining extends Training {
	private int imageSize, randomCounter = 1;

	/**
	 * Create a new cat recognizing training
	 * 
	 * @param net
	 *            Neural network to be trained
	 * @param imageSize
	 *            Width and height of the quadratic images to be used
	 */
	public CatRecognizingTraining(NeuralNetwork net, int imageSize) {
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
	public void train(boolean verbose) {
		boolean cat = false;
		List<Double> desiredOutput = new ArrayList<Double>();

		if (Math.random() < 0.5) {
			cat = true;
			desiredOutput.add(1.0);
		} else {
			desiredOutput.add(0.0);
		}

		BufferedImage image = this.downloadThumbnail(cat, verbose);
		List<Double> pixelArray = new ArrayList<Double>();

		for (int y = 0; y < this.imageSize; y++) {
			for (int x = 0; x < this.imageSize; x++) {
				pixelArray
						.add((double) ((image.getRGB(x, y) & 0x000000ff) / 255.0));
			}
		}

		this.trainIteration(pixelArray, desiredOutput, verbose);
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

		BufferedImage scaledImage = null;

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
				inputStream = websiteUrl.openStream();
				bufferedReader = new BufferedReader(new InputStreamReader(
						inputStream));
				line = bufferedReader.readLine();
			}

			/* Download the thumbnail as a buffered image */
			urlThumbnail = line.substring(line.indexOf("\"thumbnail\":") + 14,
					line.indexOf("subreddit_id") - 4);
			BufferedImage image = null;
			System.out.println("Downloading " + urlThumbnail + "...");
			image = ImageIO.read(new URL(urlThumbnail));

			/* Scale the image */
			System.out.println("Editing the image...");
			scaledImage = new BufferedImage(this.imageSize, this.imageSize,
					BufferedImage.TYPE_INT_RGB);
			Graphics g = scaledImage.createGraphics();
			g.drawImage(image, 0, 0, this.imageSize, this.imageSize, null);
			g.dispose();

			/* Convert the image to gray scale colors */
			/*ColorConvertOp op = new ColorConvertOp(
					ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
			op.filter(scaledImage, scaledImage);*/

			File outputfile = new File("saved" + this.randomCounter + ".png");
			ImageIO.write(scaledImage, "png", outputfile);
		} catch (Exception e) {
			System.err.println("Could not download an image:");
			e.printStackTrace();
		}

		return scaledImage;
	}
}
