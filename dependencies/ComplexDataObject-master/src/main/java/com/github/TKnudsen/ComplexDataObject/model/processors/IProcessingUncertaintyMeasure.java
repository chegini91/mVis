package com.github.TKnudsen.ComplexDataObject.model.processors;

import com.github.TKnudsen.ComplexDataObject.data.uncertainty.IUncertainty;

public interface IProcessingUncertaintyMeasure<D, U extends IUncertainty<?>> {

	public void calculateUncertaintyMeasure();
}
