package com.github.TKnudsen.ComplexDataObject.model.distanceMeasure;

import java.util.List;

/**
 * <p>
 * Title: IWeightedDistanceMeasure
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public interface IWeightedDistanceMeasure<T> extends IDistanceMeasure<T> {

	/**
	 * the weightings used by the weighting model of the distance measure.
	 * 
	 * @return
	 */
	public List<Double> getWeights();

}
