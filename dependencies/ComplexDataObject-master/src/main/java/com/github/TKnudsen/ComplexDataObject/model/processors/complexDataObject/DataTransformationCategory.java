package com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject;

import java.awt.Color;

/**
 * <p>
 * Title: DataTransformationCategory
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.04
 */
public enum DataTransformationCategory {

	// TODO re-assign colors
	DIMENSION_REDUCTION("Dimension Reduction", new Color(0x49, 0x85, 0x93), new Color(0x30, 0xA5, 0xBF)), DESCRIPTOR(
			"Feature Extraction", new Color(0x6b, 0x99, 0x78),
			new Color(0x60, 0xbf, 0x7b)), FEATURE_EXTRACTION("Feature Extraction", new Color(0x6b, 0x99, 0x78),
					new Color(0x60, 0xbf, 0x7b)), DATA_CLEANING("Data Cleaning", new Color(0x6b, 0x99, 0x78),
							new Color(0x60, 0xbf, 0x7b)), DATA_SEGMENTATION("Segmentation", new Color(0x6b, 0x99, 0x78),
									new Color(0x60, 0xbf, 0x7b)), MODEL_UNCERTAINTY("Uncertainty",
											new Color(0x6b, 0x99, 0x78), new Color(0x60, 0xbf, 0x7b));

	private String name;
	private Color color;
	private Color lightColor;

	private DataTransformationCategory(String name, Color color, Color lightColor) {
		this.name = name;
		this.color = color;
		this.lightColor = lightColor;
	}

	public String toString() {
		return name;
	}

	public Color getColor() {
		return color;
	}

	public Color getLightColor() {
		return lightColor;
	}
}
