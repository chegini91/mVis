package com.github.TKnudsen.ComplexDataObject.data.complexDataObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * Title: ComplexDataContainerTools
 * </p>
 * 
 * <p>
 * Description: Little helpers for ComplexDataContainers
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class ComplexDataContainerTools {

	public static List<ComplexDataObject> getObjectList(ComplexDataContainer container) {
		List<ComplexDataObject> list = new ArrayList<>();

		Iterator<ComplexDataObject> iterator = container.iterator();

		while (iterator.hasNext())
			list.add(iterator.next());

		return list;
	}
}
