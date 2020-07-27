package com.github.TKnudsen.DMandML.model.unsupervised.clustering;

import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.ISelfDescription;
import com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.IDistanceMeasure;
import com.github.TKnudsen.DMandML.data.cluster.ICluster;

/**
 * <p>
 * Title: IClusterSplittingAlgorithm
 * </p>
 * 
 * <p>
 * Description: splits/divides a cluster into *n* clusters
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.02
 */
public interface IClusterSplittingAlgorithm<O, C extends ICluster<? extends O>> extends ISelfDescription {

	public void setSplitCount(int k);

	public IDistanceMeasure<O> getDistanceMeasure();

	public List<C> splitCluster(C cluster);
}