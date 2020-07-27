package com.github.TKnudsen.ComplexDataObject.model.processors;

import com.github.TKnudsen.ComplexDataObject.data.uncertainty.IUncertainty;

public interface IUncertainDataProcessor<D, U extends IUncertainty<?>> {

	public IProcessingUncertaintyMeasure<D, U> getUncertaintyMeasure(D originalData, D processedData);
}
