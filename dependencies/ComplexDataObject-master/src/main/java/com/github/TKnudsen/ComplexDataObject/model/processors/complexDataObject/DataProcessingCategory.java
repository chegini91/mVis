package com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject;

import java.awt.Color;

/**
 * <p>
 * Title: DataProcessingCategory
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2011-2016
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.03
 */
public enum DataProcessingCategory {

	DATA_CLEANING("Data Cleaning", new Color(0x49, 0x85, 0x93), new Color(0x30, 0xA5, 0xBF)), DATA_REDUCTION("Data Reduction", new Color(0x6b, 0x99, 0x78), new Color(0x60, 0xbf, 0x7b)), DATA_OBSERVATION("Data Observation", new Color(0xbf, 0xb2, 0x30), new Color(0xe6, 0xdb, 0x73)), DATA_NORMALIZATION("Data Normalization", new Color(0xbf, 0x71, 0x30), new Color(0xe6, 0xa7, 0x73)), DATA_SEGMENTATION("Data Segmentation", new Color(0xbf, 0x3b, 0x30),
			new Color(0xe6, 0x7c, 0x73)), SECONDARY_DATA_PROVIDER("Secondary Data Provider", new Color(0xbf, 0x3b, 0x30), new Color(0xe6, 0x7c, 0x73));

	private String name;
	private Color color;
	private Color lightColor;

	private DataProcessingCategory(String name, Color color, Color lightColor) {
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
