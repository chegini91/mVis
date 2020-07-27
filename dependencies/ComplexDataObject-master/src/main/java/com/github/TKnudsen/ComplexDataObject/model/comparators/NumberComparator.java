package com.github.TKnudsen.ComplexDataObject.model.comparators;

import java.util.Comparator;

/**
 * <p>
 * Title: NumberComparator
 * </p>
 * 
 * <p>
 * Description: compares numbers
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 *
 * @author Juergen Bernard
 * @version 1.01
 */
public class NumberComparator implements Comparator<Number> {

	@Override
	public int compare(Number x, Number y) {
		if (x == null && y == null)
			return 0;
		if (x == null)
			return 1;
		else if (y == null)
			return -1;

		Double a = x.doubleValue();
		Double b = y.doubleValue();

		return Double.compare(a, b);
	}
}