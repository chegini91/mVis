package com.github.TKnudsen.ComplexDataObject.data.complexDataObject;

import java.util.Map;

import com.github.TKnudsen.ComplexDataObject.data.DataContainer;
import com.github.TKnudsen.ComplexDataObject.data.DataSchema;

/**
 * <p>
 * Title: ComplexDataContainer
 * </p>
 * 
 * <p>
 * Description: ComplexDataContainer stores and manages ComplexDataObjects. A
 * DataSchema contains all keys of the ComplexDataObjects.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2015-2016
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.02
 */
public class ComplexDataContainer extends DataContainer<ComplexDataObject> {

	public ComplexDataContainer(DataSchema dataSchema) {
		super(dataSchema);
	}

	public ComplexDataContainer(Map<Long, ComplexDataObject> objectsMap) {
		super(objectsMap);
	}

	public ComplexDataContainer(Iterable<ComplexDataObject> objects) {
		super(objects);
	}

}
