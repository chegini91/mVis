package com.github.TKnudsen.ComplexDataObject.model.distanceMeasure;

import java.util.function.ToDoubleBiFunction;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.ISelfDescription;

/**
 * <p>
 * Title: IDistanceMeasure
 * </p>
 * 
 * <p>
 * Description: Basic interface modeling distances between two objects of
 * identical type.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2017-2018,
 * https://github.com/TKnudsen/ComplexDataObject
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.02
 */
public interface IDistanceMeasure<T> extends ToDoubleBiFunction<T, T>, ISelfDescription {

	public double getDistance(T o1, T o2);
}
