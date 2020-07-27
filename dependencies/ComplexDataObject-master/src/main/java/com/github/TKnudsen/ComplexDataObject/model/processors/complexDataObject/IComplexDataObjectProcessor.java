package com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject;

import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataContainer;
import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataObject;
import com.github.TKnudsen.ComplexDataObject.model.processors.IDataProcessor;

/**
 * <p>
 * Title: IComplexDataObjectProcessor
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2016-2017
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 * 
 * TODO_GENERICS Could probably unify IMixedDataFeatureVectorProcessor and INumericalFeatureVectorProcessor and ICompledDataObjectProcessor
 */

public interface IComplexDataObjectProcessor extends IDataProcessor<ComplexDataObject> {

	public void process(ComplexDataContainer container);
}
