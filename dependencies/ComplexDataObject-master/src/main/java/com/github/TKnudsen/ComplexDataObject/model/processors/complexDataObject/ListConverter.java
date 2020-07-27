package com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject;

import java.util.Arrays;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataContainer;
import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataObject;
import com.github.TKnudsen.ComplexDataObject.model.processors.utility.IItemSplitter;

/**
 * Used to split each value for a given attribute of each
 * {@link ComplexDataObject} in the {@link ComplexDataContainer}, using a
 * routine implementing {@link IItemSplitter}.
 * 
 * @author Robert Heimbach
 *
 */
public class ListConverter implements IComplexDataObjectProcessor {

	private String attribute;
	private IItemSplitter splitter;

	public ListConverter(String attribute, IItemSplitter splitter) {
		this.attribute = attribute;
		this.splitter = splitter;
	}

	@Override
	public void process(ComplexDataContainer container) {
		for (ComplexDataObject complexDataObject : container)
			process(Arrays.asList(complexDataObject));
	}

	@Override
	public void process(List<ComplexDataObject> data) {
		for (ComplexDataObject complexDataObject : data) {

			// Load the old value
			Object oldValue = complexDataObject.getAttribute(attribute);

			// Split the old value and store if not null
			if (oldValue != null) {
				complexDataObject.add(attribute, splitter.split(oldValue));
			}
		}
	}

	@Override
	public DataProcessingCategory getPreprocessingCategory() {
		return DataProcessingCategory.SECONDARY_DATA_PROVIDER;
	}
}
