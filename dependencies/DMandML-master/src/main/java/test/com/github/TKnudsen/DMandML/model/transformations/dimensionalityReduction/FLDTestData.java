package test.com.github.TKnudsen.DMandML.model.transformations.dimensionalityReduction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeature;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;


public class FLDTestData {
	
	static List<NumericalFeatureVector> createTestData() {
		//return createTestDataA();
		return createTestDataB();
	}
	
	static List<NumericalFeatureVector> createTestDataB() {

		Random random = new Random(0);
		final String classAttributeName = "class";
		
		int dim = 5;
		double noiseSize = 0.1;
		List<NumericalFeatureVector> featureVectors = new ArrayList<>();
		featureVectors.addAll(createTestData(0, 0, 10, 10, noiseSize, 30, dim, random, classAttributeName, 0));
		featureVectors.addAll(createTestData(1, 0, 11, 10, noiseSize, 30, dim, random, classAttributeName, 1));
		featureVectors.addAll(createTestData(0, 1, 10, 11, noiseSize, 30, dim, random, classAttributeName, 2));
		return featureVectors;
	}
	
	
	private static List<NumericalFeatureVector> createTestData(
			double x0, double y0, double x1, double y1, double noiseSize, int n, int dim, Random random, String classAttributeName, int classLabel) {
		
		List<NumericalFeatureVector> featureVectors = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			double alpha = (double)i / (n-1);
			double x = x0 + alpha * (x1 - x0);
			double y = y0 + alpha * (y1 - y0);
			double[] values = createRandom(x, y, noiseSize, dim, random);
			NumericalFeatureVector numericalFeatureVector = create(values, classAttributeName, classLabel);
			featureVectors.add(numericalFeatureVector);
		}
		return featureVectors;
	}
	
	
	private static double[] createRandom(double x, double y, double noiseSize, int dim, Random random) {
		double result[] = new double[dim];
		
		result[0] = x + (-0.5 + random.nextDouble()) * noiseSize;
		result[1] = y + (-0.5 + random.nextDouble()) * noiseSize;
		for (int i=2; i<dim; i++)
		{
			result[i] = (-0.5 + random.nextDouble()) * noiseSize;
		}
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	static List<NumericalFeatureVector> createTestDataA() {

		Random random = new Random(0);
		final String classAttributeName = "class";

		double minA[] = { 0.0, 0.0, -0.1, -0.1, -0.1 };
		double maxA[] = { 1.0, 1.0,  0.1,  0.1,  0.1 };
		double minB[] = { 0.5, 0.5, -0.1, -0.1, -0.1 };
		double maxB[] = { 4.5, 4.5,  0.1,  0.1,  0.1 };
		double minC[] = { 4.0, 4.0, -0.1, -0.1, -0.1 };
		double maxC[] = { 5.0, 5.0,  0.1,  0.1,  0.1 };
		
		List<NumericalFeatureVector> featureVectors = new ArrayList<>();
		featureVectors.addAll(createTestData(minA, maxA, 30, random, classAttributeName, 0));
		featureVectors.addAll(createTestData(minB, maxB, 30, random, classAttributeName, 1));
		featureVectors.addAll(createTestData(minC, maxC, 30, random, classAttributeName, 2));
		return featureVectors;
	}

	private static List<NumericalFeatureVector> createTestData(
			double min[], double max[], int n, Random random, String classAttributeName, int classLabel) {
		
		List<NumericalFeatureVector> featureVectors = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			double[] values = createRandom(min, max, random);
			NumericalFeatureVector numericalFeatureVector = create(values, classAttributeName, classLabel);
			featureVectors.add(numericalFeatureVector);
		}
		return featureVectors;
	}
	
	
	private static double[] createRandom(double min[], double max[], Random random)
	{
		if (min.length != max.length) {
			throw new IllegalArgumentException(
					"min.length and max.length must be equal, but are " + min.length + " and " + max.length);
		}
		int n = min.length;
		double result[] = new double[n];
		for (int i=0; i<n; i++)
		{
			double vMin = min[i];
			double vMax = max[i];
			double v = vMin + random.nextDouble() * (vMax - vMin);
			result[i] = v;
		}
		return result;
	}
	
	
	
	private static NumericalFeatureVector create(double values[], String classAttributeName, int classLabel) {
		List<NumericalFeature> features = new ArrayList<>();
		int n = values.length;
		for (int i = 0; i < n; i++) {
			features.add(new NumericalFeature("values[" + i + "]", values[i]));
		}
		NumericalFeatureVector numericalFeatureVector = new NumericalFeatureVector(features);
		numericalFeatureVector.add(classAttributeName, classLabel);
		return numericalFeatureVector;
	}
	
	
}
