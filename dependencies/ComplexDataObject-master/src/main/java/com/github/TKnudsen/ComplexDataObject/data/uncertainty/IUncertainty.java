package com.github.TKnudsen.ComplexDataObject.data.uncertainty;

/**
 * <p>
 * Title: IUncertainty
 * </p>
 * 
 * <p>
 * Description: Basic interface for uncertainty data modeling.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2015-2018
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.02
 */
public interface IUncertainty<T> {

	public T getMostCertainRepresentant();
}
