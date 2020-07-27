package com.github.TKnudsen.ComplexDataObject.model.comparators;

import java.util.Comparator;

import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataObject;

/**
 * <p>
 * Title: ComplexDataObjectAttributeComparator
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2017-2018,
 * https://github.com/TKnudsen/ComplexDataObject
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class ComplexDataObjectAttributeComparator implements Comparator<ComplexDataObject> {

	private String attribute;

	public ComplexDataObjectAttributeComparator(String attribute) {
		this.attribute = attribute;
	}

	@Override
	public int compare(ComplexDataObject o1, ComplexDataObject o2) {
		if (o1 == null)
			return 1;
		if (o2 == null)
			return -1;

		if (o1.equals(o2))
			return 0;

		if (o1.getAttribute(attribute) == null)
			return 1;
		if (o2.getAttribute(attribute) == null)
			return -1;

		Object v1 = o1.getAttribute(attribute);
		Object v2 = o2.getAttribute(attribute);

		if (v1 instanceof Number && v2 instanceof Number)
			return new NumberComparator().compare((Number) v1, (Number) v2);
		else
			return v1.toString().compareTo(v2.toString());
	}

}