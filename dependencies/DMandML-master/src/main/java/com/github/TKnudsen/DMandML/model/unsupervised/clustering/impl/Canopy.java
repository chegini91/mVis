package com.github.TKnudsen.DMandML.model.unsupervised.clustering.impl;

import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.DMandML.model.unsupervised.clustering.WekaClusteringAlgorithm;

/**
 * <p>
 * Title: Canopy
 * </p>
 * 
 * <p>
 * Description: Implementation is based on WEKAs Canopy.
 * 
 * http://weka.sourceforge.net/doc.dev/weka/clusterers/Canopy.html:
 * 
 * Cluster data using the capopy clustering algorithm, which requires just one
 * pass over the data. Can run in eitherbatch or incremental mode. Results are
 * generally not as good when running incrementally as the min/max for each
 * numeric attribute is not known in advance. Has a heuristic (based on
 * attribute std. deviations), that can be used in batch mode, for setting the
 * T2 distance. The T2 distance determines how many canopies (clusters) are
 * formed. When the user specifies a specific number (N) of clusters to
 * generate, the algorithm will return the top N canopies (as determined by T2
 * density) when N < number of canopies (this applies to both batch and
 * incremental learning); when N > number of canopies, the difference is made up
 * by selecting training instances randomly (this can only be done when batch
 * training). For more information see:
 * 
 * A. McCallum, K. Nigam, L.H. Ungar: Efficient Clustering of High Dimensional
 * Data Sets with Application to Reference Matching. In: Proceedings of the
 * sixth ACM SIGKDD internation conference on knowledge discovery and data
 * mining ACM-SIAM symposium on Discrete algorithms, 169-178, 2000.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2017 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.02
 */
public class Canopy extends WekaClusteringAlgorithm {

	private int k;

	private int maxCandidates;

	private int minDensity;

	private int seed;

	protected Canopy() {
		this(3);
	}

	public Canopy(int k) {
		this(k, 100, 2, 1);
	}

	public Canopy(int k, List<? extends NumericalFeatureVector> featureVectors) {
		this(k, 100, 2, 1, featureVectors);
	}

	public Canopy(int k, int maxCandidates, int minDensity, int seed) {
		setK(k);
		setMaxCandidates(maxCandidates);
		setMinDensity(minDensity);
		setSeed(seed);
	}

	public Canopy(int k, int maxCandidates, int minDensity, int seed,
			List<? extends NumericalFeatureVector> featureVectors) {
		this(k, maxCandidates, minDensity, seed);

		setFeatureVectors(featureVectors);
	}

	@Override
	public String getName() {
		return "Canopy";
	}

	@Override
	public String getDescription() {
		return getName();
	}

	@Override
	protected void initializeClusteringAlgorithm() {
		// Valid options are:
		//
		// -N <num>
		// Number of clusters.
		// (default 2).
		//
		// -max-candidates <num>
		// Maximum number of candidate canopies to retain in memory
		// at any one time. T2 distance plus, data characteristics,
		// will determine how many candidate canopies are formed before
		// periodic and final pruning are performed, which might result
		// in exceess memory consumption. This setting avoids large numbers
		// of candidate canopies consuming memory. (default = 100)
		//
		// -periodic-pruning <num>
		// How often to prune low density canopies.
		// (default = every 10,000 training instances)
		//
		// -min-density
		// Minimum canopy density, below which a canopy will be pruned
		// during periodic pruning. (default = 2 instances)
		//
		// -t2
		// The T2 distance to use. Values < 0 indicate that
		// a heuristic based on attribute std. deviation should be used to set
		// this.
		// Note that this heuristic can only be used when batch training
		// (default = -1.0)
		//
		// -t1
		// The T1 distance to use. A value < 0 is taken as a
		// positive multiplier for T2. (default = -1.5)
		//
		// -M
		// Don't replace missing values with mean/mode when running in batch
		// mode.
		//
		//
		// -S <num>
		// Random number seed.
		// (default 1)
		//
		// -output-debug-info
		// If set, clusterer is run in debug mode and
		// may output additional info to the console
		//
		// -do-not-check-capabilities
		// If set, clusterer capabilities are not checked before clusterer is
		// built
		// (use with caution).

		wekaClusterer = new weka.clusterers.Canopy();

		String[] options = new String[8];
		options[0] = "-N";
		options[1] = "" + getK();
		options[2] = "-S";
		options[3] = "" + getSeed();
		options[4] = "-max-candidates";
		options[5] = "" + getMaxCandidates();
		options[6] = "-min-density";
		options[7] = "" + getMinDensity();

		try {
			wekaClusterer.setOptions(options);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
	}

	public int getMaxCandidates() {
		return maxCandidates;
	}

	public void setMaxCandidates(int maxCandidates) {
		this.maxCandidates = maxCandidates;
	}

	public int getMinDensity() {
		return minDensity;
	}

	public void setMinDensity(int minDensity) {
		this.minDensity = minDensity;
	}

	public int getSeed() {
		return seed;
	}

	public void setSeed(int seed) {
		this.seed = seed;
	}

}
