package com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject;

import java.util.Arrays;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataContainer;
import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataObject;

/**
 * <p>
 * Title: MinimumLimiter
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
 * @version 1.01
 */

public class MinimumLimiter implements IComplexDataObjectProcessor {

	private String attribute;
	private Number minimum;

	public MinimumLimiter() {
	}

	public MinimumLimiter(Number minimum) {
		this.minimum = minimum;
	}

	public MinimumLimiter(String attribute, Number minimum) {
		this.attribute = attribute;
		this.minimum = minimum;
	}

	@Override
	public void process(List<ComplexDataObject> data) {
		if (attribute == null)
			throw new IllegalArgumentException("MinimumLimiter: no attribute defined");

		if (minimum == null || Double.isNaN(minimum.doubleValue()))
			throw new IllegalArgumentException("MinimumLimiter: no limit defined");

		for (ComplexDataObject cdo : data) {
			Object object = cdo.getAttribute(attribute);
			if (object == null)
				continue;

			if (object instanceof Number) {
				if (((Number) object).doubleValue() < minimum.doubleValue())
					cdo.add(attribute, minimum);
			}
		}
	}

	@Override
	public DataProcessingCategory getPreprocessingCategory() {
		return DataProcessingCategory.DATA_CLEANING;
	}

	@Override
	public void process(ComplexDataContainer container) {
		for (ComplexDataObject complexDataObject : container)
			process(Arrays.asList(complexDataObject));
	}

	public Number getMinimum() {
		return minimum;
	}

	public void setMinimum(Number minimum) {
		this.minimum = minimum;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

}