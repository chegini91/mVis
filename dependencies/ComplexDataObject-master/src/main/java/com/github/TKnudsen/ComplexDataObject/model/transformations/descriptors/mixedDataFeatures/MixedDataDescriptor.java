package com.github.TKnudsen.ComplexDataObject.model.transformations.descriptors.mixedDataFeatures;

import java.util.ArrayList;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataContainer;
import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataObject;
import com.github.TKnudsen.ComplexDataObject.data.enums.FuzzyBooleanCategory;
import com.github.TKnudsen.ComplexDataObject.data.enums.FuzzyBooleanCategoryTools;
import com.github.TKnudsen.ComplexDataObject.data.features.FeatureType;
import com.github.TKnudsen.ComplexDataObject.data.features.mixedData.MixedDataFeature;
import com.github.TKnudsen.ComplexDataObject.data.features.mixedData.MixedDataFeatureTools;
import com.github.TKnudsen.ComplexDataObject.data.features.mixedData.MixedDataFeatureVector;
import com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject.DataTransformationCategory;
import com.github.TKnudsen.ComplexDataObject.model.transformations.descriptors.IDescriptor;
import com.github.TKnudsen.ComplexDataObject.model.transformations.descriptors.IMixedDataFeatureVectorDescriptor;

/**
 * <p>
 * Title: MixedDataDescriptor
 * </p>
 *
 * <p>
 * Description: creates MixedDataFeatureVector out of a given list of
 * ComplexDataObjects. Tries to transform all attributes.
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2016-2017
 * </p>
 *
 * @author Juergen Bernard
 * @version 1.03
 */
public class MixedDataDescriptor implements IMixedDataFeatureVectorDescriptor<ComplexDataObject> {

	@Override
	public List<MixedDataFeatureVector> transform(ComplexDataObject complexDataObject) {
		List<MixedDataFeatureVector> mixedDataFeatureVectors = new ArrayList<>();

		List<MixedDataFeature> features = new ArrayList<>();

		for (String string : complexDataObject) {
			Object value = complexDataObject.getAttribute(string);

			try {
				FeatureType featureType = MixedDataFeatureTools.guessFeatureType(value);
				features.add(new MixedDataFeature(string, value, featureType));
			} catch (IllegalArgumentException e) {
				if (value != null && value instanceof FuzzyBooleanCategory) {
					String v = FuzzyBooleanCategoryTools.convertToCategorical((FuzzyBooleanCategory) value);
					features.add(new MixedDataFeature(string, v, FeatureType.STRING));
				}
			}
		}

		MixedDataFeatureVector fv = new MixedDataFeatureVector(features);
		fv.setMaster(complexDataObject);
		mixedDataFeatureVectors.add(fv);

		return mixedDataFeatureVectors;
	}

	@Override
	public List<MixedDataFeatureVector> transform(List<ComplexDataObject> complexDataObjects) {

		List<MixedDataFeatureVector> mixedDataFeatureVectors = new ArrayList<>();

		for (ComplexDataObject object : complexDataObjects) {
			mixedDataFeatureVectors.addAll(transform(object));
		}

		return mixedDataFeatureVectors;
	}

	public List<MixedDataFeatureVector> transform(ComplexDataContainer container) {

		List<MixedDataFeatureVector> mixedDataFeatureVectors = new ArrayList<>();

		for (ComplexDataObject object : container) {
			mixedDataFeatureVectors.addAll(transform(object));
		}

		return mixedDataFeatureVectors;
	}

	@Override
	public String getName() {
		return "MixedDataFeatureVectorDescriptor";
	}

	@Override
	public String getDescription() {
		return "Creates MixedDataFeatureVector out of a given list of ComplexDataObjects. Tries to transform all attributes.";
	}

	@Override
	public List<IDescriptor<ComplexDataObject, MixedDataFeatureVector>> getAlternativeParameterizations(int count) {
		return null;
	}

	@Override
	public DataTransformationCategory getDataTransformationCategory() {
		return DataTransformationCategory.DESCRIPTOR;
	}
}
