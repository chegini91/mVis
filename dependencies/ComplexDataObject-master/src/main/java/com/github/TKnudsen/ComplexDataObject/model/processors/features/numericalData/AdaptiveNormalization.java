package com.github.TKnudsen.ComplexDataObject.model.processors.features.numericalData;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeature;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVectorContainer;
import com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject.DataProcessingCategory;

/**
 * Compares different normalization methods based on a quality measure
 * (kurtosis) and chooses an individual normalization for each feature based on
 * that measure.
 * 
 * @author Christian Ritter
 *
 */
public class AdaptiveNormalization implements INumericalFeatureVectorProcessor {

	private List<INumericalFeatureVectorProcessor> baseProcessors;

	private MinMaxNormalization mmn = new MinMaxNormalization();

	public AdaptiveNormalization() {
	}

	public AdaptiveNormalization(List<INumericalFeatureVectorProcessor> baseProcessors) {
		this.baseProcessors = baseProcessors;
	}

	@Override
	public DataProcessingCategory getPreprocessingCategory() {
		return DataProcessingCategory.DATA_NORMALIZATION;
	}

	@Override
	public void process(List<NumericalFeatureVector> data) {
		process(new NumericalFeatureVectorContainer(data));
	}

	@Override
	public void process(NumericalFeatureVectorContainer container) {
		int numAttributes = 0;
		List<NumericalFeatureVector> data = new ArrayList<>();

		for (NumericalFeatureVector fv : container) {
			if (numAttributes != 0 && fv.sizeOfFeatures() != numAttributes)
				throw new IllegalArgumentException("All feature vectors must have the same number of features");
			numAttributes = fv.sizeOfFeatures();
			data.add(fv);
		}

		List<List<NumericalFeatureVector>> tmpFVs = new ArrayList<>();
		for (INumericalFeatureVectorProcessor n : baseProcessors)
			tmpFVs.add(new ArrayList<>());
		for (NumericalFeatureVector fv : container) {
			List<NumericalFeature> fLst = new ArrayList<>();
			for (NumericalFeature nf : fv.getVectorRepresentation()) {
				fLst.add(new NumericalFeature(nf.getFeatureName(), nf.getFeatureValue()));
			}
			for (List<NumericalFeatureVector> lst : tmpFVs) {
				NumericalFeatureVector fvt = new NumericalFeatureVector(new ArrayList<>(fLst));
				lst.add(fvt);
			}
		}
		List<Integer> indexToUse = new ArrayList<>();
		// select features from numerical features
		for (int i = 0; i < baseProcessors.size(); i++) {
			INumericalFeatureVectorProcessor normalization = baseProcessors.get(i);
			normalization.process(tmpFVs.get(i));
			mmn.process(tmpFVs.get(i));
		}

		// evaluate quality
		for (int i = 0; i < numAttributes; i++) {
			int index = 0;
			double value = Double.MAX_VALUE;
			for (int j = 0; j < baseProcessors.size(); j++) {
				List<NumericalFeatureVector> lst = tmpFVs.get(j);
				double[] vals = new double[lst.size()];
				for (int k = 0; k < lst.size(); k++) {
					vals[k] = lst.get(k).getFeature(i).getFeatureValue();
				}
				DescriptiveStatistics ds = new DescriptiveStatistics(vals);
				double k = ds.getKurtosis();
				if (k < value) {
					value = k;
					index = j;
				}
			}
			indexToUse.add(index);
		}

		for (int i = 0; i < numAttributes; i++) {
			int index = indexToUse.get(i);
			for (int j = 0; j < data.size(); j++) {
				NumericalFeature nf = tmpFVs.get(index).get(j).getFeature(i);
				data.get(j).getFeature(i).setFeatureValue(nf.getFeatureValue());
			}
		}
	}

}
