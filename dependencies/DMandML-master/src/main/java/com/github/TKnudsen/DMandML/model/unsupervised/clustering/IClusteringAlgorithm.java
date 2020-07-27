package com.github.TKnudsen.DMandML.model.unsupervised.clustering;

import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.ISelfDescription;
import com.github.TKnudsen.DMandML.data.cluster.ICluster;
import com.github.TKnudsen.DMandML.data.cluster.IClusteringResult;

/**
 * <p>
 * Title: IClusteringAlgorithm
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.10
 */
public interface IClusteringAlgorithm<F> extends ISelfDescription {

	/**
	 * returns a unmodifiable view on the internal data state.
	 * 
	 * @return
	 */
	public List<F> getFeatureVectors();

	/**
	 * constructs a copy of a given list of elements
	 * 
	 * @param featureVectors
	 */
	public void setFeatureVectors(List<? extends F> featureVectors);

	/**
	 * runs the clustering algorithm. To be general no parameter shall be required
	 * at this state.
	 */
	public void calculateClustering();

	public IClusteringResult<F, ? extends ICluster<F>> getClusteringResult();
}
