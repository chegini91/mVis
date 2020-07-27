package com.github.TKnudsen.DMandML.model.degreeOfInterest.classBased;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.IFeatureVectorObject;
import com.github.TKnudsen.DMandML.data.classification.IClassificationResultSupplier;
import com.github.TKnudsen.DMandML.model.degreeOfInterest.IDegreeOfInterestFunction;

/**
 * <p>
 * Title: IClassificationBasedInterestingnessFunction
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2017 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public interface IClassificationBasedInterestingnessFunction<FV extends IFeatureVectorObject<?, ?>> extends IDegreeOfInterestFunction<FV> {

	public IClassificationResultSupplier<FV> getClassificationResultSupplier();
}
