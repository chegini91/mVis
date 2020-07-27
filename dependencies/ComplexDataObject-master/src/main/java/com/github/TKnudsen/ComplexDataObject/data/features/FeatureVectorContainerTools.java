package com.github.TKnudsen.ComplexDataObject.data.features;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * Title: FeatureVectorContainerTools
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.02
 * 
 * TODO_GENERICS No longer specific for FeatureVectorContainer. May become obsolete, or replaced by general "Iterable to List" method"
 */

public class FeatureVectorContainerTools {

	public static <T> List<T> getObjectList(Iterable<T> container) {
		List<T> list = new ArrayList<>();

		Iterator<T> iterator = container.iterator();

		while (iterator.hasNext())
			list.add(iterator.next());

		return list;
	}
}
