package com.github.TKnudsen.DMandML.model.unsupervised.clustering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.ComplexDataObject.model.tools.WekaConversion;
import com.github.TKnudsen.DMandML.data.cluster.featureVector.numerical.NumericalFeatureVectorClusterResult;
import com.github.TKnudsen.DMandML.model.tools.WekaClusteringTools;
import com.github.TKnudsen.DMandML.model.unsupervised.clustering.enums.LinkageStrategy;

import weka.clusterers.AbstractClusterer;
import weka.clusterers.HierarchicalClusterer;
import weka.core.Instances;
import weka.core.SelectedTag;

/**
 * <p>
 * Title: We Description: abstract baseline routine for WEKA-base clustering
 * routines.
 * 
 * For more information see: Dan Pelleg, Andrew W. Moore: X-means: Extending
 * K-means with Efficient Estimation of the Number of Clusters. In: Seventeenth
 * International Conference on Machine Learning, 727-734, 2000.
 * 
 * </p>
 * 
 * <p>
 * Copyright: (c) 2017-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.03
 */
public abstract class WekaClusteringAlgorithm implements INumericalClusteringAlgorithm {

	/**
	 * data to be clustered
	 */
	protected List<? extends NumericalFeatureVector> featureVectors;

	/**
	 * WEKA representation of the data
	 */
	protected Instances data;

	/**
	 * the WEKA-based clustering routine
	 */
	protected AbstractClusterer wekaClusterer;

	/**
	 * cluster result that can be used for downstream analysis
	 */
	protected NumericalFeatureVectorClusterResult clusterResult;

	public WekaClusteringAlgorithm() {
	}

	public WekaClusteringAlgorithm(List<? extends NumericalFeatureVector> featureVectors) {
		this.setFeatureVectors(featureVectors);
	}

	protected abstract void initializeClusteringAlgorithm();

	@Override
	public void calculateClustering() {
		initializeClusteringAlgorithm();

		try {
			clusterResult = WekaClusteringTools.getClusterResultFromWekaClusterer(wekaClusterer, data, featureVectors,
					getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public NumericalFeatureVectorClusterResult getClusteringResult() {
		if (clusterResult == null)
			throw new NullPointerException(
					"WekaClusteringAlgorithm: cluster result was null. Was the clustering calculated yet?");

		return clusterResult;
	}

	@Override
	public List<NumericalFeatureVector> getFeatureVectors() {
		return Collections.unmodifiableList(featureVectors);
	}

	@Override
	public void setFeatureVectors(List<? extends NumericalFeatureVector> featureVectors) {
		this.featureVectors = new ArrayList<>(featureVectors);

		if (featureVectors == null)
			data = null;
		else
			data = WekaConversion.getInstances(featureVectors, false);

		clusterResult = null;
	}

	public static SelectedTag converteLinkageStrategy(LinkageStrategy linkageStrategy) {

		switch (linkageStrategy) {
		case SingleLinkage:
			return new SelectedTag(0, HierarchicalClusterer.TAGS_LINK_TYPE);

		case CompleteLinkage:
			return new SelectedTag(1, HierarchicalClusterer.TAGS_LINK_TYPE);

		case AverageLinkage:
			return new SelectedTag(2, HierarchicalClusterer.TAGS_LINK_TYPE);

		case MeanLinkage:
			return new SelectedTag(3, HierarchicalClusterer.TAGS_LINK_TYPE);

		case CentroidLinkage:
			return new SelectedTag(4, HierarchicalClusterer.TAGS_LINK_TYPE);

		case WardLinkage:
			return new SelectedTag(5, HierarchicalClusterer.TAGS_LINK_TYPE);

		case AdjComplete:
			return new SelectedTag(6, HierarchicalClusterer.TAGS_LINK_TYPE);

		case NeighborJoining:
			return new SelectedTag(7, HierarchicalClusterer.TAGS_LINK_TYPE);

		case MedianLinkage:
			throw new IllegalArgumentException(
					"WekaClusteringAlgorithm.converteLinkageStrategy: WEKA does not support median linkage");
		default:
			throw new IllegalArgumentException(
					"WekaClusteringAlgorithm.converteLinkageStrategy: not supported by WEKA");
		}
	}
}
