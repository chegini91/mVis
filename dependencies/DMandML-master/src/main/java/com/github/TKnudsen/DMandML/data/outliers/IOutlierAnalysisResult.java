package com.github.TKnudsen.DMandML.data.outliers;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.IDObject;
import com.github.TKnudsen.ComplexDataObject.data.interfaces.ISelfDescription;

/**
 * <p>
 * Title: IOutlierAnalysisResult
 * </p>
 * 
 * <p>
 * Description: interface for clustering results.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public interface IOutlierAnalysisResult<FV> extends IDObject, ISelfDescription, Iterable<FV> {

	boolean containsElement(FV fv);

	double getOutlierScore(FV fv);

	int size();

}