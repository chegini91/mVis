package com.github.TKnudsen.DMandML.model.supervised.regression;

import java.util.ArrayList;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.IFeatureVectorObject;

/**
 * <p>
 * Title: BasicREPTree
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * @author Juergen Bernard, Christian Ritter
 * @version 1.02
 */
public class BasicREPTree<FV extends IFeatureVectorObject<?, ?>> extends WekaRegressionWrapper<FV> {

	private int minimumInstancesPerLeaf = 2;

	private double minimumNumericVarianceProportionForSplit = 1e-3;

	private int foldCount = 3;

	private int seed = 1;

	private boolean prune = true;

	private int maximumTreeDepth = -1;

	@Override
	protected void initializeRegression() {

		wekaRegressionModel = new weka.classifiers.trees.REPTree();

		List<String> optionsList = new ArrayList<String>();

		// Valid options are:
		//
		optionsList.add("-M"); // <minimum number of instances>
		optionsList.add(minimumInstancesPerLeaf + "");
		// Set minimum number of instances per leaf (default 2).
		//
		//
		optionsList.add("-V"); // <minimum variance for split>
		optionsList.add(minimumNumericVarianceProportionForSplit + "");
		// Set minimum numeric class variance proportion
		// of train variance for split (default 1e-3).
		//
		//
		optionsList.add("-N"); // <number of folds>
		optionsList.add(foldCount + "");
		// Number of folds for reduced error pruning (default 3).
		//
		//
		optionsList.add("-S"); // <seed>
		optionsList.add(seed + "");
		// Seed for random data shuffling (default 1).
		//
		//
		if (!prune)
			optionsList.add("-P");
		// -P
		// No pruning.
		//
		//
		optionsList.add("-L"); // -L
		optionsList.add(maximumTreeDepth + "");
		// Maximum tree depth (default -1, no maximum)

		String[] options = optionsList.toArray(new String[optionsList.size()]);

		try {
			wekaRegressionModel.setOptions(options);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getName() {
		return "REPTree";
	}

	@Override
	public String getDescription() {
		return "WEKA's implementation of the REPTree regression algorithm.";
	}

	public int getMinimumInstancesPerLeaf() {
		return minimumInstancesPerLeaf;
	}

	public void setMinimumInstancesPerLeaf(int minimumInstancesPerLeaf) {
		this.minimumInstancesPerLeaf = minimumInstancesPerLeaf;

		resetResults();
	}

	public double getMinimumNumericVarianceProportionForSplit() {
		return minimumNumericVarianceProportionForSplit;
	}

	public void setMinimumNumericVarianceProportionForSplit(double minimumNumericVarianceProportionForSplit) {
		this.minimumNumericVarianceProportionForSplit = minimumNumericVarianceProportionForSplit;

		resetResults();
	}

	public int getFoldCount() {
		return foldCount;
	}

	public void setFoldCount(int foldCount) {
		this.foldCount = foldCount;

		resetResults();
	}

	public int getSeed() {
		return seed;
	}

	public void setSeed(int seed) {
		this.seed = seed;

		resetResults();
	}

	public boolean isPrune() {
		return prune;
	}

	public void setPrune(boolean prune) {
		this.prune = prune;

		resetResults();
	}

	public int getMaximumTreeDepth() {
		return maximumTreeDepth;
	}

	public void setMaximumTreeDepth(int maximumTreeDepth) {
		this.maximumTreeDepth = maximumTreeDepth;

		resetResults();
	}

}
