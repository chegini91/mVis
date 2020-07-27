package com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject;

import java.util.Arrays;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataContainer;
import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataObject;

/**
 * <p>
 * Title: MaximumLimiter
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

public class MaximumLimiter implements IComplexDataObjectProcessor {

	private String attribute;
	private Number maximum;

	public MaximumLimiter() {
	}

	public MaximumLimiter(Number maximum) {
		this.maximum = maximum;
	}

	public MaximumLimiter(String attribute, Number maximum) {
		this.attribute = attribute;
		this.maximum = maximum;
	}

	@Override
	public void process(List<ComplexDataObject> data) {
		if (attribute == null)
			throw new IllegalArgumentException("MaximumLimiter: no attribute defined");

		if (maximum == null || Double.isNaN(maximum.doubleValue()))
			throw new IllegalArgumentException("MaximumLimiter: no limit defined");

		for (ComplexDataObject cdo : data) {
			Object object = cdo.getAttribute(attribute);
			if (object == null)
				continue;

			if (object instanceof Number) {
				if (((Number) object).doubleValue() > maximum.doubleValue())
					cdo.add(attribute, maximum);
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

	public Number getMaximum() {
		return maximum;
	}

	public void setMaximum(Number maximum) {
		this.maximum = maximum;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
}