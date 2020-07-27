package com.github.TKnudsen.ComplexDataObject.model.transformations.mergeAndJoin.Boolean;

import com.github.TKnudsen.ComplexDataObject.model.transformations.mergeAndJoin.IObjectMerger;

/**
 * <p>
 * Title: SubtractionFunction
 * </p>
 * 
 * <p>
 * Description: provides the difference between two doubles.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class BooleanEqualsFunction implements IObjectMerger<Boolean> {

	@Override
	public Boolean merge(Boolean object1, Boolean object2) {
		if (object1 == null && object2 == null)
			return null;

		if (object1 == null)
			return false;

		if (object2 == null)
			return false;

		return object1 == object2;
	}

}
