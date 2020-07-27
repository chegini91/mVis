package com.github.TKnudsen.ComplexDataObject.model.comparators;

import java.util.Comparator;
import java.util.function.Function;

/**
 * <p>
 * Title: ObjectScoreComparator
 * </p>
 * 
 * <p>
 * Description: maps CDOs to numerical values to facilitate item comparison.
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
public class ObjectScoreComparator<ITEM> implements Comparator<ITEM> {

	private Function<ITEM, Double> scoringFunction;
	private NumberComparator numberComparator = new NumberComparator();

	public ObjectScoreComparator(Function<ITEM, Double> scoringFunction) {
		this.scoringFunction = scoringFunction;
	}

	@Override
	public int compare(ITEM o1, ITEM o2) {
		if (o1 == null)
			return 1;
		if (o2 == null)
			return -1;

		if (o1.equals(o2))
			return 0;

		Double v1 = scoringFunction.apply(o1);
		Double v2 = scoringFunction.apply(o2);

		return numberComparator.compare(v1, v2);
	}
}
