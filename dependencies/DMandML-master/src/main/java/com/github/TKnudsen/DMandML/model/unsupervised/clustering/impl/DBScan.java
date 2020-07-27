package com.github.TKnudsen.DMandML.model.unsupervised.clustering.impl;

import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.ComplexDataObject.model.tools.WekaConversion;
import com.github.TKnudsen.DMandML.model.unsupervised.clustering.WekaClusteringAlgorithm;

/**
 * <p>
 * Title: DBScan
 * </p>
 * 
 * <p>
 * Description: implementation is based on WEKAs DBSCAN.
 * 
 * Note: the epsilon parameter is relative not absolute. It is thus not
 * necessary to assess distance values of the applied data set.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2017-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.05
 * 
 * @deprecated The Weka OPTICS and DBScan implementations are essentially buggy
 * and should not be used. See https://list.waikato.ac.nz/pipermail/wekalist/2010-December/050518.html
 */
public class DBScan extends WekaClusteringAlgorithm {

	/**
	 * the relative radius around an instance. WEKA default = 0.9. Best practice:
	 * 0.15
	 */
	private double epsilonRelative;

	/**
	 * the number of minPoints of a potential dense (distances <= epsilon) cluster
	 * structure to be a valid cluster. Default suggestion: n/(k*3).
	 */
	private int minPoints;

	protected DBScan() {
	}

	public DBScan(double epsilon, int minPoints) {
		setEpsilonRelative(epsilon);
		setMinPoints(minPoints);
	}

	public DBScan(List<? extends NumericalFeatureVector> featureVectors, double epsilon, int minPoints) {
		this(epsilon, minPoints);

		setFeatureVectors(featureVectors);
	}

	@Override
	public String getName() {
		return "DBScan";
	}

	@Override
	public String getDescription() {
		return getName();
	}

	@Override
	protected void initializeClusteringAlgorithm() {
		if (data == null)
			data = WekaConversion.getInstances(featureVectors, false);

		wekaClusterer = new weka.clusterers.DBSCAN();
		String[] options = new String[5];
		options[0] = "-E";
		options[1] = "" + getEpsilonRelative();
		options[2] = "-M";
		options[3] = "" + getMinPoints();
		options[4] = "-no-gui";

		try {
			wekaClusterer.setOptions(options);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public double getEpsilonRelative() {
		return epsilonRelative;
	}

	public void setEpsilonRelative(double epsilonRelative) {
		this.epsilonRelative = epsilonRelative;

		initializeClusteringAlgorithm();
	}

	public int getMinPoints() {
		return minPoints;
	}

	public void setMinPoints(int minPoints) {
		this.minPoints = minPoints;

		initializeClusteringAlgorithm();
	}
}
