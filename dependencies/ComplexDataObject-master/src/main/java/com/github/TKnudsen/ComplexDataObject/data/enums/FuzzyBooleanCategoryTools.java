package com.github.TKnudsen.ComplexDataObject.data.enums;

/**
 * <p>
 * Title: FuzzyBooleanCategoryTools
 * </p>
 * 
 * <p>
 * Description: little helpers for working with FuzzyBooleanCategory objects.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2016
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.0
 */

public class FuzzyBooleanCategoryTools {
	public static Boolean convertToBoolean(FuzzyBooleanCategory fuzzyBoolean) {
		if (fuzzyBoolean == null)
			return null;

		switch (fuzzyBoolean) {
		case YES:
			return true;
		case NO:
			return false;
		case LIKELY:
			return true;
		case UNLIKELY:
			return false;
		case UNCLEAR:
			return null;
		case NO_INFORMATION:
			return null;
		default:
			return null;
		}
	}

	public static String convertToCategorical(FuzzyBooleanCategory fuzzyBoolean) {
		if (fuzzyBoolean == null)
			return null;

		switch (fuzzyBoolean) {
		case YES:
			return "yes";
		case NO:
			return "no";
		case LIKELY:
			return "likely";
		case UNLIKELY:
			return "unlikely";
		case UNCLEAR:
			return "unclear";
		case NO_INFORMATION:
			return "no information";
		default:
			return "no information";
		}
	}
}
