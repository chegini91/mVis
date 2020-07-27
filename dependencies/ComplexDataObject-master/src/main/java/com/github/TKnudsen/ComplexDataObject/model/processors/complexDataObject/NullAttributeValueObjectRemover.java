package com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataContainer;
import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataObject;

/**
 * <p>
 * Title: NullAttributeValueObjectRemover
 * </p>
 * 
 * <p>
 * Description: Removes ComplexDataObjects with null values for a given
 * Attribute.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2016
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.02
 */
public class NullAttributeValueObjectRemover implements IComplexDataObjectProcessor {

	private String attribute;

	public NullAttributeValueObjectRemover(String attribute) {
		this.attribute = attribute;
	}

	@Override
	public void process(ComplexDataContainer container) {

		if (attribute == null)
			return;

		List<ComplexDataObject> removes = new ArrayList<>();

		for (Iterator<ComplexDataObject> iterator = container.iterator(); iterator.hasNext();) {
			ComplexDataObject complexDataObject = iterator.next();
			Object value = complexDataObject.getAttribute(attribute);
			if (value == null)
				removes.add(complexDataObject);
		}

		for (ComplexDataObject complexDataObject : removes)
			container.remove(complexDataObject);
	}

	@Override
	public void process(List<ComplexDataObject> data) {
		ComplexDataContainer container = new ComplexDataContainer(data);
		process(container);
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	@Override
	public DataProcessingCategory getPreprocessingCategory() {
		return DataProcessingCategory.DATA_CLEANING;
	}
}
