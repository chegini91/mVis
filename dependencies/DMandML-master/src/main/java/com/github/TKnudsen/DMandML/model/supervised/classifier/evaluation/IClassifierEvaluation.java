package com.github.TKnudsen.DMandML.model.supervised.classifier.evaluation;

import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.ISelfDescription;
import com.github.TKnudsen.DMandML.model.supervised.classifier.IClassifier;

/**
 * <p>
 * Title: IClassifierEvaluation
 * </p>
 * 
 * <p>
 * Description: Interface for classification quality assessment
 * </p>
 * 
 * <p>
 * Copyright: (c) 2017-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.02
 * 
 */
public interface IClassifierEvaluation<FV> extends ISelfDescription {

	public double getQuality(IClassifier<FV> model, List<FV> testData, String targetVariable);

}
