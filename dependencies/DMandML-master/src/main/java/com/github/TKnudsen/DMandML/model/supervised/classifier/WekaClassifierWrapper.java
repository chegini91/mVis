package com.github.TKnudsen.DMandML.model.supervised.classifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.DoubleStream;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.TKnudsen.ComplexDataObject.data.interfaces.IFeatureVectorObject;
import com.github.TKnudsen.ComplexDataObject.model.tools.WekaConversion;

import weka.core.Instance;
import weka.core.Instances;

/**
 * <p>
 * Title: WekaClassifierWrapper
 * </p>
 * 
 * <p>
 * Description: Abstract baseline class for wrapping WEKA classifiers. Thus,
 * inherited WEKA classifiers will only require a few lines of code.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.06
 * 
 */
public abstract class WekaClassifierWrapper<FV extends IFeatureVectorObject<?, ?>> extends Classifier<FV> {

	/**
	 * The attribute that will be used as the class attribute for the Weka
	 * instances.
	 */
	private static final String CLASS_ATTRIBUTE_NAME = "class";

	/**
	 * The delegate classifier
	 */
	private weka.classifiers.Classifier wekaClassifier;

	/**
	 * Whether a basic validation of the results that are returned by the Weka
	 * classifier should be performed
	 */
	private final boolean validateWekaResults = true;

	/**
	 * Whether {@link #initializeClassifier()} was already called
	 */
	private boolean initialized = false;

	/**
	 * Whether the previous call to {@link #buildClassifier(Instances)} was
	 * successful
	 */
	private boolean trainedSuccessfully = false;

	@JsonIgnore
	private final Map<FV, LinkedHashMap<String, Double>> labelDistributionMap;

	public WekaClassifierWrapper() {
		super(CLASS_ATTRIBUTE_NAME);
		this.labelDistributionMap = new LinkedHashMap<>();
	}

	/**
	 * Make sure that {@link #initializeClassifier()} was called
	 */
	private void ensureInitialized() {
		if (!initialized) {
			initializeClassifier();
			initialized = true;
		}

	}

	/**
	 * Initialize the classifier. This will be called once, and should be
	 * implemented by subclasses so that the {@link #wekaClassifier} is assigned and
	 * initialized
	 */
	protected abstract void initializeClassifier();

	/**
	 * Train this classifier, assigning the given weights to the feature vectors
	 * 
	 * @param featureVectors
	 *            The feature vectors
	 * @param weights
	 *            The weights
	 * @throws NullPointerException
	 *             The either argument is <code>null</code>
	 * @throws IllegalArgumentException
	 *             If the size of the list is different to the array length
	 */
	public void trainWithWeights(List<FV> featureVectors, double[] weights) {
		Objects.requireNonNull(featureVectors, "The featureVectors may not be null");
		Objects.requireNonNull(weights, "The weights may not be null");
		if (featureVectors.size() != weights.length) {
			throw new IllegalArgumentException(
					"There are " + featureVectors.size() + " feature vectors but " + weights.length + " weights");
		}

		updateLabelAlphabet(featureVectors);
		buildClassifier(featureVectors, weights);
	}

	/**
	 * Convert the given feature vectors to Weka instances, and assign the given
	 * weights to them by calling {@link Instance#setWeight(double)}
	 * 
	 * @param featureVectors
	 *            The feature vectors
	 * @param weights
	 *            The weights
	 */
	private void buildClassifier(List<FV> featureVectors, double weights[]) {
		ensureInitialized();
		Instances trainData = WekaConversion.getLabeledInstances(featureVectors, getClassAttribute(), true);
		for (int i = 0; i < trainData.size(); i++) {
			trainData.get(i).setWeight(weights[i]);
		}
		buildClassifier(trainData);
	}

	@Override
	public Map<String, Double> getLabelDistribution(FV featureVector) {

		if (!trainedSuccessfully) {
			List<String> labelAlphabet = getLabelAlphabet();
			if (labelAlphabet.size() == 1) {
				System.err.println(
						"WekaClassifierWrapper: Training data contained only a single class. Returning single-element label distribution");
				return Collections.singletonMap(labelAlphabet.get(0), 1.0);
			}
			System.err.println(
					"WekaClassifierWrapper: No successful training was performed. Returning empty label distribution");
			return Collections.emptyMap();
		}

		if (labelDistributionMap.get(featureVector) == null) {
			computeLabelDistributions(Collections.singletonList(featureVector));
		}
		return labelDistributionMap.get(featureVector);
	}

	@Override
	public final List<String> test(List<FV> featureVectors) {
		Objects.requireNonNull(featureVectors, "The featureVectors may not be null");

		if (!trainedSuccessfully) {
			List<String> labelAlphabet = getLabelAlphabet();
			if (labelAlphabet.size() == 1) {
				System.err.println(
						"WekaClassifierWrapper: Training data contained only a single class. Returning all-equal winning labels");
				return Collections.nCopies(featureVectors.size(), labelAlphabet.get(0));
			}
			System.err.println(
					"WekaClassifierWrapper: No successful training was performed. Returning 'null' as winning labels");
			return Collections.nCopies(featureVectors.size(), null);
		}

		Set<FV> featureVectorsSet = new LinkedHashSet<>(featureVectors);
		featureVectorsSet.removeAll(labelDistributionMap.keySet());
		List<FV> newFeatureVectors = new ArrayList<>(featureVectorsSet);
		computeLabelDistributions(newFeatureVectors);

		List<String> labels = new ArrayList<String>();
		for (FV fv : featureVectors) {
			Map<String, Double> labelDistribution = getLabelDistribution(fv);

			if (labelDistribution == null || labelDistribution.isEmpty())
				throw new NullPointerException(
						getName() + ": test(List<FV>) not successful with gathering label for an instance");

			Entry<String, Double> entryWithHighestProbability = Collections.max(labelDistribution.entrySet(),
					Map.Entry.comparingByValue());
			String label = entryWithHighestProbability.getKey();
			labels.add(label);
		}
		return labels;
	}

