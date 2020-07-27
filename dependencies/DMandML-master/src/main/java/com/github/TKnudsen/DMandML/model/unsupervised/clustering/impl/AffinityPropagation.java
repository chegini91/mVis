package com.github.TKnudsen.DMandML.model.unsupervised.clustering.impl;

import java.util.ArrayList;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.DMandML.data.cluster.featureVector.numerical.NumericalFeatureVectorCluster;
import com.github.TKnudsen.DMandML.data.cluster.featureVector.numerical.NumericalFeatureVectorClusterResult;
import com.github.TKnudsen.DMandML.data.elki.ELKIDataWrapper;
import com.github.TKnudsen.DMandML.model.unsupervised.clustering.IClusteringAlgorithm;

import de.lmu.ifi.dbs.elki.algorithm.clustering.affinitypropagation.AffinityPropagationClusteringAlgorithm;
import de.lmu.ifi.dbs.elki.algorithm.clustering.affinitypropagation.AffinityPropagationInitialization;
import de.lmu.ifi.dbs.elki.algorithm.clustering.affinitypropagation.DistanceBasedInitializationWithMedian;
import de.lmu.ifi.dbs.elki.data.Clustering;
import de.lmu.ifi.dbs.elki.data.NumberVector;
import de.lmu.ifi.dbs.elki.data.model.MedoidModel;
import de.lmu.ifi.dbs.elki.database.ids.DBIDIter;
import de.lmu.ifi.dbs.elki.database.ids.DBIDs;
import de.lmu.ifi.dbs.elki.database.relation.Relation;
import de.lmu.ifi.dbs.elki.distance.distancefunction.DistanceFunction;
import de.lmu.ifi.dbs.elki.distance.distancefunction.minkowski.EuclideanDistanceFunction;

/**
 * <p>
 * Title: AffinityPropagation
 * </p>
 * 
 * <p>
 * Description: Implementation is based on ELKI.
 * 
 * http://weka.sourceforge.net/doc.dev/weka/clusterers/Canopy.html:
 * 
 * For more information see: Clustering by passing messages between data.
 * Brendan J. Frey and Delbert Dueck. Science, 2007.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2017-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class AffinityPropagation implements IClusteringAlgorithm<NumericalFeatureVector> {

	private AffinityPropagationClusteringAlgorithm<NumberVector> apAlgorithm;

	/**
	 * Quantile to use for the distance-based initialization of the
	 * AP-initialization. [0....1]
	 */
	double quantile = 0.5;

	/**
	 * Damping factor lambda [0.5 ... 1]. Extent to which the current value is
	 * maintained relative to incoming values. Used to avoid numerical oscillations
	 * when updating these values.
	 */
	double lambda = 0.5;

	/**
	 * Terminate after 10 iterations with no changes.
	 */
	int convergence = 10;

	/**
	 * Maximum number of iterations.
	 */
	int maxiter = 1000;

	/**
	 * replaces Database db, List<NumericalFeatureVector> featureVectors, and
	 * Map<NumberVector, NumericalFeatureVector> lookupTable.
	 */
	ELKIDataWrapper elkiDataWrapper;

	/**
	 * ELKI clustering result
	 */
	private Clustering<MedoidModel> clusteringResult;

	/**
	 * the clustering result mapped back to the feature vectors
	 */
	private NumericalFeatureVectorClusterResult clusteringResultFVs;

	public AffinityPropagation() {
		this(null, 0.5, 0.5, 10, 1000);
	}

	public AffinityPropagation(List<? extends NumericalFeatureVector> featureVectors) {
		this(featureVectors, 0.5, 0.5, 10, 1000);
	}

	public AffinityPropagation(List<? extends NumericalFeatureVector> featureVectors, double quantile, double lambda,
			int convergence, int maxiter) {

		if (featureVectors != null)
			setFeatureVectors(featureVectors);

		if (quantile < 0 || quantile > 1.0)
			throw new IllegalArgumentException(getName() + ": quantile has to be between [0 and 1]");

		this.quantile = quantile;
		this.lambda = lambda;
		this.convergence = convergence;
		this.maxiter = maxiter;
	}

	@Override
	public List<NumericalFeatureVector> getFeatureVectors() {
		if (elkiDataWrapper == null)
			return null;

		return elkiDataWrapper.getFeatureVectors();
	}

	@Override
	public void setFeatureVectors(List<? extends NumericalFeatureVector> featureVectors) {
		elkiDataWrapper = new ELKIDataWrapper(featureVectors);

		this.clusteringResult = null;
		this.clusteringResultFVs = null;
	}

	@Override
	public void calculateClustering() {
		if (elkiDataWrapper == null)
			throw new NullPointerException(
					getName() + ": no feature vectors set before calculating a clustering result.");

		this.clusteringResult = null;
		this.clusteringResultFVs = null;

		DistanceFunction<NumberVector> distanceFunction = new EuclideanDistanceFunction();

		AffinityPropagationInitialization<NumberVector> initialization = new DistanceBasedInitializationWithMedian<>(
				distanceFunction, quantile);

		apAlgorithm = new AffinityPropagationClusteringAlgorithm<>(initialization, lambda, convergence, maxiter);

		clusteringResult = apAlgorithm.run(elkiDataWrapper.getDB());
	}

	@Override
	public NumericalFeatureVectorClusterResult getClusteringResult() {
		if (clusteringResult == null)
			throw new NullPointerException(
					"AffinitiyPropagationInitialization: clustering result null. calculateClustering() first.");

		if (clusteringResultFVs != null)
			return clusteringResultFVs;

		Relation<NumberVector> rel = elkiDataWrapper.getRelation();

		List<NumericalFeatureVectorCluster> fvCLusters = new ArrayList<>();
		int c = 1;

		for (de.lmu.ifi.dbs.elki.data.Cluster<MedoidModel> elkiCluster : clusteringResult.getAllClusters()) {
			DBIDs iDs = elkiCluster.getIDs();

			List<NumericalFeatureVector> fvs = new ArrayList<>();
			for (DBIDIter it = iDs.iter(); it.valid(); it.advance()) {
				NumberVector v = rel.get(it);

				// NumericalFeatureVector fv = lookupTable.get(v);

				NumericalFeatureVector fv = elkiDataWrapper.getFeatureVector(v);

				if (fv == null)
					throw new NullPointerException(getName() + ".getClusteringResult(): feature vector lookup failed.");

				fvs.add(fv);
			}

			fvCLusters.add(new NumericalFeatureVectorCluster(fvs, getName() + " cluster " + c++));
		}

		clusteringResultFVs = new NumericalFeatureVectorClusterResult(fvCLusters, getName());

		return clusteringResultFVs;
	}

	@Override
	public String getName() {
		return "Affinity Propagation";
	}

	@Override
	public String getDescription() {
		return getName();
	}

}
