package com.github.TKnudsen.DMandML.model.degreeOfInterest;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.IFeatureVectorObject;
import com.github.TKnudsen.ComplexDataObject.data.interfaces.ISelfDescription;

/**
 * <p>
 * Title: IInterestingnesFunction
 * </p>
 * 
 * <p>
 * Description: Interestingness functions can be used to perform algorithmical
 * guidance support. According to the implemented criterion, the function
 * prefers interesting instances over others.
 * 
 * The default value domain is [0...1] as this allows an easy combination of
 * several (weighted) interestingness functions.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.04
 */
public interface IDegreeOfInterestFunction<FV extends IFeatureVectorObject<?, ?>>
		extends Function<FV, Double>, ISelfDescription {

	public Supplier<List<FV>> getFeatureVectorSupplier();
}
