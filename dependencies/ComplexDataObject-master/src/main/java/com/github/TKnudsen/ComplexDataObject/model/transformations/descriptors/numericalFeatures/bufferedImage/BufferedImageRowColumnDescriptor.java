package com.github.TKnudsen.ComplexDataObject.model.transformations.descriptors.numericalFeatures.bufferedImage;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataObject;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeature;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject.DataTransformationCategory;
import com.github.TKnudsen.ComplexDataObject.model.tools.BufferedImageTools;
import com.github.TKnudsen.ComplexDataObject.model.transformations.descriptors.IDescriptor;
import com.github.TKnudsen.ComplexDataObject.model.transformations.descriptors.numericalFeatures.INumericFeatureVectorDescriptor;

/**
 * <p>
 * Title: BufferedImageRowColumnDescriptor
 * </p>
 * 
 * <p>
 * Description: Transforms BufferedImages into the numerical feature space. The
 * luminance values of rows and columns build the features space. Thus, the
 * dimensionality of the feature vector is rows+columns of the original
 * BufferedImage.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2016-2018
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.05
 */
public class BufferedImageRowColumnDescriptor implements INumericFeatureVectorDescriptor<ComplexDataObject> {

	private String bufferedImageAttributeName;

	/**
	 * determines which x-st row/column is used
	 */
	private int sampling = 1;

	/**
	 * determines how many pixels are skipped at each image border
	 */
	private int cropBorders = 0;

	public BufferedImageRowColumnDescriptor(String bufferedImageAttributeName) {
		this.bufferedImageAttributeName = bufferedImageAttributeName;
	}

	@Override
	public List<NumericalFeatureVector> transform(ComplexDataObject complexDataObject) {
		if (complexDataObject == null)
			return null;

		if (complexDataObject.getAttribute(bufferedImageAttributeName) == null)
			return null;

		Object object = complexDataObject.getAttribute(bufferedImageAttributeName);
		if (object instanceof BufferedImage) {
			BufferedImage image = (BufferedImage) complexDataObject.getAttribute(bufferedImageAttributeName);

			List<NumericalFeature> features = new ArrayList<>();

			int width = image.getWidth();
			int height = image.getHeight();

			double lum = 0;
			for (int x = cropBorders; x < width - cropBorders; x++) {
				lum = 0;
				for (int y = 0; y < height; y++) {
					double luminance = BufferedImageTools.getLuminanceforPixel(image, x, y);
					lum += luminance;
				}
				if ((x - cropBorders) % sampling == 0)
					features.add(new NumericalFeature("Luminance column " + x, lum));
			}

			for (int y = cropBorders; y < height - cropBorders; y++) {
				lum = 0;
				for (int x = 0; x < width; x++) {
					double luminance = BufferedImageTools.getLuminanceforPixel(image, x, y);
					lum += luminance;
				}
				if ((y - cropBorders) % sampling == 0)
					features.add(new NumericalFeature("Luminance row " + y, lum));
			}

			List<NumericalFeatureVector> featureVectors = new ArrayList<>();

			NumericalFeatureVector featureVector = new NumericalFeatureVector(features);
			featureVector.setMaster(complexDataObject);
			if (complexDataObject.getAttribute("Label") != null)
				featureVector.add("Label", complexDataObject.getAttribute("Label"));

			featureVectors.add(featureVector);
			return featureVectors;

		} else
			return null;
	}

	@Override
	public List<NumericalFeatureVector> transform(List<ComplexDataObject> complexDataObjects) {
		if (complexDataObjects == null)
			return null;

		List<NumericalFeatureVector> featureVectors = new ArrayList<>();

		for (ComplexDataObject complexDataObject : complexDataObjects)
			featureVectors.addAll(transform(complexDataObject));

		return featureVectors;
	}

	@Override
	public String getName() {
		return "BufferedImageRawDataDescriptor";
	}

	@Override
	public String getDescription() {
		return "Descriptor for BufferedImages returning a NumericalFeatureVector with the raw RGB pixel information";
	}

	public String getBufferedImageAttributeName() {
		return bufferedImageAttributeName;
	}

	public void setBufferedImageAttributeName(String bufferedImageAttributeName) {
		this.bufferedImageAttributeName = bufferedImageAttributeName;
	}

	public int getSampling() {
		return sampling;
	}

	public void setSampling(int sampling) {
		if (sampling < 1)
			throw new IllegalArgumentException("parameter must be >= 1");

		this.sampling = sampling;
	}

	public int getCropBorders() {
		return cropBorders;
	}

	public void setCropBorders(int cropBorders) {
		if (cropBorders < 0)
			throw new IllegalArgumentException("parameter must be >= 0");

		this.cropBorders = cropBorders;
	}

	@Override
	public List<IDescriptor<ComplexDataObject, NumericalFeatureVector>> getAlternativeParameterizations(int count) {
		return null;
	}

	@Override
	public DataTransformationCategory getDataTransformationCategory() {
		return DataTransformationCategory.DESCRIPTOR;
	}
}
