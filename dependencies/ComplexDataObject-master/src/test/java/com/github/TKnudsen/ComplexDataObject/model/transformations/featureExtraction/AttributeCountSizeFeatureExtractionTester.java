package com.github.TKnudsen.ComplexDataObject.model.transformations.featureExtraction;

import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataObject;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeature;
import com.github.TKnudsen.ComplexDataObject.model.io.parsers.TitanicDataSetParserTester;

public class AttributeCountSizeFeatureExtractionTester {

	public static void main(String[] args) {
		List<ComplexDataObject> passengers = TitanicDataSetParserTester.parseTitanicDataSet();

		AttributeSizeCounter attributeSizeCounter = new AttributeSizeCounter(true);
		List<NumericalFeature> transformed = attributeSizeCounter.transform(passengers);

		for (int i = 0; i < passengers.size(); i++)
			System.out.println("Passenger " + (i + 1) + " has " + transformed.get(i) + " attributes");
	}

}
