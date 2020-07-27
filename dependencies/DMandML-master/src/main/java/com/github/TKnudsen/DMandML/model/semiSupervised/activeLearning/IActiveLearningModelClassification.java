package com.github.TKnudsen.DMandML.model.semiSupervised.activeLearning;

import com.github.TKnudsen.DMandML.data.classification.IClassificationResultSupplier;

/**
 * <p>
 * Title: IActiveLearningModelClassification
 * </p>
 * 
 * <p>
 * Active learning for classification tasks.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.06
 */
public interface IActiveLearningModelClassification<FV> extends IActiveLearningModel<FV, String> {

	@Deprecated
	public IClassificationResultSupplier<FV> getClassificationResultSupplier();
}