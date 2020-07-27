package com.github.TKnudsen.ComplexDataObject.model.processors.features.numericalData;

import java.util.ArrayList;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVectorContainer;
import com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject.DataProcessingCategory;

/**
 * <p>
 * Title: MissingValueRemover
 * </p>
 * 
 * <p>
 * Description: removes NumericalFeatureVectors from a collection if one of the
 * Features is NAN.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class MissingValueRemover implements INumericalFeatureVectorProcessor {

	@Override
	public void process(NumericalFeatureVectorContainer container) {
		List<NumericalFeatureVector> fvsToBeRemoved = new ArrayList<>();

		for (NumericalFeatureVector object : container)
			if (!isValid(object))
				fvsToBeRemoved.add(object);

		for (NumericalFeatureVector fv : fvsToBeRemoved)
			container.remove(fv);
	}

	@Override
	public void process(List<NumericalFeatureVector> data) {
		for (int i = 0; i < data.size(); i++)
			if (!isValid(data.get(i))) {
				data.remove(i);
				i--;
			}
	}

	private boolean isValid(NumericalFeatureVector object) {
		for (String featureName : object.getFeatureKeySet())
			if (object.getFeature(featureName) == null || Double.isNaN(object.getFeature(featureName).doubleValue()))
				return false;

		return true;
	}

	@Override
	public DataProcessingCategory getPreprocessingCategory() {
		return DataProcessingCategory.DATA_CLEANING;
	}

}
