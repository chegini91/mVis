package com.github.TKnudsen.ComplexDataObject.model.transformations.featureExtraction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataObject;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeature;
import com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject.DataTransformationCategory;

/**
 * <p>
 * Title: AttributeSizeCounter
 * </p>
 * 
 * <p>
 * Description: Counts the number of attributes provided in a ComplexDataObject.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class AttributeSizeCounter implements IFeatureExtractor<ComplexDataObject, NumericalFeature> {

	private boolean considerNameAndDescrioption = true;
	private String targetAttributeName = "AttributeCount";

	public AttributeSizeCounter() {

	}

	public AttributeSizeCounter(boolean considerNameAndDescrioption) {
		this.considerNameAndDescrioption = considerNameAndDescrioption;
	}

	@Override
	public List<NumericalFeature> transform(ComplexDataObject input) {
		if (input == null)
			return null;

		int count = 0;

		Iterator<String> iterator = input.iterator();
		while (iterator.hasNext()) {
			String attribute = iterator.next();
			if (!considerNameAndDescrioption && (attribute.equals("Name") || attribute.equals("Description")))
				continue;

			count++;
		}

		NumericalFeature feature = new NumericalFeature("AttributeCount", new Double(count));

		return Arrays.asList(new NumericalFeature[] { feature });
	}

	@Override
	public List<NumericalFeature> transform(List<ComplexDataObject> inputObjects) {
		List<NumericalFeature> features = new ArrayList<>();

		for (ComplexDataObject cdo : inputObjects)
			features.addAll(transform(cdo));

		return features;
	}

	@Override
	public DataTransformationCategory getDataTransformationCategory() {
		return DataTransformationCategory.FEATURE_EXTRACTION;
	}

	public boolean isConsiderNameAndDescrioption() {
		return considerNameAndDescrioption;
	}

	public void setConsiderNameAndDescrioption(boolean considerNameAndDescrioption) {
		this.considerNameAndDescrioption = considerNameAndDescrioption;
	}

	public String getTargetAttributeName() {
		return targetAttributeName;
	}

	public void setTargetAttributeName(String targetAttributeName) {
		this.targetAttributeName = targetAttributeName;
	}
}
