package com.github.TKnudsen.DMandML.data.outliers;

import java.util.Iterator;
import java.util.Map;

import com.github.TKnudsen.ComplexDataObject.model.tools.MathFunctions;

/**
 * <p>
 * Title: OutlierAnalysisResult
 * </p>
 * 
 * <p>
 * Description: data structure that stores the result of outlier analysis
 * algorithms.
 * </p>
 * 
 * <p>
 * Copyright: (c) 22018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class OutlierAnalysisResult<FV> implements IOutlierAnalysisResult<FV> {

	private String name;

	private long ID = MathFunctions.randomLong();

	private final Map<FV, Double> outlierScores;

	public OutlierAnalysisResult(Map<FV, Double> outlierScores) {
		this(outlierScores, null);
	}

	public OutlierAnalysisResult(Map<FV, Double> outlierScores, String name) {
		this.outlierScores = outlierScores;
		this.name = name;
	}

	@Override
	public Iterator<FV> iterator() {
		return getOutlierScores().keySet().iterator();
	}

	@Override
	public boolean containsElement(FV fv) {
		return getOutlierScores().containsKey(fv);
	}

	@Override
	public double getOutlierScore(FV fv) {
		Double score = getOutlierScores().get(fv);

		if (Double.isNaN(score))
			return Double.NaN;

		return score;
	}

	@Override
	public int size() {
		return getOutlierScores().size();
	}

	@Override
	public long getID() {
		return ID;
	}

	@Override
	public String getName() {
		if (name != null)
			return name;

		return "OutlierAnalysisResult";
	}

	@Override
	public String getDescription() {
		return getName();
	}

	public Map<FV, Double> getOutlierScores() {
		return outlierScores;
	}

}
