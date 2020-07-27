package com.github.TKnudsen.DMandML.model.degreeOfInterest;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.IFeatureVectorObject;

/**
 * <p>
 * Title: InterestingnessFunction
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
 * @author Juergen Bernard
 * @version 1.03
 */
public abstract class DegreeOfInterestFunction<FV extends IFeatureVectorObject<?, ?>>
		implements IDegreeOfInterestFunction<FV> {

	private Supplier<List<FV>> featureVectorSupplier;

	protected Map<FV, Double> interestingnessScores;

	public DegreeOfInterestFunction(Supplier<List<FV>> featureVectorSupplier) {
		this.featureVectorSupplier = featureVectorSupplier;
	}

	public abstract void run();

	@Override
	public Double apply(FV featureVector) {
		if (interestingnessScores == null) {
			run();
		}

		return interestingnessScores.get(featureVector);
	}

	/**
	 * resets the results of the interestingness calculation model. allows lazy
	 * implementations based on abstract function calls.
	 */
	public void resetInterestingnessScores() {
		this.interestingnessScores = null;
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public Supplier<List<FV>> getFeatureVectorSupplier() {
		return featureVectorSupplier;
	}
}
