package com.github.TKnudsen.ComplexDataObject.model.tools;

/**
 * <p>
 * Title: StringUtils
 * </p>
 *
 * <p>
 * Description: little helpers when working with String.
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 *
 * @author Juergen Bernard
 * @version 1.01
 */
public class StringUtils {

	public static String truncateDouble(double value, int decimals) {
		if (decimals < 0)
			return null;

		String result = (new Double(value)).toString();
		int dot = result.lastIndexOf(".");
		int length = result.length();
		result = result.substring(0, (int) (Math.min(dot + 1 + decimals, length)));

		return result;
	}
}
