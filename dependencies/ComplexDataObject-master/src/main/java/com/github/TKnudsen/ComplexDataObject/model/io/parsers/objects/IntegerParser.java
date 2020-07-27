package com.github.TKnudsen.ComplexDataObject.model.io.parsers.objects;

/**
 * <p>
 * Title: IntegerParser
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
 * @version 1.0
 */

public class IntegerParser implements IObjectParser<Integer> {

	@Override
	public Integer apply(Object object) {
		String stringValue = String.valueOf(object).toLowerCase();

		stringValue = stringValue.trim();
		stringValue = stringValue.replace(",", ".");
		if (stringValue.contains(" "))
			stringValue = stringValue.substring(0, stringValue.indexOf(" "));

		try {
			return Integer.parseInt(stringValue);
		} catch (Exception e) {
			return null;
		}
	}

}