	private void computeLabelDistributions(List<FV> featureVectors) {

		if (featureVectors.isEmpty()) {
			// Nothing to do here. WekaConversion.getInstances
			// would return "null" for an empty list, anyhow...
			return;
		}
		if (!trainedSuccessfully) {
			// This should never happen. The methods that call this method should
			// check this case before trying to compute a label distribution
			System.err.println(
					"WekaClassifierWrapper: Trying to compute label distributions without successful training");
			return;
		}

		Instances testData = WekaConversion.getInstances(featureVectors, false);

		// TODO This should never happen! Why COULD it happen?
		// (In doubt, WekaConversion.getInstances should throw!)
		if (testData.size() != featureVectors.size()) {
			throw new IllegalArgumentException("WekaConversion failed");
		}

		List<String> labelAlphabet = getLabelAlphabet();
		WekaConversion.addLabelAttributeToInstance(testData, CLASS_ATTRIBUTE_NAME, labelAlphabet);

		for (int i = 0; i < testData.numInstances(); i++) {
			FV featureVector = featureVectors.get(i);
			Instance instance = testData.instance(i);

			double[] distribution = comuteWekaDistribution(featureVector, instance);

			LinkedHashMap<String, Double> labelDistribution = new LinkedHashMap<String, Double>();
			for (int j = 0; j < distribution.length; j++) {
				String label = labelAlphabet.get(j);
				labelDistribution.put(label, distribution[j]);
			}
			labelDistributionMap.put(featureVectors.get(i), labelDistribution);
		}

	}

	private double[] comuteWekaDistribution(FV featureVector, Instance instance) {
		try {
			double[] distribution = wekaClassifier.distributionForInstance(instance);
			if (validateWekaResults) {
				validateDistribution(distribution, featureVector, instance);
			}
			return distribution;
		} catch (Exception e) {

			// This should never happen. Something odd must be going on in
			// the weka classifier.
			System.err.println("WekaClassifierWrapper: Weka classifier could not classify instance, using NaN results");
			printDetailedWekaDebugInfo(instance, e);

			double distribution[] = new double[getLabelAlphabet().size()];
			Arrays.fill(distribution, Double.NaN);
			return distribution;
		}
	}

	@Override
	protected void buildClassifier(List<FV> featureVectors) {
		Instances trainData = WekaConversion.getLabeledInstances(featureVectors, getClassAttribute(), true);
		buildClassifier(trainData);
	}

	private void buildClassifier(Instances trainData) {
		ensureInitialized();
		if (trainData.size() < 2) {
			throw new IllegalArgumentException("At least two training instances required");
		}
		labelDistributionMap.clear();
		trainedSuccessfully = false;

		if (trainData.classAttribute().numValues() == 1) {
			System.err.println("Training data contains only a single class. Not applying " + wekaClassifier);
		} else {
			try {
				wekaClassifier.buildClassifier(trainData);
				trainedSuccessfully = true;
			} catch (Exception e) {
				System.err.println("AbstractWekaClassifier: inherited classifier ->" + getName()
						+ "<- sent an exception: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	// This is public ONLY for serialization
	public final weka.classifiers.Classifier getWekaClassifier() {
		return wekaClassifier;
	}

	// This is public ONLY for serialization. Usually, this should
	// ONLY be called by subclasses, in #initializeClassifier !!!
	public final void setWekaClassifier(weka.classifiers.Classifier wekaClassifier) {
		this.wekaClassifier = wekaClassifier;
	}

	// ========================================================================
	// Debugging and validation methods

	/**
	 * Make sure that the given distribution sums up to approximately 1.0, and print
	 * an error message otherwise
	 * 
	 * @param distribution
	 *            The distribution
	 * @param featureVector
	 *            The feature vector
	 * @param instance
	 *            The weka instance
	 */
	private void validateDistribution(double[] distribution, FV featureVector, Instance instance) {
		double sum = DoubleStream.of(distribution).sum();
		if (Math.abs(1.0 - sum) > 1e-6) {
			System.err.println("WekaClassifierWrapper with " + wekaClassifier + ": Probability sum is " + sum
					+ ", maybe unclassified? fv is " + featureVector + ", instance " + instance);
		}
	}

	/**
	 * Print detailed debug info about a failed weka classification
	 * 
	 * @param instance
	 *            The instance
	 * @param e
	 *            The exception from weka
	 */
	private void printDetailedWekaDebugInfo(Instance instance, Throwable e) {
		System.out.println("Weka classifier could not classify instance: " + e.getMessage());
		System.out.println("  Classifier: " + wekaClassifier);
		System.out.println("  Instance  : " + instance);
		List<String> labelAlphabet = getLabelAlphabet();
		System.out.println("  Label alphabet " + labelAlphabet);
		System.out.println("  Instance class attribute values:");
		for (int j = 0; j < labelAlphabet.size(); j++) {
			System.out.println("    " + instance.dataset().classAttribute().value(j));
		}
		System.out.println("  Using NaN results");
	}

}
