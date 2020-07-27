package com.github.TKnudsen.ComplexDataObject.model.processors.features.numericalData;

import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVectorContainer;
import com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject.DataProcessingCategory;

/**
 * @author Christian Ritter
 *
 */
public class MeanNormalization implements INumericalFeatureVectorProcessor {

	@Override
	public void process(List<NumericalFeatureVector> data) {
		if (data.size() < 1)
			return;
		for (int i = 0; i < data.get(0).sizeOfFeatures(); i++) {
			double mean = 0.0;
			int sum = 0;
			for (NumericalFeatureVector fv : data) {
				mean += fv.getFeature(i).getFeatureValue();
				sum++;
			}
			mean = mean / sum;
			for (NumericalFeatureVector fv : data) {
				fv.getFeature(i).setFeatureValue(fv.getFeature(i).getFeatureValue() - mean);
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
