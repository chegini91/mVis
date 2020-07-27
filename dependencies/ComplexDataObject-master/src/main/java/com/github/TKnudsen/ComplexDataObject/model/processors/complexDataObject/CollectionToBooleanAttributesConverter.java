package com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataContainer;
import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataObject;
import com.github.TKnudsen.ComplexDataObject.data.enums.FuzzyBooleanCategory;

/**
 * <p>
 * Title: CollectionToBooleanAttributesConverter
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
public class CollectionToBooleanAttributesConverter implements IComplexDataObjectProcessor {

	private String attributeString;

	private boolean useFuzzyBoolean = false;

	public CollectionToBooleanAttributesConverter(String attributeString) {
		this.attributeString = attributeString;
	}

	@Override
	public void process(ComplexDataContainer container) {

		// 1) gather all observations
		Set<String> observations = new HashSet<>();
		Iterator<ComplexDataObject> iterator = container.iterator();
		while (iterator.hasNext()) {
			ComplexDataObject next = iterator.next();
			Object object = next.getAttribute(attributeString);
			if (object instanceof Collection<?>) {
				Collection<?> collection = (Collection<?>) object;
				for (Object element : collection)
					if (element instanceof String)
						observations.add((String) element);
			}
		}

		// 2) create new (Fuzzy) Boolean category attributes
		for (String string : observations)
			if (useFuzzyBoolean)
				container.addAttribute(attributeString + "_" + string, FuzzyBooleanCategory.class, FuzzyBooleanCategory.NO_INFORMATION);
			else
				container.addAttribute(attributeString + "_" + string, Boolean.class, null);

		// 3) add entries for the objects
		iterator = container.iterator();
		while (iterator.hasNext()) {
			ComplexDataObject next = iterator.next();
			Object object = next.getAttribute(attributeString);
			Collection<?> collection = null;
			if (object != null && object instanceof Collection<?>)
				collection = (Collection<?>) object;

			if (useFuzzyBoolean)
				for (String string : observations) {
					if (collection == null)
						next.add(attributeString + "_" + string, FuzzyBooleanCategory.NO_INFORMATION);
					else if (collection != null && collection.contains(string))
						next.add(attributeString + "_" + string, FuzzyBooleanCategory.YES);
					else
						next.add(attributeString + "_" + string, FuzzyBooleanCategory.NO);
				}
			else
				for (String string : observations) {
					if (collection == null)
						next.add(attributeString + "_" + string, null);
					else if (collection != null && collection.contains(string))
						next.add(attributeString + "_" + string, true);
					else
						next.add(attributeString + "_" + string, false);
				}
		}
	}

	@Override
	public void process(List<ComplexDataObject> data) {
		ComplexDataContainer container = new ComplexDataContainer(data);
		process(container);
	}

	public String getAttributeString() {
		return attributeString;
	}

	public void setAttributeString(String attributeString) {
		this.attributeString = attributeString;
	}

	@Override
	public DataProcessingCategory getPreprocessingCategory() {
		return DataProcessingCategory.SECONDARY_DATA_PROVIDER;
	}

	public boolean isUseFuzzyBoolean() {
		return useFuzzyBoolean;
	}

	public void setUseFuzzyBoolean(boolean useFuzzyBoolean) {
		this.useFuzzyBoolean = useFuzzyBoolean;
	}
}
