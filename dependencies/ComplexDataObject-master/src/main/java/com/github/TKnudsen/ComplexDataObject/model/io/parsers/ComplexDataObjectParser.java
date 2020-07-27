package com.github.TKnudsen.ComplexDataObject.model.io.parsers;

import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataObject;
import com.github.TKnudsen.ComplexDataObject.data.interfaces.ISelfDescription;

/**
 * <p>
 * Title: ComplexDataObjectParser
 * </p>
 * 
 * <p>
 * Description: Interface for parsers to parse files for ComplexDataObjects.
 * Every line of the file is meant to include a single ComplexDataObject.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2015-2018
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.02
 */
public interface ComplexDataObjectParser extends IKeyValueObjectParser<Object, ComplexDataObject>, ISelfDescription {

}
