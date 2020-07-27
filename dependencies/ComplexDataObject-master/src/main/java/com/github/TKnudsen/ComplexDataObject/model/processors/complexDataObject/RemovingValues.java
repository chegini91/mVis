package com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataContainer;
import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataObject;

/**
 * Used to delete a given list of (extreme) values from each
 * {@link ComplexDataObject} in the {@link ComplexDataContainer}.
 * 
 * @author Robert Heimbach
 *
 * @param <T>
 */
public class RemovingValues<T> implements IComplexDataObjectProcessor {

	private String attribute;
	private Set<T> valuesToRemove;

	public RemovingValues(String attribute, List<T> valuesToRemove) {
		this.attribute = attribute;
		this.valuesToRemove = new HashSet<T>(valuesToRemove);
	}

	@Override
	public void process(ComplexDataContainer container) {
		for (ComplexDataObject complexDataObject : container)
			process(Arrays.asList(complexDataObject));
	}

	@Override
	public void process(List<ComplexDataObject> data) {
		for (ComplexDataObject complexDataObject : data) {

			Object value = complexDataObject.getAttribute(attribute);

			if (value != null && valuesToRemove.contains(value)) {

				complexDataObject.removeAttribute(attribute);
			}
		}
	}

	@Override
	public DataProcessingCategory getPreprocessingCategory() {
		return DataProcessingCategory.DATA_CLEANING;
	}
}
