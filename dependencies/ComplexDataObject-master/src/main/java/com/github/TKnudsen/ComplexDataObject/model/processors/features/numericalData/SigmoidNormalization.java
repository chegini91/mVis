package com.github.TKnudsen.ComplexDataObject.model.processors.features.numericalData;

import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeature;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVectorContainer;
import com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject.DataProcessingCategory;

/**
 * @author Christian Ritter
 *
 */
public class SigmoidNormalization implements INumericalFeatureVectorProcessor {

	@Override
	public void process(List<NumericalFeatureVector> data) {
		for (NumericalFeatureVector fv : data) {
			for (NumericalFeature f : fv.getVectorRepresentation()) {
				f.setFeatureValue(1.0 / (1.0 + Math.exp(-f.getFeatureValue())));
			}
		}
	}

	@Override
	public DataProcessingCategory getPreprocessingCategory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void process(NumericalFeatureVectorContainer container) {
		// TODO Auto-generated method stub

	}

}
