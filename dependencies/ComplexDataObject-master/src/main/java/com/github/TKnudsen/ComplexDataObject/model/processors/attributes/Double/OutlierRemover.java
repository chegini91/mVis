package com.github.TKnudsen.ComplexDataObject.model.processors.attributes.Double;

import java.util.ArrayList;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataContainer;
import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataContainerTools;
import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataObject;
import com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject.DataProcessingCategory;
import com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject.IComplexDataObjectProcessor;
import com.github.TKnudsen.ComplexDataObject.model.tools.StatisticsSupport;

/**
 * <p>
 * Title: OutlierRemover
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class OutlierRemover implements IComplexDataObjectProcessor {

	private String attribute;
	private double stdDeviationMultiple = 2.98;

	public OutlierRemover() {
	}

	public OutlierRemover(double stdDeviationMultiple) {
		this.stdDeviationMultiple = stdDeviationMultiple;
	}

	public OutlierRemover(String attribute) {
		this.attribute = attribute;
	}

	public OutlierRemover(String attribute, double stdDeviationMultiple) {
		this.attribute = attribute;
		this.stdDeviationMultiple = stdDeviationMultiple;
	}

	@Override
	public void process(List<ComplexDataObject> data) {
		if (attribute == null)
			throw new IllegalArgumentException("OutlierRemover requires attribute definition first.");

		List<Number> values = new ArrayList<>(data.size());
		for (ComplexDataObject cdo : data)
			if (cdo.getAttribute(attribute) != null)
				if (cdo.getAttribute(attribute) instanceof Number)
					values.add((Number) cdo.getAttribute(attribute));

		StatisticsSupport statisticsSupport = new StatisticsSupport(values);
		double stdDev = statisticsSupport.getStandardDeviation();
		stdDev *= stdDeviationMultiple;

		double means = statisticsSupport.getMean();
		for (ComplexDataObject cdo : data)
			if (cdo.getAttribute(attribute) != null)
				if (cdo.getAttribute(attribute) instanceof Number) {
					Number number = (Number) cdo.getAttribute(attribute);
					if (number.doubleValue() - stdDev > means)
						cdo.add(attribute, means + stdDev);
					else if (number.doubleValue() + stdDev < means)
						cdo.add(attribute, means - stdDev);
				}
	}

	@Override
	public void process(ComplexDataContainer container) {
		if (attribute == null)
			throw new IllegalArgumentException("OutlierRemover requires attribute definition first.");

		if (container == null)
			return;

		if (!container.isNumeric(attribute))
			throw new IllegalArgumentException("OutlierRemover requires numeric attribute definition.");

		process(ComplexDataContainerTools.getObjectList(container));
	}

	@Override
	public DataProcessingCategory getPreprocessingCategory() {
		return DataProcessingCategory.DATA_CLEANING;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public double getStdDeviationMultiple() {
		return stdDeviationMultiple;
	}

	public void setStdDeviationMultiple(double stdDeviationMultiple) {
		this.stdDeviationMultiple = stdDeviationMultiple;
	}

}
