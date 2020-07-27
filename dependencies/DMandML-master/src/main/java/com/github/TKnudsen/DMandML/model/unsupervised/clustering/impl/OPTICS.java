package com.github.TKnudsen.DMandML.model.unsupervised.clustering.impl;

import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.DMandML.model.unsupervised.clustering.WekaClusteringAlgorithm;

/**
 * <p>
 * Title: OPTICS
 * </p>
 * 
 * <p>
 * Description: implementation is based on WEKAs OPTICS.
 * 
 * Mihael Ankerst and Markus M. Breunig and Hans-Peter Kriegel and Joerg
 * Sander}, booktitle = {ACM SIGMOD International Conference on Management of
 * Data}, pages = {49-60}, publisher = {ACM Press}, title = {OPTICS: Ordering
 * Points To Identify the Clustering Structure}, year = {1999}
 * </p>
 * 
 * <p>
 * Copyright: (c) 2017-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.03
 * 
 * @deprecated The Weka OPTICS and DBScan implementations are essentially buggy
 * and should not be used. See https://list.waikato.ac.nz/pipermail/wekalist/2010-December/050518.html
 */
public class OPTICS extends WekaClusteringAlgorithm {

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

	protected OPTICS() {
		this(0.15, 6);
	}

	public OPTICS(double epsilon, int minPoints) {
		setEpsilonRelative(epsilon);
		setMinPoints(minPoints);
	}

	public OPTICS(double epsilon, int minPoints, List<? extends NumericalFeatureVector> featureVectors) {
		this(epsilon, minPoints);

		setFeatureVectors(featureVectors);
	}

	@Override
	public String getName() {
		return "OPTICS";
	}

	@Override
	public String getDescription() {
		return getName();
	}

	@Override
	protected void initializeClusteringAlgorithm() {
		// -E <double>
		// epsilon (default = 0.9)
		//
		// -M <int>
		// minPoints (default = 6)
		//
		// -I <String>
		// index (database) used for OPTICS (default =
		// weka.clusterers.forOPTICSAndDBScan.Databases.SequentialDatabase)
		//
		// -D <String>
		// distance-type (default =
		// weka.clusterers.forOPTICSAndDBScan.DataObjects.EuclideanDataObject)
		//
		// -F
		// write results to OPTICS_#TimeStamp#.TXT - File
		//
		// -no-gui
		// suppress the display of the GUI after building the clusterer
		//
		// -db-output <file>
		// The file to save the generated database to. If a directory
		// is provided, the database doesn't get saved.
		// The generated file can be viewed with the OPTICS Visualizer:
		// java weka.clusterers.forOPTICSAndDBScan.OPTICS_GUI.OPTICS_Visualizer
		// [file.ser]
		// (default: .)

		wekaClusterer = new weka.clusterers.OPTICS();

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
