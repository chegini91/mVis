package com.github.TKnudsen.DMandML.model.transformations.dimensionalityReduction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVectorFactory;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVectorTools;
import com.github.TKnudsen.ComplexDataObject.data.keyValueObject.KeyValueObject;

/**
 * Methods to create {@link FLD} instances 
 */
public class FLDs {
	
	/**
	 * Create an FLD from the given list of {@link NumericalFeatureVector} objects
	 * 
	 * @param numericalFeatureVectors The {@link NumericalFeatureVector} objects
	 * @param classAttributeName The name of the attribute that will be assumed to 
	 * contain class information
	 * @param outputDimensionality The output dimensionality
	 * @return The {@link FLD}
	 */
	public static FLD compute(List<NumericalFeatureVector> numericalFeatureVectors, String classAttributeName,
			int outputDimensionality) {
		double[][] x = NumericalFeatureVectorTools.createMatrixRepresentation(numericalFeatureVectors);
		int y[] = calculateClasses(numericalFeatureVectors, classAttributeName);

		smile.classification.FLD fld = new smile.classification.FLD(x, y, outputDimensionality);

		Map<NumericalFeatureVector, NumericalFeatureVector> highToLow = new LinkedHashMap<NumericalFeatureVector, NumericalFeatureVector>();
		for (NumericalFeatureVector high : numericalFeatureVectors) {
			double[] xi = high.getVectorClone();
			double[] yi = fld.project(xi);
			NumericalFeatureVector low = NumericalFeatureVectorFactory.createNumericalFeatureVector(yi, high.getName(),
					high.getDescription() + " (projected with FLD)");
			low.add(classAttributeName, high.getAttribute(classAttributeName));
			highToLow.put(high, low);
		}
		return new FLD(outputDimensionality, highToLow);
	}
		
	/**
	 * Create an array that, for each element of the given list, contains
	 * an integer representing the class of this object. The integer values
	 * are consecutive numbers, one for each distinct value that is obtained
	 * from the {@link KeyValueObject} instances by looking up the attribute
	 * value using the given class attribute name
	 * 
	 * @param keyValueObjects The {@link KeyValueObject} instances
	 * @param classAttributeName The class attribute name
	 * @return The classes
	 */
	private static int[] calculateClasses(List<? extends KeyValueObject<?>> keyValueObjects, String classAttributeName) {
		List<Object> attributeValues = extractAttributeValues(keyValueObjects, classAttributeName);
		Map<Object, Integer> classification = classify(attributeValues);
		return attributeValues.stream().map(v -> classification.get(v)).mapToInt(Integer::intValue).toArray();
	}
	
	/**
	 * Extract the attribute with the given name from the given collection of
	 * {@link KeyValueObject} instances
	 * 
	 * @param keyValueObjects The {@link KeyValueObject} instances
	 * @param attributeName The attribute name
	 * @return The attribute values
	 */
	private static List<Object> extractAttributeValues(
			Collection<? extends KeyValueObject<?>> keyValueObjects, String attributeName) {
		List<Object> attributeValues = new ArrayList<Object>();
		for (KeyValueObject<?> keyValueObject : keyValueObjects) {
			Object attributeValue = keyValueObject.getAttribute(attributeName);
			attributeValues.add(attributeValue);
		}
		return attributeValues;
	}

	/**
	 * Computes a classification of the given objects. Each object will be
	 * assigned one class (integer value) when it is encountered the first time,
	 * causing consecutive integer values to be assigned to the distinct objects
	 * 
	 * @param collection The input collection
	 * @return The classification
	 */
	private static <T> Map<T, Integer> classify(Collection<T> collection) {
		Map<T, Integer> classification = new LinkedHashMap<T, Integer>();
		for (T t : collection) {
			classification.computeIfAbsent(t, c -> classification.size());
		}
		return classification;
	}
	
	private static void testClassify() {
		List<String> list = Arrays.asList("A", "B", "A", null, "C", "B", null, "A");
		Map<String, Integer> classification = classify(list);
		System.out.println(classification);
	}
	
	private int[] calculateY_OLD(List<? extends NumericalFeatureVector> fvs, String classAttribute) {
		if (fvs == null)
			throw new NullPointerException("LDA: feature vectors must not be null");

		if (fvs.size() == 0)
			throw new IllegalArgumentException("LDA: feature vectors size was 0");

		Set<String> values = new HashSet<>();
		for (int i = 0; i < fvs.size(); i++)
			if (fvs.get(i).getAttribute(classAttribute) != null)
				values.add(fvs.get(i).getAttribute(classAttribute).toString());

		Map<String, Integer> classMapping = new HashMap();
		int index = 0;
		for (String s : values)
			classMapping.put(s, new Integer(index++));

		int[] y = new int[fvs.size()];
		for (int i = 0; i < fvs.size(); i++)
			if (fvs.get(i).getAttribute(classAttribute) != null)
				y[i] = classMapping.get(fvs.get(i).getAttribute(classAttribute).toString()).intValue();

		return y;
	}
	
}
