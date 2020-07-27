package com.github.TKnudsen.ComplexDataObject.model.transformations;

import java.util.List;

import com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject.DataTransformationCategory;

/**
 * <p>
 * Title: IDataTransformation
 * </p>
 * 
 * <p>
 * Description: Basic transformation of an object into another 'space'.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public interface IDataTransformation<I, O> {

	/**
	 * mapping from a (high-dimensional) space into another (low-dimensional) space.
	 * NO calculation of transformation model.
	 * 
	 * @param input
	 * @return
	 */
	public List<O> transform(I input);

	/**
	 * * mapping from a (high-dimensional) space into another (low-dimensional)
	 * space. NO calculation of transformation model.
	 * 
	 * @param inputObjects
	 * @return
	 */
	public List<O> transform(List<I> inputObjects);

	public DataTransformationCategory getDataTransformationCategory();
}