package com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject;

import java.util.List;
import java.util.Set;

import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataContainer;
import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataObject;
import com.github.TKnudsen.ComplexDataObject.model.processors.utility.IUniqueValuesIdentifier;

/**
 * A multistage preprocessing routine.
 * 
 * <p>
 * For a given attribute, which is assumed to store a List of Objects, all
 * unique values are identified, using a {@link IUniqueValuesIdentifier}, and
 * Dummy Variables, stored as 0/1 Integer, are created.
 * 
 * <p>
 * The naming scheme of the dummy variables is as follows: <br>
 * attribute-nameOfUniqueValue
 * 
 * <p>
 * The old attribute storing the List is removed.
 * 
 * @author Robert Heimbach
 *
 */
public class DummyVariableConverter implements IComplexDataObjectProcessor {

	private String attribute;
	private IUniqueValuesIdentifier uniqueValuesIdentifier;

	public DummyVariableConverter(String attribute, IUniqueValuesIdentifier uniqueValuesIdentifier) {
		this.attribute = attribute;
		this.uniqueValuesIdentifier = uniqueValuesIdentifier;

		// Set the attribute to be checked if not done already
		if (!uniqueValuesIdentifier.hasAttribute()) {
			uniqueValuesIdentifier.setAttribute(this.attribute);
		}
	}

	@Override
	public void process(ComplexDataContainer container) {

		// Get a list of unique values for the given attribute
		if (!uniqueValuesIdentifier.hasDataContainer()) {
			uniqueValuesIdentifier.setDataContainer(container);
		}

		Set<?> uniqueValues = uniqueValuesIdentifier.getUniqueValues();

		// Add the to-be-created dummy variables to the data schema
		for (Object val : uniqueValues) {
			container.addAttribute(attribute + "-" + val.toString(), Integer.class, null);
		}

		for (ComplexDataObject complexDataObject : container) {

			Object values = complexDataObject.getAttribute(attribute);

			if (values != null && values instanceof List) {

				List<?> listOfValues = (List<?>) values;

				// Fill out the dummy variables
				for (Object val : uniqueValues) {

					if (listOfValues.contains(val)) {
						complexDataObject.add(attribute + "-" + val.toString(), new Integer(1));
					} else {
						complexDataObject.add(attribute + "-" + val.toString(), new Integer(0));
					}
				}

				// remove the old list attribute from (each) data object
				complexDataObject.removeAttribute(attribute);
			}
		}

		// Remove the old attribute from the data schema
		container.remove(attribute);
	}
	
	@Override
	public void process(List<ComplexDataObject> data) {
		ComplexDataContainer container = new ComplexDataContainer(data);
		process(container);
	}

	@Override
	public DataProcessingCategory getPreprocessingCategory() {
		return DataProcessingCategory.SECONDARY_DATA_PROVIDER;
	}
}
