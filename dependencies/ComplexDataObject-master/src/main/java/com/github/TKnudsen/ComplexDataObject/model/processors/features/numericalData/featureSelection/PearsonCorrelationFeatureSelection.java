package com.github.TKnudsen.ComplexDataObject.model.processors.features.numericalData.featureSelection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVectorContainer;
import com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject.DataProcessingCategory;
import com.github.TKnudsen.ComplexDataObject.model.processors.features.numericalData.INumericalFeatureVectorProcessor;

/**
 * @author Christian Ritter
 *
 */
public class PearsonCorrelationFeatureSelection implements INumericalFeatureVectorProcessor {

	private int nrFeatures;

	public PearsonCorrelationFeatureSelection(int nrOfFeaturesToSelect) {
		this.nrFeatures = nrOfFeaturesToSelect;
	}

	@Override
	public DataProcessingCategory getPreprocessingCategory() {
		return DataProcessingCategory.DATA_REDUCTION;
	}

	private double pearsonCorrelation(double[] x, double[] y) {
		if (x.length != y.length)
			throw new IllegalArgumentException("arrays have to have the same length");
		if (x.length == 0)
			throw new IllegalArgumentException("arrays must not be of length 0");
		double meanx = Arrays.stream(x).sum() / Arrays.stream(x).count();
		double meany = Arrays.stream(y).sum() / Arrays.stream(y).count();

		double numinator = 0;
		for (int i = 0; i < x.length; i++) {
			numinator += (x[i] - meanx) * (y[i] - meany);
		}

		double denominatorx = Math.sqrt(Arrays.stream(x).map(v -> Math.pow(v - meanx, 2)).sum());
		double denominatory = Math.sqrt(Arrays.stream(y).map(v -> Math.pow(v - meany, 2)).sum());

		double denominator = (denominatorx * denominatory);
		if (denominator == 0)
			return 0;
		return (numinator / denominator);
	}

	@Override
	public void process(List<NumericalFeatureVector> data) {
		process(new NumericalFeatureVectorContainer(data));
	}

	@Override
	public void process(NumericalFeatureVectorContainer container) {
		int n = -1;
		double[][] values = null;
		int i = 0;
		for (NumericalFeatureVector fv : container) {
			if (values == null) {
				n = fv.sizeOfFeatures();
				values = new double[n][container.size()];
			}
			if (fv.sizeOfFeatures() != n)
				throw new IllegalArgumentException("all feature vectors have to have the same number of features");
			for (int j = 0; j < n; j++) {
				values[j][i] = fv.getFeature(j).getFeatureValue();
			}
			i++;
		}
		double[] scores = new double[n];
		for (i = 0; i < n; i++) {
			for (int j = i + 1; j < n; j++) {
				double score = Math.abs(pearsonCorrelation(values[i], values[j]));
				scores[i] += score;
				scores[j] += score;
			}
		}

		int[] sorted = weka.core.Utils.stableSort(scores);
		List<Integer> indizesToRemove = new ArrayList<>();
		for (i = nrFeatures; i < sorted.length; i++) {
			indizesToRemove.add(sorted[i]);
		}
		IndexFeatureSelection ifs = new IndexFeatureSelection(indizesToRemove);
		ifs.process(container);
	}

}
