package com.github.TKnudsen.ComplexDataObject.model.processors.features.numericalData;

import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeature;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVectorContainer;
import com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject.DataProcessingCategory;

/**
 * Allows to scale the range of feature values by a given factor.
 * 
 * @author Christian Ritter
 *
 */
public class FeatureRangeScaling implements INumericalFeatureVectorProcessor {

	private double scalingFactor;

	public FeatureRangeScaling(double scalingFactor) {
		this.scalingFactor = scalingFactor;
	}

	@Override
	public void process(List<NumericalFeatureVector> data) {
		process(new NumericalFeatureVectorContainer(data));
	}

	@Override
	public DataProcessingCategory getPreprocessingCategory() {
		return DataProcessingCategory.DATA_NORMALIZATION;
	}

	@Override
	public void process(NumericalFeatureVectorContainer container) {
		for (NumericalFeatureVector fv : container) {
			for (NumericalFeature f : fv.getVectorRepresentation()) {
				f.setFeatureValue(f.getFeatureValue() * scalingFactor);
			}
		}
	}

}
