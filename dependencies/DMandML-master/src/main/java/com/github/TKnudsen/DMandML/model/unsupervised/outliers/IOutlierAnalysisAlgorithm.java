package com.github.TKnudsen.DMandML.model.unsupervised.outliers;

import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.ISelfDescription;
import com.github.TKnudsen.DMandML.data.outliers.IOutlierAnalysisResult;

/**
 * <p>
 * Title: IOutlierAnalysisAlgorithm
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: (c) 2017-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.03
 */
public interface IOutlierAnalysisAlgorithm<FV> extends ISelfDescription {

	public void setData(List<? extends FV> data);

	/**
	 * parameters function call
	 */
	public void run();

	/**
	 * the outlier prediction score. Score is a more general term than
	 * weight/probability, etc. However, it is expected that implementations make
	 * use of a relative value domain where 1.0 means the maximum likelihood to be
	 * an outlier.
	 * 
	 * @param object
	 * @deprecated use getOutlierAnalysisResult()
	 * @return
	 */
	public double getOutlierScore(FV object);

	public IOutlierAnalysisResult<FV> getOutlierAnalysisResult();

	/**
	 * allows resetting calculated scores, e.g. when data or parameters have been
	 * changed. allows lazy implementations.
	 */
	public void resetScores();
}
