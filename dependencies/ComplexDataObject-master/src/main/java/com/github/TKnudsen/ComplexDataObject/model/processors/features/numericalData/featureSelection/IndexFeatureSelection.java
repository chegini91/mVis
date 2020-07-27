package com.github.TKnudsen.ComplexDataObject.model.processors.features.numericalData.featureSelection;

import java.util.Collections;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVectorContainer;
import com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject.DataProcessingCategory;
import com.github.TKnudsen.ComplexDataObject.model.processors.features.numericalData.INumericalFeatureVectorProcessor;

/**
 * @author Christian Ritter
 *
 */
public class IndexFeatureSelection implements INumericalFeatureVectorProcessor {

	private List<Integer> indizesToRemove;

	public IndexFeatureSelection(List<Integer> indizesToRemove) {
		this.indizesToRemove = indizesToRemove;
	}

	@Override
	public DataProcessingCategory getPreprocessingCategory() {
		return DataProcessingCategory.DATA_REDUCTION;
	}

	@Override
	public void process(List<NumericalFeatureVector> data) {
		process(new NumericalFeatureVectorContainer(data));
	}

	@Override
	public void process(NumericalFeatureVectorContainer container) {
		Collections.sort(indizesToRemove);
		for (NumericalFeatureVector fv : container) {
			int count = 0;
			for (Integer i : indizesToRemove) {
				fv.removeFeature(i - count);
				count++;
			}
		}
	}

}
