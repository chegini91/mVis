package com.github.TKnudsen.ComplexDataObject.model.processors;

import java.util.List;

import com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject.DataProcessingCategory;

/**
 * <p>
 * Title: IDataProcessor
 * </p>
 * 
 * <p>
 * Description: Baseline behavior of a data processing routine.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2011-2016
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.05
 */
public interface IDataProcessor<D> {

	public void process(List<D> data);

	public DataProcessingCategory getPreprocessingCategory();
}
