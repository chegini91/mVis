package com.github.TKnudsen.ComplexDataObject.model.processors.features.numericalData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeature;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVectorContainer;
import com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject.DataProcessingCategory;
import com.github.TKnudsen.ComplexDataObject.model.tools.MathFunctions;

/**
 * <p>
 * Title: AbstractOutlierTreatment
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * @author Christian Ritter
 * @version 1.01
 */
public abstract class AbstractOutlierTreatment implements INumericalFeatureVectorProcessor {

	@Override
	public void process(List<NumericalFeatureVector> data) {
		process(new NumericalFeatureVectorContainer(data));
	}

	@Override
	public DataProcessingCategory getPreprocessingCategory() {
		return DataProcessingCategory.DATA_CLEANING;
	}

	@Override
	public void process(NumericalFeatureVectorContainer container) {
		Map<String, Double> newMinValues = new HashMap<>();
		Map<String, Double> newMaxValues = new HashMap<>();
		for (String featureName : container.getFeatureNames()) {
			Map<Long, Object> valueMap = container.getFeatureValues(featureName);
			List<Double> values = new ArrayList<>(valueMap.size());
			for (Object value : valueMap.values()) {
				if (value instanceof NumericalFeature)
					values.add(((NumericalFeature) value).getFeatureValue());
			}
			Entry<Double, Double> bounds = calculateBounds(values);
			newMinValues.put(featureName, bounds.getKey());
			newMaxValues.put(featureName, bounds.getValue());
		}

		for (NumericalFeatureVector featureVector : container) {
			for (NumericalFeature feature : featureVector.getVectorRepresentation()) {
				feature.setFeatureValue(MathFunctions.linearScale(newMinValues.get(feature.getFeatureName()), newMaxValues.get(feature.getFeatureName()), feature.getFeatureValue(), true));
			}
		}
	}

	/**
	 * Calculates a new bounds for the given feature domain that is used to remove/crop outliers.
	 * 
	 * @param values
	 *            a list containing all values of a given feature
	 * @return an entry containing the lower bound as key and the upper bound as value
	 */
	protected abstract Entry<Double, Double> calculateBounds(List<Double> values);

}