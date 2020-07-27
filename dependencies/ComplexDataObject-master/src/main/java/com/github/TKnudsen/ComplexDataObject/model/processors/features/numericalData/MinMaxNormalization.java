package com.github.TKnudsen.ComplexDataObject.model.processors.features.numericalData;

import java.util.Arrays;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVectorContainer;
import com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject.DataProcessingCategory;
import com.github.TKnudsen.ComplexDataObject.model.tools.MathFunctions;

/**
 * <p>
 * Title: MinMaxNormalization
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
 * @author Christian Ritter
 * @version 1.1
 */
public class MinMaxNormalization implements INumericalFeatureVectorProcessor {

	@Override
	public void process(List<NumericalFeatureVector> data) {
		process(new NumericalFeatureVectorContainer(data));
	}

	@Override
	public void process(NumericalFeatureVectorContainer container) {
		Double[] mins = null;
		Double[] maxs = null;
		for (NumericalFeatureVector fv : container) {
			if (mins == null) {
				mins = new Double[fv.sizeOfFeatures()];
				Arrays.fill(mins, Double.POSITIVE_INFINITY);
				maxs = new Double[fv.sizeOfFeatures()];
				Arrays.fill(maxs, Double.NEGATIVE_INFINITY);
			}
			for (int i = 0; i < mins.length; i++) {
				double v = fv.get(i);
				if (mins[i] > v)
					mins[i] = v;
				if (maxs[i] < v)
					maxs[i] = v;
			}
		}
		
		for (NumericalFeatureVector fv : container) {
			for (int i = 0; i < fv.sizeOfFeatures(); i++) {
				if (i >= mins.length)
					throw new IllegalArgumentException("Feature vectors have to match in size.");
				double v = fv.get(i);
				fv.getFeature(i).setFeatureValue(MathFunctions.linearScale(mins[i], maxs[i], v));
			}
		}
	}

	@Override
	public DataProcessingCategory getPreprocessingCategory() {
		return DataProcessingCategory.DATA_NORMALIZATION;
	}
}