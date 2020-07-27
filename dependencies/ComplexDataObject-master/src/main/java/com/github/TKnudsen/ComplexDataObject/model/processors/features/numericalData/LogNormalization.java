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
public class LogNormalization implements INumericalFeatureVectorProcessor {

	private double basis = 2;
	private boolean safeDomain = true;

	public LogNormalization() {
	}

	public LogNormalization(double basis) {
		this.basis = basis;
	}

	public double getBasis() {
		return basis;
	}

	@Override
	public DataProcessingCategory getPreprocessingCategory() {
		return DataProcessingCategory.DATA_NORMALIZATION;
	}

	public boolean isSafeDomain() {
		return safeDomain;
	}

	@Override
	public void process(List<NumericalFeatureVector> data) {
		process(new NumericalFeatureVectorContainer(data));
	}

	@Override
	public void process(NumericalFeatureVectorContainer container) {
		if (safeDomain) {
			MinMaxNormalization mmn = new MinMaxNormalization();
			mmn.process(container);
			// prevent values <= 0
			double epsilon = 0.00001;
			FeatureRangeScaling frs = new FeatureRangeScaling(1 - epsilon);
			frs.process(container);
			FeatureRangeTranslation frt = new FeatureRangeTranslation(epsilon);
			frt.process(container);
		}
		for (NumericalFeatureVector fv : container) {
			for (NumericalFeature f : fv.getVectorRepresentation()) {
				f.setFeatureValue(Math.log(f.getFeatureValue()) / Math.log(basis));
			}
		}
	}

	public void setBasis(double basis) {
		this.basis = basis;
	}

	public void setSafeDomain(boolean adaptDomain) {
		this.safeDomain = adaptDomain;
	}

}
