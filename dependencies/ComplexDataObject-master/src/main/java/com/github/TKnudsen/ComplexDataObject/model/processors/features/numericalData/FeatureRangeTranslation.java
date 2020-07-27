package com.github.TKnudsen.ComplexDataObject.model.processors.features.numericalData;

import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeature;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVectorContainer;
import com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject.DataProcessingCategory;

/**
 * Allows the translation of all feature values by a given amount. Useful for
 * preventing illegal values for mathematical operations, e.g. values <= 0 when
 * applying log.
 * 
 * @author Christian Ritter
 *
 */
public class FeatureRangeTranslation implements INumericalFeatureVectorProcessor {

	private double translation;

	public FeatureRangeTranslation(double translation) {
		this.translation = translation;
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
				f.setFeatureValue(f.getFeatureValue() + translation);
			}
		}
	}

}
