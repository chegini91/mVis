package com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject;

import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataContainer;
import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataObject;

/**
 * <p>
 * Title: AttributeRemover
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2016
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */

public class AttributeRemover implements IComplexDataObjectProcessor {

	private String attributeString;

	public AttributeRemover(String attributeString) {
		this.attributeString = attributeString;
	}

	@Override
	public void process(ComplexDataContainer container) {
		container.remove(attributeString);
	}

	@Override
	public void process(List<ComplexDataObject> data) {
		for (ComplexDataObject object : data)
			object.removeAttribute(attributeString);
	}

	public String getAttributeString() {
		return attributeString;
	}

	public void setAttributeString(String attributeString) {
		this.attributeString = attributeString;
	}

	@Override
	public DataProcessingCategory getPreprocessingCategory() {
		return DataProcessingCategory.DATA_REDUCTION;
	}
}
