package com.github.TKnudsen.DMandML.model.supervised;

import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.IFeatureVectorObject;

/**
 * <p>
 * Title: ILearningModelEvaluation
 * </p>
 * 
 * <p>
 * Description: evaluator for learning models
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.03
 * 
 */
public interface ILearningModelEvaluation<X extends IFeatureVectorObject<?, ?>, Y> {

	public double getQuality(ILearningModel<X, Y> model, List<X> testData, Y targetVariable);
}
