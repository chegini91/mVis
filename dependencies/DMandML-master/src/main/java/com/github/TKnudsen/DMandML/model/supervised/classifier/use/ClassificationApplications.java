package com.github.TKnudsen.DMandML.model.supervised.classifier.use;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.github.TKnudsen.DMandML.data.classification.IClassificationResult;
import com.github.TKnudsen.DMandML.model.supervised.classifier.IClassifier;

public class ClassificationApplications {

	public static <FV> List<Function<List<? extends FV>, IClassificationResult<FV>>> createClassificationApplicationFunctions(
			List<IClassifier<FV>> classifiers) {

		List<Function<List<? extends FV>, IClassificationResult<FV>>> classificationApplicationFunctions = new ArrayList<>();
		for (IClassifier<FV> classifier : classifiers)
			classificationApplicationFunctions.add(classifier::createClassificationResult);

		return classificationApplicationFunctions;
	}
}
