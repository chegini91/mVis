package com.github.TKnudsen.DMandML.model.transformations.dimensionalityReduction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeature;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject.DataTransformationCategory;
import com.github.TKnudsen.ComplexDataObject.model.tools.WekaConversion;
import com.github.TKnudsen.ComplexDataObject.model.transformations.dimensionalityReduction.DimensionalityReduction;

import weka.attributeSelection.PrincipalComponents;
import weka.core.Instance;
import weka.core.Instances;

/**
 * <p>
 * Title: PrincipalComponentAnalysis
 * </p>
 * 
 * <p>
 * Description: Principal component analysis using WEKA's PrincipalComponents
 * algorithm. Default parameters:
 * 
 * -D Don't normalize input data.
 * 
 * -R Retain enough PC attributes to account for this proportion of variance in
 * the original data. (default = 0.95)
 * 
 * -O Transform through the PC space and back to the original space.
 * 
 * -A Maximum number of attributes to include in transformed attribute names.
 * (-1 = include all)
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.03
 */
public class PCA extends DimensionalityReduction<NumericalFeatureVector> {

	/**
	 * whether or not the PCA model will normalize the data at start
	 */
	private boolean normalize;

	/**
	 * parameter that can be used as a criterion of convergence. Principal
	 * components are built until the minimum remaining variance is achieved.
	 * Example: let's say 60% of the variance shall be preserved for a given data
	 * set. then d principal components are needed to achieve at least 60% variance
	 * preservation.
	 */
	private double minimumRemainingVariance;

	/**
	 * whether or not feature are 'back-projected' into the original space.
	 */
	private boolean transformThroughPCASpaceBackToOriginalSpace = false;

	private List<NumericalFeatureVector> featureVectors;

	public PCA(List<NumericalFeatureVector> featureVectors, int outputDimensionality) {
		this(featureVectors, true, Double.NaN, outputDimensionality);
	}

	public PCA(List<NumericalFeatureVector> featureVectors, boolean normalize, int outputDimensionality) {
		this(featureVectors, normalize, Double.NaN, outputDimensionality);
	}

	public PCA(List<NumericalFeatureVector> featureVectors, boolean normalize, double minimumRemainingVariance,
			int outputDimensionality) {
		this.featureVectors = featureVectors;
		this.normalize = normalize;
		this.minimumRemainingVariance = minimumRemainingVariance;
		this.outputDimensionality = outputDimensionality;
	}

	private weka.attributeSelection.PrincipalComponents pca;

	private void initPCA() {
		List<String> parameters = new ArrayList<>();

		if (!normalize)
			parameters.add("-D");

		if (!Double.isNaN(minimumRemainingVariance) && minimumRemainingVariance > 0.0
				&& minimumRemainingVariance <= 1.0) {
			parameters.add("-R");
			parameters.add("" + minimumRemainingVariance);
		}

		if (transformThroughPCASpaceBackToOriginalSpace)
			parameters.add("-O");

		if (outputDimensionality > 0) {
			parameters.add("-A");
			parameters.add("" + outputDimensionality);
		}

		String[] options = new String[parameters.size()];
		options = parameters.toArray(options);

		pca = new PrincipalComponents();
		try {
			pca.setOptions(options);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * provides a low-dimensional representation of X
	 */
	@Override
	public List<NumericalFeatureVector> transform(List<NumericalFeatureVector> inputObjects) {
		if (mapping == null || pca == null)
			throw new NullPointerException("PCA: model not calculated yet.");

		List<NumericalFeatureVector> output = new ArrayList<>();
		for (NumericalFeatureVector fv : inputObjects)
			if (mapping.containsKey(fv))
				output.add(mapping.get(fv));
			else {
				Instances instances = WekaConversion
						.getInstances(new ArrayList<NumericalFeatureVector>(Arrays.asList(fv)), false);
				Iterator<Instance> iterator = instances.iterator();
				if (iterator.hasNext()) {
					try {
						Instance transformed = pca.convertInstance(iterator.next());
						NumericalFeatureVector outputFeatureVector = createNumericalFeatureVector(transformed);

						outputFeatureVector.setMaster(fv);
						output.add(outputFeatureVector);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		return output;
	}

	private NumericalFeatureVector createNumericalFeatureVector(Instance transformed) {
		double[] values = transformed.toDoubleArray();

		List<NumericalFeature> features = new ArrayList<>();
		for (int d = 0; d < Math.min(values.length, outputDimensionality); d++)
			features.add(new NumericalFeature("Dim " + d, values[d]));

		while (features.size() < outputDimensionality)
			features.add(new NumericalFeature("Dim_" + features.size(), 0.0));

		NumericalFeatureVector outputFeatureVector = new NumericalFeatureVector(features);
		return outputFeatureVector;
	}

	@Override
	public DataTransformationCategory getDataTransformationCategory() {
		return DataTransformationCategory.DIMENSION_REDUCTION;
	}

	public double getMinimumRemainingVariance() {
		return minimumRemainingVariance;
	}

	public void setMinimumRemainingVariance(double minimumRemainingVariance) {
		this.minimumRemainingVariance = minimumRemainingVariance;
	}

	public boolean isNormalize() {
		return normalize;
	}

	public void setNormalize(boolean normalize) {
		this.normalize = normalize;
	}

	public boolean isTransformThroughPCASpaceBackToOriginalSpace() {
		return transformThroughPCASpaceBackToOriginalSpace;
	}

	public void setTransformThroughPCASpaceBackToOriginalSpace(boolean transformThroughPCASpaceBackToOriginalSpace) {
		this.transformThroughPCASpaceBackToOriginalSpace = transformThroughPCASpaceBackToOriginalSpace;
	}

	@Override
	public void calculateDimensionalityReduction() {
		if (featureVectors == null)
			throw new NullPointerException("PCA: feature vectors null");

		initPCA();

		mapping = new HashMap<>();

		Instances instances = WekaConversion.getInstances(featureVectors, false);
		try {
			pca.buildEvaluator(instances);
			Instances transformedData = pca.transformedData(instances);

			// build new feature vectors
			List<NumericalFeatureVector> returnFVs = new ArrayList<>();
			for (int i = 0; i < transformedData.size(); i++) {
				Instance transformed = transformedData.get(i);

				NumericalFeatureVector outputFeatureVector = createNumericalFeatureVector(transformed);

				outputFeatureVector.setMaster(featureVectors.get(i));
				returnFVs.add(outputFeatureVector);

				mapping.put(featureVectors.get(i), outputFeatureVector);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
