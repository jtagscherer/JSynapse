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

/**
 * A handwritten digit used for training
 * 
 * @author Novanoid
 */
public class DigitRecognitionDataSet {
	/**
	 * The image data of this set
	 */
	private Double[] imageData;
	/**
	 * The number this data set shows, represented as nine zeros and one number
	 * one whose index indicates the number of this data set
	 */
	private Double[] number;

	/**
	 * Initialize a new digit using raw data
	 * 
	 * @param rawData
	 *            String of data to be used
	 */
	public DigitRecognitionDataSet(String rawData) {
		this.imageData = new Double[256];
		this.number = new Double[10];
		String[] rawDataSplit = rawData.split(" ");

		try {
			for (int i = 0; i < 256; i++) {
				this.imageData[i] = Double.parseDouble(rawDataSplit[i]);
			}

			for (int i = 0; i < 10; i++) {
				this.number[i] = Double.parseDouble(rawDataSplit[i + 256]);
			}
		} catch (Exception e) {
			System.err
					.println("The image data could not be parsed because it is invalid:");
			e.printStackTrace();
		}
	}

	/**
	 * Get the image data in this training set
	 * 
	 * @return The image data of this training set
	 */
	public Double[] getImageData() {
		return imageData;
	}

	/**
	 * Get the number of this data set
	 * 
	 * @return The number which is shown in the image of this data set
	 */
	public Double[] getNumber() {
		return number;
	}
}
