package com.github.TKnudsen.ComplexDataObject.model.processors.features.mixedData;

import com.github.TKnudsen.ComplexDataObject.data.features.mixedData.MixedDataFeatureContainer;
import com.github.TKnudsen.ComplexDataObject.data.features.mixedData.MixedDataFeatureVector;
import com.github.TKnudsen.ComplexDataObject.model.processors.IDataProcessor;

/**
 * <p>
 * Title: IMixedDataFeatureVectorProcessor
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2017-2018
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.04
 * 
 *          TODO_GENERICS Could probably unify IMixedDataFeatureVectorProcessor
 *          and INumericalFeatureVectorProcessor and ICompledDataObjectProcessor
 */
public interface IMixedDataFeatureVectorProcessor extends IDataProcessor<MixedDataFeatureVector> {

	public void process(MixedDataFeatureContainer container);
}
