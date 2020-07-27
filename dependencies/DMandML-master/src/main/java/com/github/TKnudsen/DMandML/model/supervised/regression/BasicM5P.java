package com.github.TKnudsen.DMandML.model.supervised.regression;

import java.util.ArrayList;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.IFeatureVectorObject;

/**
 * <p>
 * Title: M5Base
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * @author Christian Ritter
 * @version 1.03
 * 
 */
public class BasicM5P<FV extends IFeatureVectorObject<?, ?>> extends WekaRegressionWrapper<FV> {

	private int minimumNumberOfInstances = 4;
	private boolean safeInstances = false;
	private boolean useUnprunedTree = false;
	private boolean useUnsmoothedPredictions = false;

	@Override
	public String getDescription() {
		return "WEKA's implementation of the M5P regression algorithm.";
	}

	/**
	 * @return the minimumNumberOfInstances
	 */
	public int getMinimumNumberOfInstances() {
		return minimumNumberOfInstances;
	}

	@Override
	public String getName() {
		return "M5P";
	}

	/**
	 * @return the safeInstances
	 */
	public boolean isSafeInstances() {
		return safeInstances;
	}

	/**
	 * @return the useUnprunedTree
	 */
	public boolean isUseUnprunedTree() {
		return useUnprunedTree;
	}

	/**
	 * @return the useUnsmoothedPredictions
	 */
	public boolean isUseUnsmoothedPredictions() {
		return useUnsmoothedPredictions;
	}

	/**
	 * @param minimumNumberOfInstances
	 *            the minimumNumberOfInstances to set
	 */
	public void setMinimumNumberOfInstances(int minimumNumberOfInstances) {
		this.minimumNumberOfInstances = minimumNumberOfInstances;
	}

	/**
	 * @param safeInstances
	 *            the safeInstances to set
	 */
	public void setSafeInstances(boolean safeInstances) {
		this.safeInstances = safeInstances;
	}

	/**
	 * @param useUnprunedTree
	 *            the useUnprunedTree to set
	 */
	public void setUseUnprunedTree(boolean useUnprunedTree) {
		this.useUnprunedTree = useUnprunedTree;
	}

	/**
	 * @param useUnsmoothedPredictions
	 *            the useUnsmoothedPredictions to set
	 */
	public void setUseUnsmoothedPredictions(boolean useUnsmoothedPredictions) {
		this.useUnsmoothedPredictions = useUnsmoothedPredictions;
	}

	@Override
	protected void initializeRegression() {
		wekaRegressionModel = new weka.classifiers.trees.M5P();

		List<String> optionsList = new ArrayList<String>();

		if (isUseUnprunedTree())
			optionsList.add("-N"); // Use unpruned tree/rules

		if (isUseUnsmoothedPredictions())
			optionsList.add("-U"); // Use unsmoothed predictions

		optionsList.add("-R"); // Build regression tree/rule rather than a model tree/rule

		optionsList.add("-M"); // Set minimum number of instances per leaf (default 4)
		optionsList.add(String.valueOf(getMinimumNumberOfInstances()));

		if (isSafeInstances())
			optionsList.add("-L"); // Save instances at the nodes in the tree (for visualization purposes)

		String[] options = optionsList.toArray(new String[optionsList.size()]);

		try {
			wekaRegressionModel.setOptions(options);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
