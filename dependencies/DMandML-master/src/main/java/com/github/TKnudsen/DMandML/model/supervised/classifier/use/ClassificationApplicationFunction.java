package com.github.TKnudsen.DMandML.model.supervised.classifier.use;

import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.ISelfDescription;
import com.github.TKnudsen.DMandML.data.classification.IClassificationResult;
import com.github.TKnudsen.DMandML.model.supervised.classifier.IClassifier;

/**
 * <p>
 * Title: ClassificationApplicationFunction
 * </p>
 * 
 * <p>
 * Description: wrapper class that can create classification results with a
 * given classifier.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.02
 */
public class ClassificationApplicationFunction<FV> implements IClassificationApplicationFunction<FV>, ISelfDescription {

	private final IClassifier<FV> classifier;

	public ClassificationApplicationFunction(IClassifier<FV> classifier) {
		this.classifier = classifier;
	}

	@Override
	public IClassificationResult<FV> apply(List<? extends FV> t) {
		return classifier.createClassificationResult(t);
	}

	@Override
	public String getName() {
		return classifier.getName();
	}

	@Override
	public String getDescription() {
		return getName();
	}

}
