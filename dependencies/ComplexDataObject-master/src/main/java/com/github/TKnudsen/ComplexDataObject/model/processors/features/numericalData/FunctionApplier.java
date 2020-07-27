package com.github.TKnudsen.ComplexDataObject.model.processors.features.numericalData;

import java.util.List;
import java.util.function.Function;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeature;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVectorContainer;
import com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject.DataProcessingCategory;

/**
 * Allows to apply an arbitrary function from double to double to each feature
 * value of a feature vector.
 * 
 * @author Christian Ritter
 *
 */
public class FunctionApplier implements INumericalFeatureVectorProcessor {

	private Function<Double, Double> function;

	public FunctionApplier(Function<Double, Double> function) {
		this.function = function;
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
				f.setFeatureValue(function.apply(f.getFeatureValue()));
			}
		}
	}

}
