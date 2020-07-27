package com.github.TKnudsen.DMandML.model.transformations.dimensionalityReduction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject.DataTransformationCategory;
import com.github.TKnudsen.ComplexDataObject.model.transformations.dimensionalityReduction.IDimensionalityReduction;

/**
 * Implementation of Fisher's linear discriminant (FLD), a special form of LDA,
 * based on the "Smile" library from http://haifengl.github.io/smile/
 * 
 * Instances of this class may be created with the {@link FLDs} class
 */
public class FLD implements IDimensionalityReduction<NumericalFeatureVector> {
	private final int outputDimensionality;
	private final Map<NumericalFeatureVector, NumericalFeatureVector> highToLow;

	FLD(int outputDimensionality, Map<NumericalFeatureVector, NumericalFeatureVector> highToLow) {
		this.outputDimensionality = outputDimensionality;
		this.highToLow = highToLow;
	}

	@Override
	public List<NumericalFeatureVector> transform(NumericalFeatureVector numericalFeatureVector) {
		return transform(Collections.singletonList(numericalFeatureVector));
	}

	@Override
	public List<NumericalFeatureVector> transform(List<NumericalFeatureVector> numericalFeatureVectors) {

		List<NumericalFeatureVector> result = new ArrayList<NumericalFeatureVector>();
		for (NumericalFeatureVector high : numericalFeatureVectors) {
			result.add(highToLow.get(high));
		}
		return result;
	}

	@Override
	public DataTransformationCategory getDataTransformationCategory() {
		return DataTransformationCategory.DIMENSION_REDUCTION;
	}

	@Override
	public int getOutputDimensionality() {
		return outputDimensionality;
	}

	@Override
	public Map<NumericalFeatureVector, NumericalFeatureVector> getMapping() {
		return Collections.unmodifiableMap(highToLow);
	}

	@Override
	public void calculateDimensionalityReduction() {
		// TODO Auto-generated method stub
	}

}
