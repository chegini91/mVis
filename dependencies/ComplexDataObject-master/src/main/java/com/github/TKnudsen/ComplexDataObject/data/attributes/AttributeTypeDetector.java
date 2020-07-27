package com.github.TKnudsen.ComplexDataObject.data.attributes;

import java.util.Collection;

/**
 * <p>
 * Title: AttributeTypeDetector
 * </p>
 * 
 * <p>
 * Description: determines the type of an attribute (column of a table)
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public interface AttributeTypeDetector {

	public Class<?> getAttributeType(Collection<Object> values);
}
