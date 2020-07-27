package com.github.TKnudsen.DMandML.model.unsupervised.clustering.impl;

import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.DMandML.model.unsupervised.clustering.WekaClusteringAlgorithm;

/**
 * <p>
 * Title: Cobweb
 * </p>
 * 
 * <p>
 * Description: incremental system for hierarchical conceptual clustering.
 * Implementation is based on WEKAs Cobweb.
 * 
 * D. Fisher (1987). Knowledge acquisition via incremental conceptual
 * clustering. Machine Learning. 2(2):139-172.
 * 
 * J. H. Gennari, P. Langley, D. Fisher (1990). Models of incremental concept
 * formation. Artificial Intelligence. 40:11-61.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2017 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class Cobweb extends WekaClusteringAlgorithm {

	private double acuity;

	private double cutoff;

	private int seed;

	protected Cobweb() {
		this(1.0, 0.002);
	}

	public Cobweb(double acuity, double cutoff) {
		setAcuity(acuity);
		setCutoff(cutoff);
		setSeed(17);
	}

	public Cobweb(double acuity, double cutoff, List<? extends NumericalFeatureVector> featureVectors) {
		this(acuity, cutoff);

		setFeatureVectors(featureVectors);
	}

	public Cobweb(double acuity, double cutoff, int seed) {
		setAcuity(acuity);
		setCutoff(cutoff);
		setSeed(seed);
	}

	@Override
	public String getName() {
		return "Cobweb";
	}

	@Override
	public String getDescription() {
		return getName();
	}

	@Override
	protected void initializeClusteringAlgorithm() {
		// Valid options are:
		//
		// -A <acuity>
		// Acuity.
		// (default=1.0) 0-100
		//
		// -C <cutoff>
		// Cutoff.
		// (default=0.002) 0-100
		//
		// -save-data
		// Save instance data.
		//
		// -S <num>
		// Random number seed.
		// (default 42)
		//
		// -output-debug-info
		// If set, clusterer is run in debug mode and
		// may output additional info to the console
		//
		// -do-not-check-capabilities
		// If set, clusterer capabilities are not checked before clusterer is
		// built
		// (use with caution).

		wekaClusterer = new weka.clusterers.Cobweb();

		String[] options = new String[6];
		options[0] = "-A";
		options[1] = "" + getAcuity();
		options[2] = "-C";
		options[3] = "" + getCutoff();
		options[4] = "-S";
		options[5] = "" + getSeed();

		try {
			wekaClusterer.setOptions(options);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public double getAcuity() {
		return acuity;
	}

	public void setAcuity(double acuity) {
		this.acuity = acuity;
	}

	public double getCutoff() {
		return cutoff;
	}

	public void setCutoff(double cutoff) {
		this.cutoff = cutoff;
	}

	public int getSeed() {
		return seed;
	}

	public void setSeed(int seed) {
		this.seed = seed;
	}

}
