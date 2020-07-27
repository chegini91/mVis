package com.github.TKnudsen.DMandML.model.supervised.classifier.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.github.TKnudsen.ComplexDataObject.data.features.Feature;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeature;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVectorFactory;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVectorTools;
import com.github.TKnudsen.DMandML.model.supervised.classifier.IClassifier;

/**
 * Utility methods for the classifier tests
 */
class ClassifierTestUtils {
	
	/**
	 * Creates a set of training vectors for the classifier test.
	 * 
	 * @param classAttribute The class attribute, as obtained via {@link IClassifier#getClassAttribute()}
	 * @param numClasses The number of classes
	 * @return The training vectors
	 */
	static List<NumericalFeatureVector> createDefaultTrainingVectors(String classAttribute, int numClasses)
	{
		int numFeatureVectors = 100;
		Random random = new Random(0);
		return createDefaultLabeledVectors(numFeatureVectors, classAttribute, numClasses, random);
	}
	
	/**
	 * Creates a set of training vectors for the classifier test.
	 * 
	 * @param classAttribute The class attribute, as obtained via {@link IClassifier#getClassAttribute()}
	 * @param numClasses The number of classes
	 * @return The testing vectors
	 */
	static List<NumericalFeatureVector> createDefaultTestingVectors(String classAttribute, int numClasses)
	{
		int numFeatureVectors = 10;
		Random random = new Random(1);
		return createDefaultLabeledVectors(numFeatureVectors, classAttribute, numClasses, random);
	}
	
	/**
	 * Creates a set of labeled vectors for the classifier test.
	 * 
	 * @param count The number of vectors to create
	 * @param classAttribute The class attribute, as obtained via {@link IClassifier#getClassAttribute()}
	 * @param numClasses The number of classes
	 * @return The vectors
	 */
	private static List<NumericalFeatureVector> createDefaultLabeledVectors(int count, String classAttribute, int numClasses, Random random)
	{
		int dimensions = 10;
		List<NumericalFeatureVector> vectors = createFeatureVectors(count, dimensions, random);
		List<String> labels = createLables(count, numClasses, random);
		NumericalFeatureVectorTools.addClassAttribute(vectors, labels, classAttribute);
		return vectors;
	}
	
	
	/**
	 * Create a list with the given number of {@link NumericalFeatureVector} 
	 * objects with the given dimensions
	 * 
	 * @param count The number of vectors to create
	 * @param dimensions The dimensions
	 * @param random The random number generator 
	 * @return The feature vectors
	 */
	static List<NumericalFeatureVector> createFeatureVectors(int count, int dimensions, Random random) {
		List<NumericalFeatureVector> featureVectors = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			double[] vector = new double[dimensions];
			for (int d = 0; d < dimensions; d++) {
				vector[d] = random.nextDouble();
			}
			NumericalFeatureVector fv = NumericalFeatureVectorFactory.createNumericalFeatureVector(vector);
			fv.setName(i + "");
			featureVectors.add(fv);
		}

		return featureVectors;
	}

	/**
	 * Create a list containing the given number of label strings.
	 * The strings will be chosen randomly from <code>numLabels</code>
	 * different (unspecified) strings.
	 * 
	 * @param count The number of strings
	 * @param numLabels The number of different labels
	 * @param random The random number generator
	 * @return The list
	 */
	private static List<String> createLables(int count, int numLabels, Random random) {

		List<String> labels = new ArrayList<String>();

		String[] strings = new String[numLabels];
		for (int i = 0; i < numLabels; i++) {
			strings[i] = "class" + i;
		}

		for (int i = 0; i < count; i++) {
			labels.add(strings[random.nextInt(numLabels)]);
		}
		return labels;
	}
	
	/**
	 * Create a numerical feature vector from the given features, with
	 * a sensible <code>toString</code> implementation...
	 * 
	 * @param features The features
	 * @return The feature vector
	 */
	static NumericalFeatureVector create(List<NumericalFeature> features) {
		NumericalFeatureVector numericalFeatureVector = new NumericalFeatureVector(features) {
			@Override
			public String toString() {
				return createNumericalFeatureVectorString(this);
			}
		};
		return numericalFeatureVector;
	}
	
	/**
	 * Create a sensible string representation of the given numerical feature vector
	 * 
	 * @param numericalFeatureVector The feature vector
	 * @return The string
	 */
	private static String createNumericalFeatureVectorString(NumericalFeatureVector numericalFeatureVector) {
		StringBuilder sb = new StringBuilder();
		sb.append("NumericalFeatureVector[(");
		for (int i = 0; i < numericalFeatureVector.getSize(); i++) {
			if (i > 0) {
				sb.append(", ");
			}
			Feature<?> feature = numericalFeatureVector.getFeature(i);
			sb.append(feature.getFeatureName());
			sb.append("=");
			sb.append(feature.getFeatureValue());
		}
		sb.append(")");
		Set<String> attributeKeySet = numericalFeatureVector.keySet();
		if (!attributeKeySet.isEmpty()) {
			sb.append(", {");
			int counter = 0;
			for (String attributeKey : attributeKeySet) {
				if (counter > 0) {
					sb.append(", ");
				}
				sb.append(attributeKey);
				sb.append("=");
				sb.append(numericalFeatureVector.getAttribute(attributeKey));
			}
			sb.append("}");
		}
		sb.append("]");
		return sb.toString();
	}

}
