package com.github.TKnudsen.DMandML.model.evaluation.io;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.ISelfDescription;
import com.github.TKnudsen.DMandML.model.evaluation.IModelEvaluation;
import com.github.TKnudsen.DMandML.model.evaluation.performanceMeasure.IPerformanceMeasure;
import com.github.TKnudsen.DMandML.model.supervised.ILearningModel;

/**
 * <p>
 * Title: AbstractModelEvaluationIO
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Christian Ritter
 * @version 1.02
 * 
 */
public abstract class AbstractModelEvaluationIO<X, Y, L extends ILearningModel<X, Y>> implements ISelfDescription {

	private Map<String, Double> cumulatedPerformanceValues;
	private Map<String, String> metaData;
	private IModelEvaluation<X, Y, L> modelEvaluation;
	private Map<String, List<Double>> performanceValues;
	private List<String> orderedPerformanceMeasureList;

	public AbstractModelEvaluationIO() {
		this(null);
	}

	public AbstractModelEvaluationIO(IModelEvaluation<X, Y, L> modelEvaluation) {
		this.setModelEvaluation(modelEvaluation);
		metaData = new HashMap<>();
		cumulatedPerformanceValues = new HashMap<>();
		performanceValues = new HashMap<>();
		setOrderedPerformanceMeasureList(new ArrayList<>());
		if (modelEvaluation != null) {
			initMeasureMaps();
			metaData.put("modelEvaluation", modelEvaluation.getName());
		}
	}

	public void exportResult(File file) {
		exportResult(file.getAbsolutePath());
	}

	public abstract void exportResult(String directory);

	public Map<String, String> getAllMetaData() {
		return metaData;
	}

	public Double getCumulatedPerformanceValue(IPerformanceMeasure<Y> performanceMeasure) {
		return getCumulatedPerformanceValue(performanceMeasure.getName());
	}

	public Double getCumulatedPerformanceValue(String performanceMeasureName) {
		return cumulatedPerformanceValues.get(performanceMeasureName);
	}

	/**
	 * @return the cumulatedPerformanceValues
	 */
	public Map<String, Double> getCumulatedPerformanceValues() {
		return cumulatedPerformanceValues;
	}

	public String getMetaData(String key) {
		return metaData.get(key);
	}

	/**
	 * @return the modelEvaluation
	 */
	public IModelEvaluation<X, Y, L> getModelEvaluation() {
		return modelEvaluation;
	}

	public List<Double> getPerformanceValue(IPerformanceMeasure<Y> performanceMeasure) {
		return performanceValues.get(performanceMeasure.getName());
	}

	public List<Double> getPerformanceValue(String performanceMeasureName) {
		return performanceValues.get(performanceMeasureName);
	}

	/**
	 * @return the performanceValues
	 */
	public Map<String, List<Double>> getPerformanceValues() {
		return performanceValues;
	}

	public void importResult(File file) {
		importResult(file.getAbsolutePath());
	}

	public abstract void importResult(String directory);

	public void putMetaData(String key, String value) {
		metaData.put(key, value);
	}

	/**
	 * @param cumulatedPerformanceValues
	 *            the cumulatedPerformanceValues to set
	 */
	public void setCumulatedPerformanceValues(Map<String, Double> cumulatedPerformanceValues) {
		this.cumulatedPerformanceValues = cumulatedPerformanceValues;
	}

	/**
	 * @param modelEvaluation
	 *            the modelEvaluation to set
	 */
	public void setModelEvaluation(IModelEvaluation<X, Y, L> modelEvaluation) {
		this.modelEvaluation = modelEvaluation;
	}

	/**
	 * @param performanceValues
	 *            the performanceValues to set
	 */
	public void setPerformanceValues(Map<String, List<Double>> performanceValues) {
		this.performanceValues = performanceValues;
	}

	private void initMeasureMaps() {
		for (IPerformanceMeasure<Y> pm : modelEvaluation.getPerformanceMeasures()) {
			cumulatedPerformanceValues.put(pm.getName(), modelEvaluation.getCumulatedPerformance(pm));
			performanceValues.put(pm.getName(), modelEvaluation.getPerformance(pm));
		}
	}

	/**
	 * @return the orderedPerformanceMeasureList
	 */
	public List<String> getOrderedPerformanceMeasureList() {
		return orderedPerformanceMeasureList;
	}

	/**
	 * @param orderedPerformanceMeasureList
	 *            the orderedPerformanceMeasureList to set
	 */
	public void setOrderedPerformanceMeasureList(List<String> orderedPerformanceMeasureList) {
		this.orderedPerformanceMeasureList = orderedPerformanceMeasureList;
	}
}
