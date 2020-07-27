package com.github.TKnudsen.ComplexDataObject.model.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataContainer;
import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataObject;
import com.github.TKnudsen.ComplexDataObject.data.features.mixedData.MixedDataFeatureVector;
import com.github.TKnudsen.ComplexDataObject.model.io.parsers.TitanicDataSetParserTester;
import com.github.TKnudsen.ComplexDataObject.model.io.parsers.arff.ARFFParser;
import com.github.TKnudsen.ComplexDataObject.model.io.parsers.arff.WekaTools;
import com.github.TKnudsen.ComplexDataObject.model.transformations.descriptors.mixedDataFeatures.MixedDataDescriptor;

import weka.core.Instances;

public class WekaConversionTester {

	public static void main(String[] args) throws IOException {

		//testConversion();
		testPerformance();
	}
	
	private static void testConversion() {
		List<ComplexDataObject> data = TitanicDataSetParserTester.parseTitanicDataSet();
		// List<ComplexDataObject> data = createSimpleDataSet();

		MixedDataDescriptor descriptor = new MixedDataDescriptor();
		List<MixedDataFeatureVector> featureVectors = descriptor.transform(data);

		Instances instances = WekaConversion.getInstances(featureVectors, true);
		System.out.println(instances);

		ComplexDataContainer container2 = new ComplexDataContainer(WekaTools.getComplexDataObjects(instances));
		for (ComplexDataObject o : container2)
			System.out.println(o);
		
	}

	@SuppressWarnings("unused")
	private static List<ComplexDataObject> loadCarsDataTest() throws IOException {
		ARFFParser parser = new ARFFParser();
		List<ComplexDataObject> cars = parser.parse("data/cars.arff");

		return cars;
	}

	@SuppressWarnings("unused")
	private static List<ComplexDataObject> createSimpleDataSet() {
		List<ComplexDataObject> complexDataObjects = new ArrayList<>();

		ComplexDataObject o1 = new ComplexDataObject(1L);
		o1.add("Attribute 1", 1.0);
		o1.add("Attribute 2", true);
		o1.add("Attribute 3", "pete");
		complexDataObjects.add(o1);

		ComplexDataObject o2 = new ComplexDataObject(2L);
		o2.add("Attribute 1", 2.0);
		o2.add("Attribute 2", false);
		o2.add("Attribute 3", "paul");
		complexDataObjects.add(o2);

		return complexDataObjects;
	}
	
	
	private static void testPerformance() 
	{
		for (int numStringAttributes = 0; numStringAttributes <= 1; numStringAttributes++) {
			for (int numDoubleAttributes = 10; numDoubleAttributes <= 90; numDoubleAttributes += 10) {
				for (int numObjects = 5000; numObjects <= 10000; numObjects += 5000) {
					List<MixedDataFeatureVector> featureVectors = createTestVectors(numDoubleAttributes,
							numStringAttributes, numObjects);

					long beforeNs = System.nanoTime();
					Instances instances = WekaConversion.getInstances(featureVectors, true);
					long afterNs = System.nanoTime();

					System.out.println("For " + numStringAttributes + " string and " + numDoubleAttributes
							+ " double attributes, converting " + numObjects + " vectors into " + instances.size()
							+ " took " + ((afterNs - beforeNs) / 1e6) + "ms");
				}
			}
		}
	}

	private static List<MixedDataFeatureVector> createTestVectors(int numDoubleAttributes, int numStringAttributes, int numObjects) {
		List<ComplexDataObject> cdos = createTestObjects(numDoubleAttributes, numStringAttributes, numObjects);
		MixedDataDescriptor descriptor = new MixedDataDescriptor();
		List<MixedDataFeatureVector> featureVectors = descriptor.transform(cdos);
		return featureVectors;
	}
	
	
	private static List<ComplexDataObject> createTestObjects(int numDoubleAttributes, int numStringAttributes, int numObjects)
	{
		List<ComplexDataObject> cdos = new ArrayList<>();
		for (int i=0; i<numObjects; i++) {
			cdos.add(createTestObject(numDoubleAttributes, numStringAttributes));
		}
		return cdos;
	}
	
	private static ComplexDataObject createTestObject(int numDoubleAttributes, int numStringAttributes)
	{
		ComplexDataObject cdo = new ComplexDataObject();
		for (int i=0; i<numStringAttributes; i++) {
			cdo.add("StringAttribute"+i, "String"+i);
		}
		for (int i=0; i<numDoubleAttributes; i++) {
			cdo.add("DoubleAttribute"+i, 0.0);
		}
		return cdo;
		
		
	}
	

}
