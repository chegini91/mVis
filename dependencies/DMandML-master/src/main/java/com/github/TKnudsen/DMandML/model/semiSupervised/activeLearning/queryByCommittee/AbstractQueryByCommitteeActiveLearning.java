package com.github.TKnudsen.DMandML.model.semiSupervised.activeLearning.queryByCommittee;

import java.util.List;
import java.util.function.Function;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.IFeatureVectorObject;
import com.github.TKnudsen.DMandML.data.classification.IClassificationResult;
import com.github.TKnudsen.DMandML.model.semiSupervised.activeLearning.AbstractActiveLearningModel;

/**
 * <p>
 * Title: AbstractQueryByCommitteeActiveLearning
 * </p>
 * <p>
 * <p>
 * Description: queries controversial instances/regions in the input space.
 * Compares the label distributions of every candidate for a given set of
 * models. The winning candidate poses those label distributions where the
 * committee disagrees most.
 * <p>
 * Degree of freedom: measure of disagreement among committee members. See the
 * inheriting classes.
 * </p>
 * <p>
 * <p>
 * Copyright: (c) 2016-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 *
 * @author Juergen Bernard
 * @version 1.06
 */
public abstract class AbstractQueryByCommitteeActiveLearning<FV extends IFeatureVectorObject<?, ?>>
		extends AbstractActiveLearningModel<FV> {

	private List<Function<List<? extends FV>, IClassificationResult<FV>>> classificationApplicationFunctions;

	protected AbstractQueryByCommitteeActiveLearning() {
	}

	public AbstractQueryByCommitteeActiveLearning(
			List<Function<List<? extends FV>, IClassificationResult<FV>>> classificationApplicationFunctions) {
		super(classificationApplicationFunctions.get(0));

		this.classificationApplicationFunctions = classificationApplicationFunctions;
	}

	public abstract String getComparisonMethod();

	public List<Function<List<? extends FV>, IClassificationResult<FV>>> getClassificationApplicationFunctions() {
		return classificationApplicationFunctions;
	}

	public void setClassificationApplicationFunctions(
			List<Function<List<? extends FV>, IClassificationResult<FV>>> classificationApplicationFunctions) {
		this.classificationApplicationFunctions = classificationApplicationFunctions;
	}
}
