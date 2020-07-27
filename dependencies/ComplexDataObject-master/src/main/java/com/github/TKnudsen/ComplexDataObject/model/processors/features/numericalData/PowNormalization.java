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
public class PowNormalization implements INumericalFeatureVectorProcessor {

	private double pow = 2;
	private boolean safeDomain = false;

	/**
	 * 
	 */
	public PowNormalization() {
	}

	public PowNormalization(double power) {
		this.pow = power;
	}

	public double getPow() {
		return pow;
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
		}
		for (NumericalFeatureVector fv : container) {
			for (NumericalFeature f : fv.getVectorRepresentation()) {
				f.setFeatureValue(Math.pow(f.getFeatureValue(), pow));
			}
		}
	}

	public void setPow(double pow) {
		this.pow = pow;
	}

	public void setSafeDomain(boolean safeDomain) {
		this.safeDomain = safeDomain;
	}

}
