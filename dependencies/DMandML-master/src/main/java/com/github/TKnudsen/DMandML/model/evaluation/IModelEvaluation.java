package com.github.TKnudsen.DMandML.model.evaluation;

import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.ISelfDescription;
import com.github.TKnudsen.DMandML.model.evaluation.performanceMeasure.IPerformanceMeasure;
import com.github.TKnudsen.DMandML.model.supervised.ILearningModel;

/**
 * <p>
 * Title: IModelEvaluation
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2018 Jürgen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Christian Ritter, Juergen Bernard
 * @version 1.02
 * 
 */
public interface IModelEvaluation<X, Y, L extends ILearningModel<X, Y>> extends ISelfDescription {

	/**
	 * Evaluates the performance of a learning algorithm on a given dataset
	 * 
	 * @param learner
	 *            the learning model
	 * @param featureVectors
	 *            the feature vectors of the data set
	 * @param groundTruth
	 *            the true value that should be assigned to each feature vector
	 * @return a list containing a list with performance values for each iteration
	 *         of evaluation
	 */
	void evaluate(L learner, List<X> featureVectors, List<Y> groundTruth);

	/**
	 * provides a List of added/default performance measures
	 * 
	 * @return
	 */
	List<IPerformanceMeasure<Y>> getPerformanceMeasures();

	/**
	 * retrieves the result of a particular PerformanceMeasure
	 * 
	 * @param performanceMeasure
	 * @return
	 */
	Double getCumulatedPerformance(IPerformanceMeasure<Y> performanceMeasure);

	/**
	 * retrieves the a List of results of a particular PerformanceMeasure
	 * representing the distribution, etc.
	 * 
	 * @param performanceMeasure
	 * @return
	 */
	List<Double> getPerformance(IPerformanceMeasure<Y> performanceMeasure);
}
