package com.github.TKnudsen.DMandML.model.degreeOfInterest.classBased;

import java.util.List;
import java.util.function.Supplier;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.IFeatureVectorObject;
import com.github.TKnudsen.DMandML.data.classification.IClassificationResultSupplier;
import com.github.TKnudsen.DMandML.model.degreeOfInterest.DegreeOfInterestFunction;

/**
 * <p>
 * Title: ClassificationBasedInterestingnessFunction
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
 * @version 1.02
 */
public abstract class ClassificationBasedInterestingnessFunction<FV extends IFeatureVectorObject<?, ?>>
		extends DegreeOfInterestFunction<FV> implements IClassificationBasedInterestingnessFunction<FV> {

	IClassificationResultSupplier<FV> classificationResultSupplier;

	public ClassificationBasedInterestingnessFunction(Supplier<List<FV>> featureVectorSupplier,
			IClassificationResultSupplier<FV> classificationResultSupplier) {
		super(featureVectorSupplier);

		this.classificationResultSupplier = classificationResultSupplier;
	}

	@Override
	public IClassificationResultSupplier<FV> getClassificationResultSupplier() {
		return classificationResultSupplier;
	}

}
