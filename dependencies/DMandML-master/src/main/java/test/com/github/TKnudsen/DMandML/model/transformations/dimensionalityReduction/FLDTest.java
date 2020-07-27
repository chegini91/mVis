package test.com.github.TKnudsen.DMandML.model.transformations.dimensionalityReduction;

import java.util.Arrays;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.DMandML.model.transformations.dimensionalityReduction.FLD;
import com.github.TKnudsen.DMandML.model.transformations.dimensionalityReduction.FLDs;

public class FLDTest {
	
	static final String CLASS_ATTRIBUTE_NAME = "class";
	
	public static void main(String[] args) {
		
		List<NumericalFeatureVector> testData = FLDTestData.createTestData();
		final int outputDimensionality = 2;

		FLD fld = FLDs.compute(testData, CLASS_ATTRIBUTE_NAME, outputDimensionality);

		for (NumericalFeatureVector high : testData) {
			NumericalFeatureVector low = fld.transform(high).get(0);
			System.out.println(Arrays.toString(high.getVector()) + " mapped to " + Arrays.toString(low.getVector()));
		}
	}
}
