package com.github.TKnudsen.DMandML.model.unsupervised.clustering.impl;

import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.DMandML.model.unsupervised.clustering.WekaClusteringAlgorithm;
import com.github.TKnudsen.DMandML.model.unsupervised.clustering.enums.LinkageStrategy;

import weka.clusterers.HierarchicalClusterer;
import weka.core.SelectedTag;

/**
 * <p>
 * Title: HierarchicalClustering
 * </p>
 * 
 * <p>
 * Description: implementation is based on WEKAs HierarchicalClustering.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2017-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.04
 */
public class HierarchicalClustering extends WekaClusteringAlgorithm {

	/**
	 * number of cluster to be calculated.
	 */
	private int k;

	/**
	 * linkage strategy of the hierarchical clustering algorithm
	 */
	private LinkageStrategy linkageStrategy;
	private SelectedTag WEKALinkType;

	protected HierarchicalClustering() {
		this(3);
	}

	public HierarchicalClustering(int k) {
		this(k, LinkageStrategy.AverageLinkage);
	}

	public HierarchicalClustering(int k, LinkageStrategy linkageStrategy) {
		this.k = k;
		setLinkageStrategy(linkageStrategy);
	}

	public HierarchicalClustering(int k, List<? extends NumericalFeatureVector> featureVectors) {
		setK(k);

		setFeatureVectors(featureVectors);
	}

	public HierarchicalClustering(int k, LinkageStrategy linkageStrategy,
			List<? extends NumericalFeatureVector> featureVectors) {
		this(k, linkageStrategy);

		setFeatureVectors(featureVectors);
	}

	@Override
	public String getName() {
		return "Hierarchical Clustering";
	}

	@Override
	public String getDescription() {
		return getName();
	}

	@Override
	protected void initializeClusteringAlgorithm() {
		// -N
		// number of clusters
		//
		//
		// -L
		// Link type (Single, Complete, Average, Mean, Centroid, Ward, Adjusted
		// complete, Neighbor Joining)
		// [SINGLE|COMPLETE|AVERAGE|MEAN|CENTROID|WARD|ADJCOMPLETE|NEIGHBOR_JOINING]
		//
		//
		// -A
		// Distance function to use. (default: weka.core.EuclideanDistance)
		//
		//
		// -P
		// Print hierarchy in Newick format, which can be used for display in
		// other programs.
		//
		//
		// -D
		// If set, classifier is run in debug mode and may output additional
		// info to the console.
		//
		//
		// -B
		// \If set, distance is interpreted as branch length, otherwise it is
		// node height.

		wekaClusterer = new weka.clusterers.HierarchicalClusterer();

		String[] options = new String[2];
		options[0] = "-N";
		options[1] = "" + getK();

		try {
			wekaClusterer.setOptions(options);

			weka.clusterers.HierarchicalClusterer clusterer = (HierarchicalClusterer) wekaClusterer;
			clusterer.setLinkType(WEKALinkType);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;

		initializeClusteringAlgorithm();
	}

	public LinkageStrategy getLinkageStrategy() {
		return linkageStrategy;
	}

	public void setLinkageStrategy(LinkageStrategy linkageStrategy) {
		this.linkageStrategy = linkageStrategy;
		this.WEKALinkType = WekaClusteringAlgorithm.converteLinkageStrategy(linkageStrategy);

		initializeClusteringAlgorithm();
	}

}
