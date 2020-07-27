package com.github.TKnudsen.ComplexDataObject.model.tools;

import java.util.List;

/**
 * <p>
 * Title: IndexTools
 * </p>
 *
 * <p>
 * Description: little helpers to apply index operations on arrays and lists.
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 *
 * @author Juergen Bernard
 * @version 1.01
 */
public class IndexTools {

	/**
	 * index of the max value within an int array.
	 * 
	 * @param values
	 * @return
	 */
	public static int getMaxIndex(int values[]) {
		if (values == null || values.length == 0)
			return -1;
		int maxIndex = 0;
		double max = values[maxIndex];
		for (int i = 1; i < values.length; i++) {
			if (values[i] > max) {
				max = values[i];
				maxIndex = i;
			}
		}
		return maxIndex;
	}

	/**
	 * index of the maximum value within an array.
	 * 
	 * @param values
	 * @return
	 */
	public static int getMaxIndex(double values[]) {
		if (values == null || values.length == 0)
			return -1;

		int maxIndex = 0;
		double max = values[maxIndex];

		for (int i = 1; i < values.length; i++) {
			if (values[i] > max) {
				max = values[i];
				maxIndex = i;
			}
		}

		return maxIndex;
	}

	/**
	 * index of the max value within a list.
	 * 
	 * @param values
	 * @return
	 */
	public static int getMaxIndex(List<Number> values) {
		if (values == null || values.size() == 0)
			return -1;

		int maxIndex = 0;
		Number max = values.get(maxIndex);

		for (int i = 1; i < values.size(); i++) {
			if (values.get(maxIndex).doubleValue() > max.doubleValue()) {
				max = values.get(maxIndex);
				maxIndex = i;
			}
		}

		return maxIndex;
	}
}
