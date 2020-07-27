package com.github.TKnudsen.ComplexDataObject.model.processors.features.numericalData;

import com.github.TKnudsen.ComplexDataObject.data.enums.NormalizationType;

public class NormalizationRoutineFactory {
	public static INumericalFeatureVectorProcessor createNormalizationRoutine(NormalizationType normalizationType) {

		INumericalFeatureVectorProcessor normalizer = null;

		if (normalizationType.equals(NormalizationType.normalizeMinMax))
			normalizer = new MinMaxNormalization();
		else if (normalizationType.equals(NormalizationType.normalizeMinMaxGlobal))
			normalizer = new MinMaxNormalization();
		else if (normalizationType.equals(NormalizationType.normalizeMinMaxBinWise))
			normalizer = new MinMaxNormalization();
		else if (normalizationType.equals(NormalizationType.offsetTranslation))
			throw new IllegalArgumentException("Offset Translation not yet supported in CDO");
		else if (normalizationType.equals(NormalizationType.amplitudeScaling))
			throw new IllegalArgumentException("Amplitude Scaling not yet supported in CDO");

		return normalizer;
	}
}
