package com.github.TKnudsen.ComplexDataObject.model.weighting;

/**
 * <p>
 * Title: IWeightingKernel
 * </p>
 * 
 * <p>
 * Description: Behavior of kernel functions to determine the weight of given
 * objects, or indices, respectively.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2016
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.02
 */
public interface IWeightingKernel<T extends Number> {

	public double getWeight(T t);

	public T getReference();

	public void setReference(T t);

	/**
	 * Value range where the kernel provides weights
	 * 
	 * @return
	 */
	public T getInterval();

	public void setInterval(T interval);

}
