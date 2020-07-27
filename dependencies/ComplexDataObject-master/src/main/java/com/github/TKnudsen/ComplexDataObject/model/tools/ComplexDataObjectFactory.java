package com.github.TKnudsen.ComplexDataObject.model.tools;

import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataObject;

/**
 * <p>
 * Title: ComplexDataObjectFactory
 * </p>
 *
 * <p>
 * Description:
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2016
 * </p>
 *
 * @author Juergen Bernard
 * @version 1.01
 */
public class ComplexDataObjectFactory {

	public static ComplexDataObject createObject(String attribute1, Object value1, String attribute2, Object value2) {
		ComplexDataObject a = new ComplexDataObject();
		a.add(attribute1, value1);
		a.add(attribute2, value2);
		return a;
	}
}
