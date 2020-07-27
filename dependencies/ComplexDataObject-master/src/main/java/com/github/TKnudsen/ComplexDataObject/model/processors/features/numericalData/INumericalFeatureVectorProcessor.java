package com.github.TKnudsen.ComplexDataObject.model.processors.features.numericalData;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVectorContainer;
import com.github.TKnudsen.ComplexDataObject.model.processors.IDataProcessor;

/**
 * <p>
 * Title: INumericalFeatureVectorProcessor
 * </p>
 *
 * <p>
 * Description:
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2016-2018
 * </p>
 *
 * @author Juergen Bernard
 * @version 1.03
 * 
 *          TODO_GENERICS Could probably unify IMixedDataFeatureVectorProcessor
 *          and INumericalFeatureVectorProcessor and ICompledDataObjectProcessor
 */
public interface INumericalFeatureVectorProcessor extends IDataProcessor<NumericalFeatureVector> {

	public void process(NumericalFeatureVectorContainer container);
}
