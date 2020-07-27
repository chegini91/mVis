package com.github.TKnudsen.ComplexDataObject.model.io.parsers.objects;

/**
 * <p>
 * Title: StringParser
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class StringParser implements IObjectParser<String> {

	@Override
	public String apply(Object t) {
		if (t == null)
			return null;
		
		return String.valueOf(t);
	}

}
