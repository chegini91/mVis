package com.github.TKnudsen.DMandML.data.classification;

import java.util.List;
import java.util.function.Supplier;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.IFeatureVectorObject;

/**
 * <p>
 * Title: ITrainingDataSupplier
 * </p>
 * 
 * <p>
 * Description: supplier for training data (e.g., used to train a learning
 * model).
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2017 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.02
 */
public interface ITrainingDataSupplier<FV extends IFeatureVectorObject<?, ?>> extends Supplier<List<FV>> {

	public String getClassAttribute();
}
