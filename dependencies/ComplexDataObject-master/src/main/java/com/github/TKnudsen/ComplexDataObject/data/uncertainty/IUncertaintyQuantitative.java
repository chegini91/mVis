package com.github.TKnudsen.ComplexDataObject.data.uncertainty;

/**
 * <p>
 * Title: IUncertaintyQuantitative
 * </p>
 * 
 * <p>
 * Description: Tnterface for uncertainty information for qualitative data.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2015-2017
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.0
 */
public interface IUncertaintyQuantitative<T> extends IUncertainty<T> {

	public T getMinimum();

	public T getMaximum();

	public double getVariation();

}